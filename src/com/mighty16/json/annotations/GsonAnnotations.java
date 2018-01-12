package com.mighty16.json.annotations;


public class GsonAnnotations implements AnnotationGenerator {


    @Override
    public String getSerializeName(String jsonKey) {
        return "@SerializedName(\"" + jsonKey + "\")";
    }

    @Override
    public String getImportString() {
        return "com.google.gson.annotations.SerializedName";
    }
}
