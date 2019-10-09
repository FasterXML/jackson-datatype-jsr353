package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import javax.json.Json;
import javax.json.JsonPatch;
import java.io.IOException;

public class JsonPatchDeserializer extends StdDeserializer<JsonPatch> {

    protected final JsonValueDeserializer jsonValueDeserializer;

    public JsonPatchDeserializer(JsonValueDeserializer jsonValueDeserializer) {
        super(JsonPatch.class);
        this.jsonValueDeserializer = jsonValueDeserializer;
    }

    @Override
    public JsonPatch deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return Json.createPatch(jsonValueDeserializer._deserializeArray(jsonParser, deserializationContext));
    }

}
