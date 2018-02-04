package com.mighty16.json.annotations;


import com.mighty16.json.core.AnnotationGenerator;

public class JacksonAnnotations implements AnnotationGenerator {
    @Override
    public String getSerializeName(String jsonKey) {
        return "@JsonProperty(\"" + jsonKey + "\")";
    }

    @Override
    public String getImportString() {
        return "import com.fasterxml.jackson.annotation.JsonProperty";
    }
}
