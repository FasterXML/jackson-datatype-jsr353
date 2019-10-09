package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import javax.json.Json;
import javax.json.JsonMergePatch;
import java.io.IOException;

public class JsonMergePatchDeserializer extends StdDeserializer<JsonMergePatch> {

    protected final JsonValueDeserializer jsonValueDeserializer;

    public JsonMergePatchDeserializer(JsonValueDeserializer jsonValueDeserializer) {
        super(JsonMergePatch.class);
        this.jsonValueDeserializer = jsonValueDeserializer;
    }

    @Override
    public JsonMergePatch deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return Json.createMergePatch(jsonValueDeserializer._deserializeObject(jsonParser, deserializationContext));
    }

}
