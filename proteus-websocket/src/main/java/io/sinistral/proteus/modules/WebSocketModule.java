package io.sinistral.proteus.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.websocket.DefaultWebSocketService;
import io.sinistral.proteus.websocket.WebSocketService;

/**
 * Guice bindings for the proteus WebSocket endpoint framework.
 *
 * <p>Binds the application {@link Config} if no other module has, so the module can be
 * installed standalone in tests.
 *
 * @author jbauer
 */
public class WebSocketModule extends AbstractModule {

    /** Default constructor for Guice installation. */
    public WebSocketModule() {
    }

    @Override
    protected void configure() {
        bind(WebSocketService.class).to(DefaultWebSocketService.class).in(Singleton.class);
        bind(DefaultWebSocketService.class).in(Singleton.class);
    }

    /** Provides a fallback application config for standalone installs; regular installs reuse the bound instance.
     *
     * @return the loaded application config
     */
    @Provides
    @Singleton
    Config provideConfig() {
        return ConfigFactory.load();
    }
}
