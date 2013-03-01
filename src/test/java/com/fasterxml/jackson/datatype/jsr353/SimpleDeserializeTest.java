package com.fasterxml.jackson.datatype.jsr353;

import javax.json.*;

public class SimpleDeserializeTest extends TestBase
{
    public void testSimpleArray() throws Exception
    {
        final String JSON = "[1,true,\"foo\"]";
        JsonValue v = MAPPER.readValue(JSON, JsonValue.class);
        assertTrue(v instanceof JsonArray);
        JsonArray a = (JsonArray) v;
        assertEquals(3, a.size());
        assertTrue(a.get(0) instanceof JsonNumber);
        assertSame(JsonValue.TRUE, a.get(1));
        assertTrue(a.get(2) instanceof JsonString);

        // also, should work with explicit type
        JsonArray array = MAPPER.readValue(JSON, JsonArray.class);
        assertEquals(3, array.size());

        // and round-tripping ought to be ok:
        assertEquals(JSON, serializeAsString(v));
    }

    public void testNestedArray() throws Exception
    {
        final String JSON = "[1,[false,45],{\"foo\":13}]";
        JsonValue v = MAPPER.readValue(JSON, JsonValue.class);
        assertTrue(v instanceof JsonArray);
        JsonArray a = (JsonArray) v;
        assertEquals(3, a.size());
        assertTrue(a.get(0) instanceof JsonNumber);
        assertTrue(a.get(1) instanceof JsonArray);
        assertTrue(a.get(2) instanceof JsonObject);

        // and round-tripping ought to be ok:
        assertEquals(JSON, serializeAsString(v));
    }

    public void testSimpleObject() throws Exception
    {
        final String JSON = "{\"a\":12.5,\"b\":\"Text\"}";
        JsonValue v = MAPPER.readValue(JSON, JsonValue.class);
        assertTrue(v instanceof JsonObject);
        JsonObject ob = (JsonObject) v;
        assertEquals(2, ob.size());

        assertTrue(ob.get("a") instanceof JsonNumber);
        assertEquals(12.5, ((JsonNumber) ob.get("a")).doubleValue());
        assertTrue(ob.get("b") instanceof JsonString);
        assertEquals("Text", ((JsonString) ob.get("b")).getString());

        // also, should work with explicit type
        ob = MAPPER.readValue(JSON, JsonObject.class);
        assertEquals(2, ob.size());

        // and round-tripping ought to be ok:
        assertEquals(JSON, serializeAsString(v));
    }

    public void testNestedObject() throws Exception
    {
        final String JSON = "{\"array\":[1,2],\"obj\":{\"first\":true}}";
        JsonValue v = MAPPER.readValue(JSON, JsonValue.class);
        assertTrue(v instanceof JsonObject);
        JsonObject ob = (JsonObject) v;
        assertEquals(2, ob.size());

        assertTrue(ob.get("array") instanceof JsonArray);
        assertEquals(2, ((JsonArray) ob.get("array")).size());
        assertTrue(ob.get("obj") instanceof JsonObject);
        assertEquals(1, ((JsonObject) ob.get("obj")).size());

        // also, should work with explicit type
        ob = MAPPER.readValue(JSON, JsonObject.class);
        assertEquals(2, ob.size());

        // and round-tripping ought to be ok:
        assertEquals(JSON, serializeAsString(v));
    }
}
