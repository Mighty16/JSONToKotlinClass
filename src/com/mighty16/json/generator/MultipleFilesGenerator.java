package com.mighty16.json.generator;

import com.mighty16.json.core.FileSaver;
import com.mighty16.json.core.AnnotationGenerator;
import com.mighty16.json.core.LanguageResolver;
import com.mighty16.json.core.models.ClassModel;
import java.util.List;

public class MultipleFilesGenerator extends KotlinFileGenerator {


    public MultipleFilesGenerator(FileSaver fileSaver, LanguageResolver resolver, AnnotationGenerator annotations) {
        super(resolver, annotations, fileSaver);
    }

    @Override
    public void generateFiles(String packageName, List<ClassModel> classDataList) {
        for (ClassModel classData : classDataList) {
            classData.packageName = packageName;

            StringBuilder builder = new StringBuilder();

            builder.append(String.format(PACKAGE_BLOCK, classData.packageName));
            if (annotations != null) {
                String importString = annotations.getImportString() + "\n\n";
                builder.append(importString);
            }

            String sourceText = builder.toString() + generateFileContentForClass(classData);
            fileSaver.saveFile(resolver.getFileName(classData.name), sourceText);
        }

        if (listener != null) {
            listener.onFilesGenerated(classDataList.size());
        }
    }
}
