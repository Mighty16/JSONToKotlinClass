package com.mighty16.json;


import com.mighty16.json.models.ClassModel;
import org.json.JSONObject;
import java.util.List;

public abstract class JsonParser {

    protected TypesResolver typesResolver;

    public JsonParser(TypesResolver resolver) {
        this.typesResolver = resolver;
    }

    public abstract void parse(JSONObject json, String rootClassName);

    public abstract List<ClassModel> getClasses();

}
