package com.mighty16.json.resolver;

import com.mighty16.json.core.LanguageResolver;
import com.mighty16.json.core.models.FieldModel;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

public class KotlinResolver extends LanguageResolver {

    private static final String INT = "Int";
    private static final String LONG = "Long";
    private static final String STRING = "String";
    private static final String DOUBLE = "Double";
    private static final String BOOLEAN = "Boolean";

    private HashMap<String, String> types;
    private HashMap<String, Object> defaultvalues;

    public KotlinResolver() {
        types = new HashMap<>();
        types.put(TYPE_INTEGER, INT);
        types.put(TYPE_LONG, LONG);
        types.put(TYPE_STRING, STRING);
        types.put(TYPE_DOUBLE, DOUBLE);
        types.put(TYPE_BOOLEAN, BOOLEAN);

        defaultvalues = new HashMap<>();

        defaultvalues.put(INT, 0);
        defaultvalues.put(LONG, 0);
        defaultvalues.put(STRING, "\"\"");
        defaultvalues.put(DOUBLE, 0.0);
        defaultvalues.put(BOOLEAN, false);
    }

    @Override
    public String resolve(String javaType) {
        String type = types.get(javaType);
        if (type == null) {
            return StringUtils.capitalize(javaType);// + "?";
        }
        return type;
    }

    @Override
    public String getClassName(String jsonKey) {
        String result = toCamelCase(jsonKey);
        return StringUtils.capitalize(result);
    }

    @Override
    public String getFieldName(String jsonName) {
        String name;
        if (jsonName.toUpperCase().equals(jsonName)) {
            name = jsonName.toLowerCase();
        } else {
            name = jsonName.substring(0, 1).toLowerCase() + jsonName.substring(1);
        }
        return toCamelCase(name);
    }

    @Override
    public String getDefaultValue(String type) {
        if (type.endsWith("?")) {
            return "null";
        }
        return String.valueOf(defaultvalues.get(type));
    }

    @Override
    public String getArrayType(String type) {
        String result = toCamelCase(type);
        return "List<" + result + ">?";
    }

    @Override
    public String getArrayOriginalValue() {
        return "Array";
    }

    @Override
    public String getObjectOriginalValue() {
        return "Object";
    }

    @Override
    public String getArrayItemOriginalValue(String type) {
        return type + "Item";
    }

    @Override
    public String getModifier(boolean mutable) {
        return mutable ? "var" : "val";
    }

    @Override
    public String getFieldTypeAndValue(FieldModel field) {
        String type = field.optional ? field.type + "?" : field.type;
        return (field.defaultValue != null && field.defaultValue.length() > 0) ? type + " = " + field.defaultValue : type;
    }

    @Override
    public boolean isModifierMutable(String modifier) {
        return modifier.equals("var");
    }

    @Override
    public String getFileName(String className) {
        return className + ".kt";
    }

    private String toCamelCase(String name) {
        String result;
        int nonCharPos = getNoCharPosition(name);
        if (nonCharPos != -1) {
            result = name.substring(0, nonCharPos) + StringUtils.capitalize(name.substring(nonCharPos + 1));
            return toCamelCase(result);
        } else {
            result = name;
        }
        return result;
    }
}
