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
 * An abstract base class for a Proteus service.
 *
 * @author jbauer
 */
public interface  BaseService extends Module, Service
{

}



