package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class JSR353Module extends SimpleModule
{
    private static final long serialVersionUID = 1L;

    public JSR353Module()
    {
        super(Version.unknownVersion()); //ModuleVersion.instance.version());

        // first deserializers
//        addDeserializer(DateMidnight.class, new DateMidnightDeserializer());

        // then serializers:
//        addSerializer(DateMidnight.class, new DateMidnightSerializer());
    }
}