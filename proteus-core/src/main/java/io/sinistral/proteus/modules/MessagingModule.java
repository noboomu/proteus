package io.sinistral.proteus.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import io.sinistral.proteus.messaging.DefaultEventBusService;
import io.sinistral.proteus.messaging.EventBusService;
import io.sinistral.proteus.messaging.nats.DefaultNatsBridgeService;
import io.sinistral.proteus.messaging.nats.NatsBridgeService;

/**
 * Guice bindings for the proteus messaging services.
 *
 * @author jbauer
 */
@Singleton
public class MessagingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EventBusService.class).to(DefaultEventBusService.class).in(Singleton.class);
        bind(DefaultEventBusService.class).in(Singleton.class);
        bind(NatsBridgeService.class).to(DefaultNatsBridgeService.class).in(Singleton.class);
        bind(DefaultNatsBridgeService.class).in(Singleton.class);
    }
}
