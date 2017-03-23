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
    private static final long serialVersionUID = 1L;

    public JsonValueSerializer() {
        super(JsonValue.class);
    }

    /*
    /**********************************************************
    /* Public API
    /**********************************************************
     */
    
    @Override
    public void serialize(JsonValue value, JsonGenerator g, SerializerProvider provider)
        throws IOException
    {
        switch (value.getValueType()) {
        case ARRAY:
            g.writeStartArray();
            serializeArrayContents((JsonArray) value, g, provider);
            g.writeEndArray();
            break;
        case OBJECT:
            g.writeStartObject();
            serializeObjectContents((JsonObject) value, g, provider);
            g.writeEndObject();
            break;
        default: // value type of some kind (scalar)
            serializeScalar(value, g, provider);
        }
    }

    @Override
    public void serializeWithType(JsonValue value, JsonGenerator g, SerializerProvider provider,
            TypeSerializer typeSer)
        throws IOException
    {
        typeSer.writeTypePrefixForScalar(value, g);
        switch (value.getValueType()) {
        case ARRAY:
            g.writeStartArray();
            serializeArrayContents((JsonArray) value, g, provider);
            g.writeEndArray();
            break;
        case OBJECT:
            g.writeStartObject();
            serializeObjectContents((JsonObject) value, g, provider);
            g.writeEndObject();
            break;
        default: // value type of some kind (scalar)
            serializeScalar(value, g, provider);
        }
        typeSer.writeTypeSuffixForScalar(value, g);
    }

    /*
    /**********************************************************
    /* Internal methods
    /**********************************************************
     */

    protected void serializeScalar(JsonValue value,
            JsonGenerator g, SerializerProvider provider)
        throws IOException
    {
        switch (value.getValueType()) {
        case FALSE:
            g.writeBoolean(false);
            break;
        case NULL: // hmmh. explicit nulls... I guess we may get them
            g.writeNull();
            break;
        case NUMBER:
            {
                JsonNumber num = (JsonNumber) value;
                if (num.isIntegral()) {
                    g.writeNumber(num.longValue());
                } else {
                    /* 26-Feb-2013, tatu: Apparently no way to know if we need heavy BigDecimal
                     *   or not. Let's err on side of correct-if-slow to avoid losing precision.
                     */
                    g.writeNumber(num.bigDecimalValue());
                }
            }
            break;
        case STRING:
            g.writeString(((JsonString) value).getString());
            break;
        case TRUE:
            g.writeBoolean(true);
            break;
        default:
            break;
//        default: // should never happen as array, object should not be called
//            throw new IllegalStateException("Unrecognized scalar JsonValue type: "+value.getClass().getName();
        }        
    }

    protected void serializeArrayContents(JsonArray values,
            JsonGenerator g, SerializerProvider provider)
        throws IOException
    {
        if (!values.isEmpty()) {
            for (JsonValue value : values) {
                serialize(value, g, provider);
            }
        }
    }

    protected void serializeObjectContents(JsonObject ob,
            JsonGenerator g, SerializerProvider provider)
        throws IOException
    {
        if (!ob.isEmpty()) {
            for (Map.Entry<String, JsonValue> entry : ob.entrySet()) {
                g.writeFieldName(entry.getKey());
                serialize(entry.getValue(), g, provider);
            }
        }
        
    }
}
