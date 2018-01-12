package com.mighty16.json.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.annotations.AnnotationGenerator;
import com.mighty16.json.models.ClassModel;
import java.util.List;

public class SingleFileGenerator extends SourceFilesGenerator {


    private String fileName;

    public SingleFileGenerator(LanguageResolver resolver, AnnotationGenerator annotations, String fileName) {
        super(resolver, annotations);
        this.fileName = fileName;
    }

    @Override
    public void generateFiles(PsiDirectory directory, List<ClassModel> classDataList) {
        Project project = directory.getProject();
        PsiFileFactory factory = PsiFileFactory.getInstance(project);
        PsiDirectoryFactory directoryFactory = PsiDirectoryFactory.getInstance(directory.getProject());

        String packageName = directoryFactory.getQualifiedName(directory, true);

        final StringBuilder resultFileContent = new StringBuilder();

        resultFileContent.append(String.format(SourceFilesGenerator.PACKAGE_BLOCK, packageName));

        int initialLength = resultFileContent.length();

        if (annotations != null) {
            resultFileContent.insert(initialLength, "\n" + "import " + annotations.getImportString() + "\n\n");
        }

        for (ClassModel classData : classDataList) {
            resultFileContent.append(generateFileContentForClass(classData) + "\n\n\n");
        }

        saveFile(factory,directory,resolver.getFileName(fileName),resultFileContent.toString());

        if (listener != null) {
            listener.onFilesGenerated(1);
        }
    }
}
