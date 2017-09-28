package com.mighty16.json.kotlin;


import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.mighty16.json.annotations.AnnotationGenerator;
import com.mighty16.json.SourceFilesGenerator;
import com.mighty16.json.TypesResolver;
import com.mighty16.json.models.ClassModel;
import com.mighty16.json.models.FieldModel;

import java.util.List;

public class KotlinFilesGenerator extends SourceFilesGenerator {

    private static final String CLASS_HEADER_BLOCK = "data class %s (";
    private static final String CLASS_END_BLOCK = ")";

    public KotlinFilesGenerator(TypesResolver resolver, AnnotationGenerator annotations) {
        super(resolver, annotations);
    }

    @Override
    public void generateFiles(PsiDirectory directory, List<ClassModel> classDataList) {
        WriteCommandAction.runWriteCommandAction(directory.getProject(), new Runnable() {
            @Override
            public void run() {
                PsiFileFactory factory = PsiFileFactory.getInstance(directory.getProject());
                for (ClassModel classData : classDataList) {
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
        builder.append(String.format(CLASS_HEADER_BLOCK, classData.name));
        List<FieldModel> fields = classData.fields;
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            FieldModel field = fields.get(i);
            if (field.enabled) {
                if (annotations != null) {
                    if (field.needsSerializesName()) {
                        builder.append(annotations.getSerializeName(field.jsonName)).append("\n");
                    }
                }
                String defaultValue = resolver.getDefaultValue(field.type);
                String type = defaultValue != null ? (field.type + " = " + defaultValue) : field.type + " = null";
                builder.append("val ")
                        .append(field.name)
                        .append(": ")
                        .append(type);
                if (i < size - 1) {
                    builder.append(",\n");
                }
            }
        }
        builder.append(CLASS_END_BLOCK);
        return builder.toString();
    }

    @Override
    public String getFileName(String className) {
        return className + ".kt";
    }
}
