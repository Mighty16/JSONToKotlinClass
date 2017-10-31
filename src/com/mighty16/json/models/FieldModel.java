package com.mighty16.json.models;

public class FieldModel {

    public static final String TYPE_STRING = "String";
    public static final String TYPE_DOUBLE = "Double";
    public static final String TYPE_LONG = "Long";
    public static final String TYPE_INT = "Int";

    public String jsonName;
    public String name;
    public String type;
    public String originalValue;
    public boolean enabled;
    public boolean mutable;

    public FieldModel(String jsonName, String name, String type, String originalValue) {
        this.jsonName = jsonName;
        this.name = name;
        this.type = type;
        this.originalValue = originalValue;
        this.enabled = true;
    }

    public boolean needsSerializesName() {
        return !name.equals(jsonName);
    }

}
