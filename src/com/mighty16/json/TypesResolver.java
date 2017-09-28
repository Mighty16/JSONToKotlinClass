package com.mighty16.json;

public abstract class TypesResolver {

    public static final String TYPE_INTEGER = "Integer";
    public static final String TYPE_LONG = "Long";
    public static final String TYPE_STRING = "String";
    public static final String TYPE_DOUBLE = "Double";
    public static final String TYPE_BOOLEAN = "Boolean";

    public abstract String resolve(String javaType);

    public abstract String getClassName(String jsonKey);

    public abstract String getFieldName(String jsonKey);

    public abstract String getDefaultValue(String type);

    public abstract String getArrayType(String type);

    public abstract boolean canChangeType(String type);

    public int getNoCharPosition(String name) {
        char[] chars = name.toCharArray();
        int pos = 0;
        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return pos;
            }
            pos++;
        }
        return -1;
    }
}
