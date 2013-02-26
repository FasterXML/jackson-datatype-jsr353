package com.fasterxml.jackson.datatype.jsr353;

import java.io.IOException;

import javax.json.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class TestBase extends junit.framework.TestCase
{
    protected final static JSR353Module MODULE = new JSR353Module();
    
    protected final static ObjectMapper MAPPER = new ObjectMapper()
        .registerModule(MODULE);

    protected String serializeAsString(JsonValue node) throws IOException {
        return MAPPER.writeValueAsString(node);
    }

    protected JsonArrayBuilder arrayBuilder() {
        return MODULE._builderFactory.createArrayBuilder();
    }

    protected JsonObjectBuilder objectBuilder() {
        return MODULE._builderFactory.createObjectBuilder();
    }
}
