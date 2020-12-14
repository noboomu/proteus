package io.sinistral.proteus.services;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract base class for a Proteus service.
 *
 * @author jbauer
 */
@Singleton
public abstract class DefaultService extends AbstractIdleService implements BaseService
{
    private static Logger log = LoggerFactory.getLogger(DefaultService.class.getCanonicalName());

    /*
     * @see com.google.inject.AbstractModule#configure()
     */
    @Inject
    protected Config config;

    public DefaultService()
    {
    }

    /*
     *  (non-Javadoc)
     * @see com.google.inject.Module#configure(com.google.inject.Binder)
     */
    @Override
    public void configure(Binder binder)
    {

    }

    /*
    //* @see com.google.common.util.concurrent.AbstractIdleService#shutDown()
    */
    @Override
    protected void shutDown() throws Exception
    {
        log.info("Stopping " + this.getClass().getSimpleName());

    }

    /*
     * @see com.google.common.util.concurrent.AbstractIdleService#startUp()
     */
    @Override
    protected void startUp() throws Exception
    {
        log.info("Starting " + this.getClass().getSimpleName());

    }





}



