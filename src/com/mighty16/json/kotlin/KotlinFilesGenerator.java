package com.mighty16.json.kotlin;


import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.mighty16.json.annotations.AnnotationGenerator;
import com.mighty16.json.SourceFilesGenerator;
import com.mighty16.json.TypesResolver;
import com.mighty16.json.models.ClassModel;
import com.mighty16.json.models.FieldModel;

import java.util.Arrays;
import java.util.List;

public class KotlinFilesGenerator extends SourceFilesGenerator {

    private static final String PACKAGE_BLOCK = "package %s\n\n";
    private static final String CLASS_HEADER_BLOCK = "data class %s(";
    private static final String CLASS_END_BLOCK = ")";

    public KotlinFilesGenerator(TypesResolver resolver, AnnotationGenerator annotations) {
        super(resolver, annotations);
    }

    @Override
    public void generateFiles(PsiDirectory directory, List<ClassModel> classDataList) {
        Project project = directory.getProject();
        WriteCommandAction.runWriteCommandAction(directory.getProject(), new Runnable() {
            @Override
            public void run() {
                PsiFileFactory factory = PsiFileFactory.getInstance(project);
                PsiDirectoryFactory directoryFactory = PsiDirectoryFactory.getInstance(directory.getProject());
                for (ClassModel classData : classDataList) {
                    classData.packageName = directoryFactory.getQualifiedName(directory, true);
                    String sourceText = generateFileContentForClass(classData);
                    PsiFile classFile = factory.createFileFromText(getFileName(classData.name), KotlinFileType.INSTANCE, sourceText);
                    directory.add(classFile);
                }
            }
        });
    }

    @Override
    public String generateFileContentForClass(ClassModel classData) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(PACKAGE_BLOCK, classData.packageName));

        String classNameLine = String.format(CLASS_HEADER_BLOCK, classData.name);
        final String gapString = getGapString(classNameLine.length());

        builder.append(classNameLine);
        List<FieldModel> fields = classData.fields;
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            FieldModel field = fields.get(i);
            if (field.enabled) {
                if (annotations != null) {
                    if (field.needsSerializesName()) {
                        builder.append(annotations.getSerializeName(field.jsonName)).append("\n"+gapString);
                    }
                }
                String defaultValue = resolver.getDefaultValue(field.type);
                String type = defaultValue != null ? (field.type + " = " + defaultValue) : field.type + " = null";
                builder.append("val ")
                        .append(field.name)
                        .append(": ")
                        .append(type);
                if (i < size - 1) {
                    builder.append(",\n" + gapString);
                }
            }
        }
        builder.append(CLASS_END_BLOCK);
        return builder.toString();
    }

    private String getGapString(int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }


    @Override
    public String getFileName(String className) {
        return className + ".kt";
    }
}
