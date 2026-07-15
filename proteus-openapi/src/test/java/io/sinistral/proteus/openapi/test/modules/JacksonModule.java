package io.sinistral.proteus.openapi.test.modules;

import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.util.JsonRecyclerPools;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.module.blackbird.BlackbirdModule;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

@Singleton
public class JacksonModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        JsonFactory factory = JsonFactory.builder()
            .recyclerPool(JsonRecyclerPools.sharedConcurrentDequePool())
            .build();

        ObjectMapper objectMapper = JsonMapper.builder(factory)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true)
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .addModule(new BlackbirdModule())
            .build();

        this.bind(ObjectMapper.class).toInstance(objectMapper);
    }
}