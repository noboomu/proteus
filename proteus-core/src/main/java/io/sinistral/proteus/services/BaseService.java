package io.sinistral.proteus.services;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.Service;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contract for application-managed services: a Guice {@link Module} that is also a Guava
 * {@link Service}, so a registered service can bind dependencies and run under the
 * application's {@code ServiceManager}.
 *
 * @author jbauer
 */
public interface  BaseService extends Module, Service
{

}



