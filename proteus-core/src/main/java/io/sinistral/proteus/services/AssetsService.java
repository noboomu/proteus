package io.sinistral.proteus.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.undertow.predicate.TruePredicate;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Methods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.function.Supplier;

/**
 * A service for serving static assets from a directory.
 *
 * @author jbauer
 */
@Singleton
public class AssetsService extends DefaultService implements Supplier<RoutingHandler>
{

    private static final Logger log = LoggerFactory.getLogger(AssetsService.class.getName());

    /** Registry of discovered endpoints used to skip asset-shadowed routes. */
    @Inject
    @Named("registeredEndpoints")
    protected Set<EndpointInfo> registeredEndpoints;
    /** The application routing handler to extend with asset routes. */
    @Inject
    protected RoutingHandler router;
    /** The {@code assets} configuration block. */
    @Inject
    @Named("assets")
    protected Config serviceConfig;

    /** Creates the assets service, invoked by Guice. */
    public AssetsService()
    {
    }

    /**
     * Returns the start up.
     *
    * @throws Exception when the operation fails
     */
    @Override
    protected void startUp() throws Exception
    {
        super.startUp();
        router.addAll(this.get());
    }

    /**
     * Builds the asset routing handler.
     *
     * @return the routing handler serving assets
     */
    public RoutingHandler get()
    {
        RoutingHandler router = new RoutingHandler();
        final String assetsPath = serviceConfig.getString("path");
        final String assetsDirectoryName = serviceConfig.getString("dir");
        final Integer assetsCacheTime = serviceConfig.getInt("cache.time");

        Path path = Paths.get(assetsDirectoryName);
        File pathFile = path.toFile();

        if(!pathFile.exists())
        {
            try
            {
                pathFile.mkdirs();

            } catch( Exception e )
            {
                log.error("Failed to create assets directory",e);
            }
        }

        final FileResourceManager fileResourceManager = new FileResourceManager(pathFile);

        router.add(Methods.GET,
                assetsPath + "/*",
                io.undertow.Handlers.rewrite("regex('" + assetsPath + "/(.*)')",
                        "/$1",
                        getClass().getClassLoader(),
                        new ResourceHandler(fileResourceManager).setCachable(TruePredicate.instance()).setCacheTime(assetsCacheTime)));

        this.registeredEndpoints.add(EndpointInfo.builder()
                .withConsumes("*/*")
                .withProduces("*/*")
                .withPathTemplate(assetsPath)
                .withControllerName(this.getClass().getSimpleName())
                .withMethod(Methods.GET)
                .build());

        return router;
    }
}



