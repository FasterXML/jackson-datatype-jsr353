package com.fasterxml.jackson.datatype.jsr353;

import javax.json.JsonObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DeserViaCreatorTest extends TestBase
{
    static class Pojo {
        String text;
        JsonObject object;

        @JsonCreator
        public Pojo(@JsonProperty("s") String s, @JsonProperty("o") JsonObject o) {
            text = s;
            object = o;
        }
    }

    public void testCreatorDeser() throws Exception
    {
        final ObjectMapper mapper = newMapper();
        Pojo p = mapper.readerFor(Pojo.class)
            .readValue( "{\"s\": \"String\", \"o\": { \"a\": 1, \"b\": \"2\" } }");
        assertNotNull(p);

        p = mapper.readerFor(Pojo.class).readValue("{\"s\": \"String\"}");
        assertNotNull(p);
    }
}
