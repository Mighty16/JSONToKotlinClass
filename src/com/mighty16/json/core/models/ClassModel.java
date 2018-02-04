package com.mighty16.json.core.models;

import java.util.ArrayList;
import java.util.List;

public class ClassModel {

    public String packageName;
    public String name;
    public List<FieldModel> fields;

    public ClassModel(String name) {
        this.name = name;
        fields = new ArrayList<>();
    }

    public void addField(FieldModel field) {
        fields.add(field);
    }

    @Override
    public String toString() {
        return name;
    }

}
