package com.fasterxml.jackson.datatype.jsr353;

import java.io.IOException;
import java.util.Map;

import javax.json.*;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JsonValueSerializer extends StdSerializer<JsonValue>
{
    public JsonValueSerializer() {
        super(JsonValue.class);
    }

    /*
    /**********************************************************
    /* Public API
    /**********************************************************
     */
    
    @Override
    public void serialize(JsonValue value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonGenerationException
    {
        switch (value.getValueType()) {
        case ARRAY:
            jgen.writeStartArray();
            serializeArrayContents((JsonArray) value, jgen, provider);
            jgen.writeEndArray();
            break;
        case OBJECT:
            jgen.writeStartObject();
            serializeObjectContents((JsonObject) value, jgen, provider);
            jgen.writeEndObject();
            break;
        default: // value type of some kind (scalar)
            serializeScalar(value, jgen, provider);
        }
    }

    @Override
    public void serializeWithType(JsonValue value, JsonGenerator jgen, SerializerProvider provider,
            TypeSerializer typeSer)
        throws IOException, JsonProcessingException
    {
        typeSer.writeTypePrefixForScalar(value, jgen);
        switch (value.getValueType()) {
        case ARRAY:
            jgen.writeStartArray();
            serializeArrayContents((JsonArray) value, jgen, provider);
            jgen.writeEndArray();
            break;
        case OBJECT:
            jgen.writeStartObject();
            serializeObjectContents((JsonObject) value, jgen, provider);
            jgen.writeEndObject();
            break;
        default: // value type of some kind (scalar)
            serializeScalar(value, jgen, provider);
        }
        typeSer.writeTypeSuffixForScalar(value, jgen);
    }

    /*
    /**********************************************************
    /* Internal methods
    /**********************************************************
     */

    protected void serializeScalar(JsonValue value,
            JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        switch (value.getValueType()) {
        case FALSE:
            jgen.writeBoolean(false);
            break;
        case NULL: // hmmh. explicit nulls... I guess we may get them
            jgen.writeNull();
            break;
        case NUMBER:
            {
                JsonNumber num = (JsonNumber) value;
                if (num.isIntegral()) {
                    jgen.writeNumber(num.longValue());
                } else {
                    /* 26-Feb-2013, tatu: Apparently no way to know if we need heavy BigDecimal
                     *   or not. Let's err on side of correct-if-slow to avoid losing precision.
                     */
                    jgen.writeNumber(num.bigDecimalValue());
                }
            }
            break;
        case STRING:
            jgen.writeString(((JsonString) value).getString());
            break;
        case TRUE:
            jgen.writeBoolean(true);
            break;
        default:
            break;
//        default: // should never happen as array, object should not be called
//            throw new IllegalStateException("Unrecognized scalar JsonValue type: "+value.getClass().getName();
        }        
    }

    protected void serializeArrayContents(JsonArray values,
            JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        if (!values.isEmpty()) {
            for (JsonValue value : values) {
                serialize(value, jgen, provider);
            }
        }
    }

    protected void serializeObjectContents(JsonObject ob,
            JsonGenerator jgen, SerializerProvider provider)
        throws IOException, JsonProcessingException
    {
        if (!ob.isEmpty()) {
            for (Map.Entry<String, JsonValue> entry : ob.entrySet()) {
                jgen.writeFieldName(entry.getKey());
                serialize(entry.getValue(), jgen, provider);
            }
        }
        
    }
}
