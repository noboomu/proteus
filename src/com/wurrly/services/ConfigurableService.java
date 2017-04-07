/**
 * 
 */
package com.wurrly.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.wurrly.modules.ConfigModule;

/**
 * @author jbauer
 *
 */
public abstract class ConfigurableService  extends AbstractIdleService
{
	private static Logger log = LoggerFactory.getLogger(ConfigurableService.class.getCanonicalName());

	/* (non-Javadoc)
	 * @see com.google.inject.AbstractModule#configure()
	 */
	
	protected String configFile;
	
	@Inject 
	protected ConfigModule configModule;
	
	public ConfigurableService()
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
