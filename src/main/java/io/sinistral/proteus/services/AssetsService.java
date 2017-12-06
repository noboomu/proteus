/**
 * 
 */
package io.sinistral.proteus.services;

import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Supplier;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.predicate.Predicates;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Methods;

/**
 * @author jbauer
 *
 */
public class AssetsService extends BaseService implements Supplier<RoutingHandler>
{

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
	
	public RoutingHandler get()
	{ 
		RoutingHandler router = new RoutingHandler();
		
		final String assetsPath = serviceConfig.getString("path");
		final String assetsDirectoryName = serviceConfig.getString("dir") ;
		final Integer assetsCacheTime = serviceConfig.getInt("cache.time");
		
		final FileResourceManager fileResourceManager = new FileResourceManager(Paths.get(assetsDirectoryName).toFile(), 0L);

		router.add(Methods.GET, assetsPath + "/*", io.undertow.Handlers.rewrite("regex['" + assetsPath  +  "/(.*)']", "/$1", getClass().getClassLoader(), new ResourceHandler(fileResourceManager)
		.setCachable(Predicates.truePredicate())
		.setCacheTime(assetsCacheTime)
		));
		
		this.registeredEndpoints.add(EndpointInfo.builder().withConsumes("*/*").withProduces("*/*").withPathTemplate(assetsPath).withControllerName("Assets").withMethod(Methods.GET).build());

		return router;
	}

 
	@Override
	protected void startUp() throws Exception
	{
		super.startUp();
		
		router.addAll(this.get());
	}

 
	@Override
	protected void shutDown() throws Exception
	{
		super.shutDown();

	}

}
