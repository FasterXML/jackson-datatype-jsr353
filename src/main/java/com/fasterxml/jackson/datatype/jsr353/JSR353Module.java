package com.fasterxml.jackson.datatype.jsr353;

import java.util.Collections;

import javax.json.*;
import javax.json.spi.JsonProvider;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class JSR353Module extends SimpleModule
{
    private static final long serialVersionUID = 1L;

    protected final JsonBuilderFactory _builderFactory;

    public JSR353Module()
    {
        super(PackageVersion.VERSION); //ModuleVersion.instance.version());

        JsonProvider jp = JsonProvider.provider();
        _builderFactory = jp.createBuilderFactory(Collections.<String,Object>emptyMap());

//         JsonArrayBuilder arrayBuilder = _builderFactory.createArrayBuilder();
//         JsonObjectBuilder objectBuilder = _builderFactory.createObjectBuilder();
        
        // First Serializers, as they are easier, can use SimpleXxx and they'll match
        
        // first deserializers
        addSerializer(JsonValue.class, new JsonValueSerializer());
//        addDeserializer(JsonArray.class, new DateMidnightDeserializer());

        // then serializers:
//        addSerializer(DateMidnight.class, new DateMidnightSerializer());
    }
}