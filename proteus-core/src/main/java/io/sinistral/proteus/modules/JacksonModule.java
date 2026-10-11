package io.sinistral.proteus.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import tools.jackson.core.util.BufferRecycler;
import tools.jackson.core.util.RecyclerPool;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.util.JsonRecyclerPools;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.module.blackbird.BlackbirdModule;

/** Guice module that binds the shared Jackson 3 ObjectMapper. */
@Singleton
public class JacksonModule extends AbstractModule {

    /** Jackson buffer recycler strategy: threadLocal, concurrentDeque, bounded, or none. */
    protected final String recyclerPool;

    /** Creates the module reading {@code jackson.recyclerPool} from the loaded application config. */
    public JacksonModule() {
        this(ConfigFactory.load());
    }

    /**
     * Creates the module from an explicit config.
     *
     * @param config configuration providing {@code jackson.recyclerPool}
     */
    public JacksonModule(Config config) {
        this.recyclerPool = config.hasPath("jackson.recyclerPool")
            ? config.getString("jackson.recyclerPool")
            : "threadLocal";
    }

    /**
     * Resolves the configured recycler pool name.
     *
     * @return the pool shared by every generator and parser the mapper creates
     */
    protected RecyclerPool<BufferRecycler> recyclerPool() {
        return switch (recyclerPool) {
            case "threadLocal" -> JsonRecyclerPools.threadLocalPool();
            case "concurrentDeque" -> JsonRecyclerPools.sharedConcurrentDequePool();
            case "bounded" -> JsonRecyclerPools.sharedBoundedPool();
            case "none" -> JsonRecyclerPools.nonRecyclingPool();
            default -> throw new IllegalStateException(
                "jackson.recyclerPool must be threadLocal, concurrentDeque, bounded, or none, got '" + recyclerPool + "'"
            );
        };
    }

    /**
     * Binds the shared ObjectMapper instance.
     *
     */
    @Override
    protected void configure() {
        JsonFactory factory = JsonFactory.builder()
            .recyclerPool(recyclerPool())
            .build();

        ObjectMapper objectMapper = JsonMapper.builder(factory)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(
                DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT,
                true
            )
            .configure(
                DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT,
                true
            )
            .configure(DeserializationFeature.EAGER_DESERIALIZER_FETCH, true)
            .configure(
                DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,
                true
            )
            .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .addModule(new BlackbirdModule())
            .build();

        this.bind(ObjectMapper.class).toInstance(objectMapper);
    }
}
