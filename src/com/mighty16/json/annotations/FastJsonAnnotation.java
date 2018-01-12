package com.mighty16.json.annotations;


public class FastJsonAnnotation implements AnnotationGenerator {


    @Override
    public String getSerializeName(String jsonKey) {
        return "@JSONField(name=\"" + jsonKey + "\")";
    }

    @Override
    public String getImportString() {
        return "com.alibaba.fastjson.JSON";
    }
}
