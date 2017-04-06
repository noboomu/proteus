/**
 * 
 */
package com.wurrly.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;

/**
 * @author jbauer
 *
 */
public abstract class AbstractServerModule  extends AbstractIdleService
{
	private static Logger log = LoggerFactory.getLogger(AbstractServerModule.class.getCanonicalName());

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	
	protected String configFile;
	
	@Inject 
	protected ConfigModule configModule;
	
	public AbstractServerModule()
	{
	 
	}
	
 
	protected void configure()
	{
		log.debug("Configuring : " + this.getClass().getSimpleName());
 
		if( this.configFile != null )
		{
			configModule.bindFileConfig(this.configFile);
		}
		
	}
	
	 

}
