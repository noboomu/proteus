package io.sinistral.proteus.services;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience base for services: an idle service with the application {@code Config}
 * injected and no-op {@link #configure(Binder)}, {@link #startUp()}, and
 * {@link #shutDown()} defaults that log lifecycle transitions.
 *
 * @author jbauer
 */
@Singleton
public abstract class DefaultService extends AbstractIdleService implements BaseService
{
    private static Logger log = LoggerFactory.getLogger(DefaultService.class.getCanonicalName());

    /** Application configuration injected by Guice. */
    @Inject
    protected Config config;

    /** Creates the service, invoked by Guice. */
    public DefaultService()
    {
    }

    /**
     * Binds nothing by default; override to contribute bindings.
     *
     * @param binder the Guice binder
     */
    @Override
    public void configure(Binder binder)
    {

    }

    /**
     * Logs the stop; override to release resources.
     *
     * @throws Exception when shutdown fails
     */
    @Override
    protected void shutDown() throws Exception
    {
        log.info("Stopping " + this.getClass().getSimpleName());

    }

    /**
     * Logs the start; override to acquire resources.
     *
     * @throws Exception when startup fails
     */
    @Override
    protected void startUp() throws Exception
    {
        log.info("Starting " + this.getClass().getSimpleName());

    }





}



