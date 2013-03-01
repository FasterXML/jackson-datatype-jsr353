package com.fasterxml.jackson.datatype.jsr353;

import java.io.IOException;

import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.NumberType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class JsonValueDeserializer extends StdDeserializer<JsonValue>
{
    private static final long serialVersionUID = 1L;

    protected final JsonBuilderFactory _builderFactory;

    public JsonValueDeserializer(JsonBuilderFactory bf) {
        super(JsonValue.class);
        _builderFactory = bf;
    }
    
    @Override
    public JsonValue deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException
    {
        switch (jp.getCurrentToken()) {
        case START_OBJECT:
            return _deserializeObject(jp, ctxt);
        case START_ARRAY:
            return _deserializeArray(jp, ctxt);
        default:
            return _deserializeScalar(jp, ctxt);
        }
    }

    @Override
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt,
            TypeDeserializer typeDeser)
        throws IOException, JsonProcessingException
    {
        // we will always serialize using wrapper-array; approximated by claiming it's scalar
        return typeDeser.deserializeTypedFromScalar(jp, ctxt);
    }

    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */

    protected JsonValue _deserializeObject(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException
    {
        JsonObjectBuilder b = _builderFactory.createObjectBuilder();
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String name = jp.getCurrentName();
            JsonToken t = jp.nextToken();
            switch (t) {
            case START_ARRAY:
                b.add(name, _deserializeArray(jp, ctxt));
                break;
            case START_OBJECT:
                b.add(name, _deserializeObject(jp, ctxt));
                break;
            case VALUE_FALSE:
                b.add(name, false);
                break;
            case VALUE_TRUE:
                b.add(name, true);
                break;
            case VALUE_NULL:
                b.addNull(name);
                break;
            case VALUE_NUMBER_FLOAT:
                if (jp.getNumberType() == NumberType.BIG_DECIMAL) {
                    b.add(name, jp.getDecimalValue());
                } else {
                    b.add(name, jp.getDoubleValue());
                }
                break;
            case VALUE_NUMBER_INT:
                // very cumbersome... but has to be done
                switch (jp.getNumberType()) {
                case LONG:
                    b.add(name, jp.getLongValue());
                    break;
                case INT:
                    b.add(name, jp.getIntValue());
                    break;
                default:
                    b.add(name, jp.getBigIntegerValue());
                }
                break;
            case VALUE_STRING:
                b.add(name, jp.getText());
                break;
            default:
                throw ctxt.mappingException(JsonValue.class);
            }
        }
        return b.build();
    }

    protected JsonValue _deserializeArray(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException
    {
        JsonArrayBuilder b = _builderFactory.createArrayBuilder();
        JsonToken t;
        while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
            switch (t) {
            case START_ARRAY:
                b.add(_deserializeArray(jp, ctxt));
                break;
            case START_OBJECT:
                b.add(_deserializeObject(jp, ctxt));
                break;
            case VALUE_FALSE:
                b.add(false);
                break;
            case VALUE_TRUE:
                b.add(true);
                break;
            case VALUE_NULL:
                b.addNull();
                break;
            case VALUE_NUMBER_FLOAT:
                if (jp.getNumberType() == NumberType.BIG_DECIMAL) {
                    b.add(jp.getDecimalValue());
                } else {
                    b.add(jp.getDoubleValue());
                }
                break;
            case VALUE_NUMBER_INT:
                // very cumbersome... but has to be done
                switch (jp.getNumberType()) {
                case LONG:
                    b.add(jp.getLongValue());
                    break;
                case INT:
                    b.add(jp.getIntValue());
                    break;
                default:
                    b.add(jp.getBigIntegerValue());
                }
                break;
            case VALUE_STRING:
                b.add(jp.getText());
                break;
            default:
                throw ctxt.mappingException(JsonValue.class);
            }
        }
        return b.build();
    }

    protected JsonValue _deserializeScalar(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException
    {
        switch (jp.getCurrentToken()) {
        case VALUE_EMBEDDED_OBJECT:
            /* Not sure what to do with it -- could convert byte[] into Base64 encoded
             * if we wanted to... ?
             */
            throw ctxt.mappingException(JsonValue.class);
        case VALUE_FALSE:
            return JsonValue.FALSE;
        case VALUE_TRUE:
            return JsonValue.TRUE;
        case VALUE_NULL:
            return JsonValue.NULL;
        case VALUE_NUMBER_FLOAT:
            // very cumbersome... but has to be done
            {
                JsonArrayBuilder b = _builderFactory.createArrayBuilder();
                if (jp.getNumberType() == NumberType.BIG_DECIMAL) {
                    return b.add(jp.getDecimalValue()).build().get(0);
                }
                return b.add(jp.getDoubleValue()).build().get(0);
            }
        case VALUE_NUMBER_INT:
            // very cumbersome... but has to be done
            {
                JsonArrayBuilder b = _builderFactory.createArrayBuilder();
                switch (jp.getNumberType()) {
                case LONG:
                    return b.add(jp.getLongValue()).build().get(0);
                case INT:
                    return b.add(jp.getIntValue()).build().get(0);
                default:
                    return b.add(jp.getBigIntegerValue()).build().get(0);
                }
            }
        case VALUE_STRING:
            return _builderFactory.createArrayBuilder().add(jp.getText()).build().get(0);
        default: // errors, should never get here
//        case END_ARRAY:
//        case END_OBJECT:
//        case FIELD_NAME:
//        case NOT_AVAILABLE:
//        case START_ARRAY:
//        case START_OBJECT:
            throw ctxt.mappingException(JsonValue.class);
        }
    }
}
