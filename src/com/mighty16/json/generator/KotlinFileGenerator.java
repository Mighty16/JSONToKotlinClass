package com.mighty16.json.generator;


import com.mighty16.json.core.AnnotationGenerator;
import com.mighty16.json.core.FileSaver;
import com.mighty16.json.core.LanguageResolver;
import com.mighty16.json.core.SourceFilesGenerator;
import com.mighty16.json.core.models.ClassModel;
import com.mighty16.json.core.models.FieldModel;

import java.util.List;

public class KotlinFileGenerator extends SourceFilesGenerator {

    public static final String PACKAGE_BLOCK = "package %s\n\n";
    public static final String CLASS_HEADER_BLOCK = "data class %s(";
    public static final String CLASS_END_BLOCK = ")";

    public KotlinFileGenerator(LanguageResolver resolver, AnnotationGenerator annotations, FileSaver fileSaver) {
        super(resolver, annotations, fileSaver);
    }

    @Override
    public void generateFiles(String packageName, List<ClassModel> classDataList) {

    }

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

                String typeAndValue = resolver.getFieldTypeAndValue(field);

                builder.append(resolver.getModifier(field.mutable) + " ")
                        .append(field.name)
                        .append(": ")
                        .append(typeAndValue);
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


}
