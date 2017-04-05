/**
 * 
 */
package com.wurrly.modules;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.util.Types;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;

/**
 * @author jbauer
 */

@Singleton
public class ConfigModule extends AbstractModule
{
	private static Logger log = LoggerFactory.getLogger(ConfigModule.class.getCanonicalName());

	/**
	 * @param configFileName
	 */
	
	protected String configFile = "application.conf";
 
	
	public ConfigModule()
	{
		
	}
	
	public ConfigModule(String configFile)
	{
		this.configFile = configFile;
	}

	
	@Override
	protected void configure()
	{
		log.debug("Configuring : " + this.getClass().getSimpleName());

		if(this.configFile != null )
		{
			 this.bindConfig(fileConfig(configFile));
		}
		
	}

 
	public void bindFileConfig(String fileName)
	{
		 this.bindConfig(fileConfig(configFile));
	}
	
	@SuppressWarnings("unchecked")
	public void bindConfig(final Config config)
	{
		// root nodes
		
 		
 
		traverse(this.binder(), "", config.root());
 
		
		// terminal nodes
		for (Entry<String, ConfigValue> entry : config.entrySet())
		{
			String name = entry.getKey();
			Named named = Names.named(name);
			Object value = entry.getValue().unwrapped();
			if (value instanceof List)
			{
				List<Object> values = (List<Object>) value;
				Type listType = values.size() == 0 ? String.class : Types.listOf(values.iterator().next().getClass());
				Key<Object> key = (Key<Object>) Key.get(listType, Names.named(name));
				this.binder().bind(key).toInstance(values);
			}
			else
			{
				this.binder().bindConstant().annotatedWith(named).to(value.toString());
			}
		}
		// bind config
		this.binder().bind(Config.class).toInstance( ConfigFactory.load(config));
		
		log.info("Config:\n" + config);

		
 	}


	public static void traverse(final Binder binder, final String p, final ConfigObject root)
	{
		root.forEach((n, v) -> {
			if (v instanceof ConfigObject)
			{
				ConfigObject child = (ConfigObject) v;
				String path = p + n;
				Named named = Names.named(path);
				binder.bind(Config.class).annotatedWith(named).toInstance(child.toConfig());
				traverse(binder, path + ".", child);
			}
		});
	}
	
	
	public static Config fileConfig(final String fname)
	{
		File dir = new File(System.getProperty("user.dir"));
		File froot = new File(dir, fname);
		if (froot.exists())
		{
			return ConfigFactory.load(ConfigFactory.parseFile(froot));
		}
		else
		{
			File fconfig = new File(new File(dir, "conf"), fname);
			if (fconfig.exists())
			{
				return ConfigFactory.load(ConfigFactory.parseFile(fconfig));
			}
		}
		return ConfigFactory.empty();
	}


}
