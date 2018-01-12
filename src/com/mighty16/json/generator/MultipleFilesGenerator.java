package com.mighty16.json.generator;


import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.mighty16.json.annotations.AnnotationGenerator;
import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.models.ClassModel;

import java.util.List;

public class MultipleFilesGenerator extends SourceFilesGenerator {

    public MultipleFilesGenerator(LanguageResolver resolver, AnnotationGenerator annotations) {
        super(resolver, annotations);
    }

    @Override
    public void generateFiles(PsiDirectory directory, List<ClassModel> classDataList) {
        Project project = directory.getProject();
        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiDirectoryFactory directoryFactory = PsiDirectoryFactory.getInstance(directory.getProject());

        for (ClassModel classData : classDataList) {
            classData.packageName = directoryFactory.getQualifiedName(directory, true);

            StringBuilder builder = new StringBuilder();

            builder.append(String.format(PACKAGE_BLOCK, classData.packageName));
            if (annotations != null) {
                builder.append("import " + annotations.getImportString() + "\n\n");
            }

            String sourceText = builder.toString() + generateFileContentForClass(classData);
            saveFile(factory, directory, resolver.getFileName(classData.name), sourceText);
        }

        if (listener != null) {
            listener.onFilesGenerated(classDataList.size());
        }
    }
}
