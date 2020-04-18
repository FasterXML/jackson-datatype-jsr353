package com.fasterxml.jackson.datatype.jsr353;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;

import javax.json.JsonBuilderFactory;
import javax.json.JsonMergePatch;
import javax.json.JsonPatch;
import javax.json.spi.JsonProvider;
import java.util.Collections;

public class JSR374Module extends SimpleModule {
    private static final long serialVersionUID = 1L;

    protected final JsonBuilderFactory _builderFactory;
    protected final JsonValueDeserializer _jsonValueDeser;
    protected final JsonPatchDeserializer _jsonPatchDeser;
    protected final JsonMergePatchDeserializer _jsonMergePatchDeser;

    @SuppressWarnings("serial")
    public JSR374Module() {
        super(PackageVersion.VERSION);

        JsonProvider jp = JsonProvider.provider();
        _builderFactory = jp.createBuilderFactory(Collections.<String, Object>emptyMap());
        _jsonValueDeser = new JsonValueDeserializer(_builderFactory);
        _jsonPatchDeser = new JsonPatchDeserializer(_jsonValueDeser);
        _jsonMergePatchDeser = new JsonMergePatchDeserializer(_jsonValueDeser);

        setDeserializers(new SimpleDeserializers() {
            @Override
            public JsonDeserializer<?> findBeanDeserializer(
                    JavaType type,
                    DeserializationConfig config,
                    BeanDescription beanDesc
            ) {
                if (JsonPatch.class.isAssignableFrom(type.getRawClass())) {
                    return _jsonPatchDeser;
                }

                if (JsonMergePatch.class.isAssignableFrom(type.getRawClass())) {
                    return _jsonMergePatchDeser;
                }
                return null;
            }

            @Override // since 2.11
            public boolean hasDeserializerFor(DeserializationConfig config, Class<?> valueType) {
                return JsonPatch.class.isAssignableFrom(valueType) ||
                        JsonMergePatch.class.isAssignableFrom(valueType);
            }
        });
    }

}
