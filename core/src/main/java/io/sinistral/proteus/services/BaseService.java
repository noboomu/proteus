
package io.sinistral.proteus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;

import com.typesafe.config.Config;

/**
 * An abstract base class for a Proteus service.
 * @author jbauer
 */
@Singleton
public abstract class BaseService extends AbstractIdleService implements Module
{
    private static Logger log = LoggerFactory.getLogger(BaseService.class.getCanonicalName());

    /*
     * @see com.google.inject.AbstractModule#configure()
     */
    @Inject
    protected Config config;

    public BaseService()
    {
    }

    /*
     *  (non-Javadoc)
     * @see com.google.inject.Module#configure(com.google.inject.Binder)
     */
    @Override
    public void configure(Binder binder) {

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



