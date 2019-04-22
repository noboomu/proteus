package io.sinistral.proteus.modules;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.google.inject.AbstractModule;
import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerResponse;

public class JacksonModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        objectMapper.registerModule(new AfterburnerModule());
        objectMapper.registerModule(new Jdk8Module());

        this.bind(ObjectMapper.class).toInstance(objectMapper);

        this.requestStaticInjection(Extractors.class);
        this.requestStaticInjection(ServerResponse.class);
    }
}
