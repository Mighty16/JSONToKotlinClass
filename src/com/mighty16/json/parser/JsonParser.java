package com.mighty16.json.parser;


import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.models.ClassModel;
import org.json.JSONObject;
import java.util.List;

public abstract class JsonParser {

    protected LanguageResolver languageResolver;

    public JsonParser(LanguageResolver resolver) {
        this.languageResolver = resolver;
    }

    public abstract void parse(JSONObject json, String rootClassName);

    public abstract List<ClassModel> getClasses();

}
