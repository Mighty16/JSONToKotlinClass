package com.mighty16.json.generator;

import com.mighty16.json.core.FileSaver;
import com.mighty16.json.core.LanguageResolver;
import com.mighty16.json.core.AnnotationGenerator;
import com.mighty16.json.core.models.ClassModel;

import java.util.List;

public class SingleFileGenerator extends KotlinFileGenerator {

    private String fileName;

    public SingleFileGenerator(String fileName, LanguageResolver resolver,
                               AnnotationGenerator annotations, FileSaver fileSaver) {
        super(resolver, annotations, fileSaver);
        this.fileName = fileName;
    }

    @Override
    public void generateFiles(String packageName, List<ClassModel> classDataList) {

        final StringBuilder resultFileContent = new StringBuilder();

        resultFileContent.append(String.format(PACKAGE_BLOCK, packageName));

        int initialLength = resultFileContent.length();

        if (annotations != null) {
            resultFileContent.insert(initialLength, "\n" + annotations.getImportString() + "\n\n");
        }

        for (ClassModel classData : classDataList) {
            String content = generateFileContentForClass(classData) + "\n\n\n";
            resultFileContent.append(content);
        }

        fileSaver.saveFile(resolver.getFileName(fileName), resultFileContent.toString());

        if (listener != null) {
            listener.onFilesGenerated(1);
        }
    }
}
