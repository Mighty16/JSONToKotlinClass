package com.mighty16.json.annotations;


public class MoshiAnnotations implements AnnotationGenerator {


    @Override
    public String getSerializeName(String jsonKey) {
        return "@Json(name = \"" + jsonKey + "\")";
    }

    @Override
    public String getImportString() {
        return "import com.squareup.moshi.Json";
    }
}
