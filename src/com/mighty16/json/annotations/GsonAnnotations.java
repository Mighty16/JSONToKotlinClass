package com.mighty16.json.annotations;


import com.mighty16.json.core.AnnotationGenerator;

public class GsonAnnotations implements AnnotationGenerator {


    @Override
    public String getSerializeName(String jsonKey) {
        return "@SerializedName(\"" + jsonKey + "\")";
    }

    @Override
    public String getImportString() {
        return "import com.google.gson.annotations.SerializedName";
    }
}
