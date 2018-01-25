package com.mighty16.json.generator;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ui.UIUtil;
import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.annotations.AnnotationGenerator;
import com.mighty16.json.models.ClassModel;
import com.mighty16.json.models.FieldModel;
import org.apache.http.util.TextUtils;

import java.util.List;
import java.util.StringTokenizer;

public abstract class SourceFilesGenerator {

    public static final String PACKAGE_BLOCK = "package %s\n\n";
    public static final String CLASS_HEADER_BLOCK = "data class %s(";
    public static final String CLASS_END_BLOCK = ")";

    protected LanguageResolver resolver;
    protected AnnotationGenerator annotations;

    protected Listener listener;


    public SourceFilesGenerator(LanguageResolver resolver, AnnotationGenerator annotations) {
        this.resolver = resolver;
        this.annotations = annotations;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public abstract void generateFiles(PsiDirectory directory, List<ClassModel> classDataList);

    public String generateFileContentForClass(ClassModel classData) {

        StringBuilder builder = new StringBuilder();

        String classNameLine = String.format(CLASS_HEADER_BLOCK, classData.name);
        final String gapString = getGapString(classNameLine.length());

        builder.append(classNameLine);
        List<FieldModel> fields = classData.fields;
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            FieldModel field = fields.get(i);
            if (field.enabled) {
                if (annotations != null) {
                    builder.append(annotations.getSerializeName(field.jsonName)).append("\n" + gapString);
                }

                String type;
                if (!TextUtils.isEmpty(field.defaultValue)) {
                    type = field.type + " = " + field.defaultValue;
                } else {
                    type = field.type;
                }

                builder.append(resolver.getModifier(field.mutable) + " ")
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

    public PsiDirectory createDirectory(PsiDirectory parent, String name) throws IncorrectOperationException {
        PsiDirectory result = null;
        for (PsiDirectory dir : parent.getSubdirectories()) {
            if (dir.getName().equalsIgnoreCase(name)) {
                result = dir;
                break;
            }
        }
        if (null == result) {
            result = parent.createSubdirectory(name);
        }
        return result;
    }

    public PsiDirectory createPackage(PsiDirectory sourceDir, String qualifiedPackage) throws IncorrectOperationException {
        PsiDirectory parent = sourceDir;
        StringTokenizer token = new StringTokenizer(qualifiedPackage, ".");
        while (token.hasMoreTokens()) {
            String dirName = token.nextToken();
            parent = createDirectory(parent, dirName);
        }
        return parent;
    }

    private String getGapString(int count) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }

    protected void saveFile(PsiFileFactory factory, PsiDirectory directory, String fileName, String fileContent) {
        PsiFile classFile = factory.createFileFromText(fileName,
                resolver.getFileType(), fileContent);

        PsiFile oldFile = directory.findFile(fileName);

        if (oldFile != null) {
            int ok = Messages.showOkCancelDialog("Replace \"" + fileName + "\"?", "Replace File", UIUtil.getQuestionIcon());
            if (ok == 0) {

                WriteCommandAction.runWriteCommandAction(directory.getProject(), new Runnable() {
                    @Override
                    public void run() {
                        oldFile.delete();
                        directory.add(classFile);
                    }
                });
            }
        } else {
            WriteCommandAction.runWriteCommandAction(directory.getProject(), new Runnable() {
                @Override
                public void run() {
                    directory.add(classFile);
                }
            });
        }
    }

    public interface Listener {
        void onFilesGenerated(int filesCount);
    }

}
