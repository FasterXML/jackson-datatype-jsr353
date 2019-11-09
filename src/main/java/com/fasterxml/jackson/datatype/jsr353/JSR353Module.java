package com.fasterxml.jackson.datatype.jsr353;

import java.util.Collections;

import javax.json.*;
import javax.json.spi.JsonProvider;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

public class JSR353Module extends SimpleModule
{
    private static final long serialVersionUID = 1L;

    protected final JsonBuilderFactory _builderFactory;

    @SuppressWarnings("serial")
    public JSR353Module()
    {
        super(PackageVersion.VERSION); //ModuleVersion.instance.version());

        JsonProvider jp = JsonProvider.provider();
        _builderFactory = jp.createBuilderFactory(Collections.<String,Object>emptyMap());

//         JsonArrayBuilder arrayBuilder = _builderFactory.createArrayBuilder();
//         JsonObjectBuilder objectBuilder = _builderFactory.createObjectBuilder();
        // first deserializers
        final JsonValueDeserializer jsonValueDeser = new JsonValueDeserializer(_builderFactory);
        
        addSerializer(JsonValue.class, new JsonValueSerializer());
        setDeserializers(new SimpleDeserializers() {
            @Override
            public JsonDeserializer<?> findBeanDeserializer(JavaType type,
                    DeserializationConfig config, BeanDescription beanDesc)
                throws JsonMappingException
            {
                if (JsonValue.class.isAssignableFrom(type.getRawClass())) {
                    return jsonValueDeser;
                }
                return null;
            }

            @Override
            public JsonDeserializer<?> findCollectionDeserializer(CollectionType type,
                    DeserializationConfig config, BeanDescription beanDesc,
                    TypeDeserializer elementTypeDeserializer,
                    JsonDeserializer<?> elementDeserializer)
                throws JsonMappingException
            {
                if (JsonArray.class.isAssignableFrom(type.getRawClass())) {
                    return jsonValueDeser;
                }
                return null;
            }

            @Override
            public JsonDeserializer<?> findMapDeserializer(MapType type,
                    DeserializationConfig config, BeanDescription beanDesc,
                    KeyDeserializer keyDeserializer,
                    TypeDeserializer elementTypeDeserializer,
                    JsonDeserializer<?> elementDeserializer)
                throws JsonMappingException
            {
                if (JsonObject.class.isAssignableFrom(type.getRawClass())) {
                    return jsonValueDeser;
                }
                return null;
            }

            @Override // since 2.11
            public boolean hasDeserializerFor(DeserializationConfig config,
                    Class<?> valueType) {
                return JsonValue.class.isAssignableFrom(valueType);
            }
        });
    }
}
