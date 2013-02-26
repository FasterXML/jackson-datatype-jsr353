package com.fasterxml.jackson.datatype.jsr353;

import java.util.Collections;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.spi.JsonProvider;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JSR353Module extends SimpleModule
{
    private static final long serialVersionUID = 1L;

    public JSR353Module()
    {
        super(Version.unknownVersion()); //ModuleVersion.instance.version());

        JsonProvider jp = JsonProvider.provider();
        JsonBuilderFactory builderFactory = jp.createBuilderFactory(Collections.<String,Object>emptyMap());

        JsonArrayBuilder arrayBuilder = builderFactory.createArrayBuilder();
        JsonObjectBuilder objectBuilder = builderFactory.createObjectBuilder();
        
        // first deserializers
//        addDeserializer(DateMidnight.class, new DateMidnightDeserializer());

        // then serializers:
//        addSerializer(DateMidnight.class, new DateMidnightSerializer());
    }
}