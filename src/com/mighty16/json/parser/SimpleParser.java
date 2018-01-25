package com.mighty16.json.parser;

import com.mighty16.json.resolver.LanguageResolver;
import com.mighty16.json.models.ClassModel;
import com.mighty16.json.models.FieldModel;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class SimpleParser extends JsonParser {

    private Map<String, ClassModel> classes;

    public SimpleParser(LanguageResolver resolver) {
        super(resolver);
        classes = new HashMap<>();
    }

    @Override
    public void parse(JSONObject json, String rootClassName) {
        findClasses(json, rootClassName);
    }

    @Override
    public List<ClassModel> getClasses() {
        return new ArrayList<>(classes.values());
    }

    private void findClasses(JSONObject json, String name) {
        ClassModel classData = new ClassModel(languageResolver.getClassName(name));
        Iterator<String> iterator = json.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object val = json.get(key);

            if (val instanceof JSONObject) {
                if (classes.get(key) == null) {
                    String className = languageResolver.getClassName(key);

                    classData.addField(new FieldModel(key,
                            languageResolver.getFieldName(key),
                            languageResolver.resolve(className),
                            "Object"));

                    findClasses((JSONObject) val, key);
                }
            } else if (val instanceof JSONArray) {
                JSONArray array = (JSONArray) val;

                String typeName = StringUtils.capitalize(key);
                ClassModel parsedClass = classes.get(typeName);

                String arrayItemTypeName;

                if (parsedClass != null) {
                    classData.addField(new FieldModel(key, languageResolver.getFieldName(key), languageResolver.getArrayType(typeName),
                            "Array"));
                    arrayItemTypeName = typeName;
                } else {
                    arrayItemTypeName = typeName + "Item";
                }

                if (array.length() > 0) {
                    Object firstArrayElement = array.get(0);
                    if (firstArrayElement instanceof JSONObject) {
                        classData.addField(new FieldModel(key, languageResolver.getFieldName(key),
                                languageResolver.getArrayType(arrayItemTypeName), "Array"));
                        findClasses((JSONObject) firstArrayElement, languageResolver.resolve(arrayItemTypeName));
                    } else {
                        String type = firstArrayElement.getClass().getSimpleName();
                        classData.addField(new FieldModel(key, languageResolver.getFieldName(key),
                                languageResolver.getArrayType(type), "Array"));
                    }
                }

            } else {
                String type = val.getClass().getSimpleName();
                String resolvedType = languageResolver.resolve(type);
                FieldModel field = new FieldModel(key, languageResolver.getFieldName(key), resolvedType
                        , String.valueOf(val));
                field.defaultValue = languageResolver.getDefaultValue(resolvedType);
                classData.addField(field);
            }
        }
        String className = languageResolver.getClassName(name);
        classes.put(className, classData);
    }

}
