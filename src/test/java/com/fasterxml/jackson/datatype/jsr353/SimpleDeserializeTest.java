package com.fasterxml.jackson.datatype.jsr353;

import javax.json.*;

public class SimpleDeserializeTest extends TestBase
{
    public void testSimpleArray() throws Exception
    {
        final String JSON = "[1, true, \"foo\"]";
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
    /*
    public void testSimpleObject() throws Exception
    {
        JsonObject ob = objectBuilder()
                .add("a", 123)
                .add("b", "Text")
                .build();
        // not sure if order is guaranteed but:
        assertEquals("{\"a\":123,\"b\":\"Text\"}", serializeAsString(ob));
    }

    public void testNestedObject() throws Exception
    {
        JsonObject ob = objectBuilder()
                .add("array", arrayBuilder().add(1).add(2))
                .add("obj", objectBuilder().add("first", true))
                .build();
        // not sure if order is guaranteed but:
        assertEquals("{\"array\":[1,2],\"obj\":{\"first\":true}}", serializeAsString(ob));
    }
    */
}
