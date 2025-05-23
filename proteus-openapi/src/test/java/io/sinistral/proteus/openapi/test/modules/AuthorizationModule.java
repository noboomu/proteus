package io.sinistral.proteus.openapi.test.modules;

import com.google.inject.AbstractModule;
import io.sinistral.proteus.openapi.wrappers.BearerTokenWrapper;
import jakarta.inject.Singleton;

@Singleton
public class AuthorizationModule extends AbstractModule {



    public static
    class Validator implements BearerTokenWrapper.BearerTokenValidator {
        @Override
        public Object validate(String token) {
            return true;
        }
    }

    @Override
    protected void configure() {
        // Bind any necessary classes or interfaces here
        // For example:
        // bind(MyService.class).to(MyServiceImpl.class);

        Validator validator = new Validator();

        bind(BearerTokenWrapper.BearerTokenValidator.class).toInstance(validator);


    }
}
