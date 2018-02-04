package com.mighty16.json.core.models;

public class FieldModel {

    public String jsonName;
    public String name;
    public String type;
    public boolean optional;
    public String originalValue;
    public boolean enabled;
    public boolean mutable;
    public String defaultValue;

    public FieldModel(String jsonName, String name, String type, String originalValue) {
        this.jsonName = jsonName;
        this.name = name;
        this.type = type;
        this.originalValue = originalValue;
        this.enabled = true;
        this.optional = false;
    }


}
