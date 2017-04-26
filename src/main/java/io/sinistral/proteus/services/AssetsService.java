/**
 * 
 */
package io.sinistral.proteus.services;

import java.nio.file.Paths;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.predicate.TruePredicate;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Methods;

/**
 * @author jbauer
 *
 */
public class AssetsService extends BaseService
{
	private static Logger log = LoggerFactory.getLogger(AssetsService.class.getCanonicalName());

	@Inject
	@Named("registeredEndpoints")
	protected Set<EndpointInfo> registeredEndpoints;
 
	@Inject
	protected RoutingHandler router;
	
	@Inject
	@Named("assets")
	protected Config serviceConfig;
	
	/**
	 * 
	 */
	public AssetsService()
	{ 
	}

 
	@Override
	protected void startUp() throws Exception
	{
		super.startUp();

		final String assetsPath = serviceConfig.getString("path");
		final String assetsDirectoryName = serviceConfig.getString("dir") ;
		final Integer assetsCacheTime = serviceConfig.getInt("cache.time");
		
		final FileResourceManager fileResourceManager = new FileResourceManager(Paths.get(assetsDirectoryName).toFile());


		router.add(Methods.GET, assetsPath + "/*", io.undertow.Handlers.rewrite("regex['" + assetsPath  +  "/(.*)']", "/$1", getClass().getClassLoader(), new ResourceHandler(fileResourceManager)
		.setCachable(TruePredicate.instance())
		.setCacheTime(assetsCacheTime)
		));
		
		this.registeredEndpoints.add(EndpointInfo.builder().withConsumes("*/*").withProduces("*/*").withPathTemplate(assetsPath).withControllerName("Assets").withMethod(Methods.GET).build());

	}

 
	@Override
	protected void shutDown() throws Exception
	{
		super.shutDown();

	}

}
