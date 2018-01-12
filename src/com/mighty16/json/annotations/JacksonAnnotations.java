package com.mighty16.json.annotations;


public class JacksonAnnotations implements AnnotationGenerator {
    @Override
    public String getSerializeName(String jsonKey) {
        return "@JsonProperty(\"" + jsonKey + "\")";
    }

    @Override
    public String getImportString() {
        return "com.fasterxml.jackson.annotation.JsonProperty";
    }
}
