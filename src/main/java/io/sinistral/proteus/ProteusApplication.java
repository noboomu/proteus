/**
 * 
 */
package io.sinistral.proteus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Service.State;
import com.google.common.util.concurrent.ServiceManager;
import com.google.common.util.concurrent.ServiceManager.Listener;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Named;
import com.typesafe.config.Config;

import io.sinistral.proteus.modules.ConfigModule;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.sinistral.proteus.server.handlers.HandlerGenerator;
import io.sinistral.proteus.server.handlers.ServerDefaultHttpHandler;
import io.sinistral.proteus.services.BaseService;
import io.sinistral.proteus.utilities.SecurityOps;
import io.sinistral.proteus.utilities.TablePrinter;
import io.undertow.Undertow;
import io.undertow.Undertow.ListenerInfo;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

/**
 * @author jbauer
 */
public class ProteusApplication
{

	private static Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ProteusApplication.class.getCanonicalName());

	@Inject
	@Named("registeredControllers")
	protected Set<Class<?>> registeredControllers;

	@Inject
	@Named("registeredEndpoints")
	protected Set<EndpointInfo> registeredEndpoints;

	@Inject
	@Named("registeredServices")
	protected Set<Class<? extends BaseService>> registeredServices;

	@Inject
	protected RoutingHandler router;

	@Inject
	protected Config config;

	protected List<Class<? extends Module>> registeredModules = new ArrayList<>();

	protected Injector injector = null;
	protected ServiceManager serviceManager = null;
	protected Undertow undertow = null;
	protected Class<? extends HttpHandler> rootHandlerClass;
	protected HttpHandler rootHandler;
	protected AtomicBoolean running = new AtomicBoolean(false);
	protected List<Integer> ports = new ArrayList<>();
	protected Function<Undertow.Builder, Undertow.Builder> serverConfigurationFunction = null;

	public ProteusApplication()
	{

		injector = Guice.createInjector(new ConfigModule());
		injector.injectMembers(this);

	}

	public ProteusApplication(String configFile)
	{

		injector = Guice.createInjector(new ConfigModule(configFile));
		injector.injectMembers(this);

	}

	public ProteusApplication(URL configURL)
	{

		injector = Guice.createInjector(new ConfigModule(configURL));
		injector.injectMembers(this);

	}

	public void start()
	{
		if (this.isRunning())
		{
			log.warn("Server has already started...");
			return;
		}

		log.info("Configuring modules: " + registeredModules);

		Set<Module> modules = registeredModules.stream().map(mc -> injector.getInstance(mc)).collect(Collectors.toSet());

		injector = injector.createChildInjector(modules);

		if (rootHandlerClass == null && rootHandler == null)
		{
			log.warn("No root handler class or root HttpHandler was specified, using default ServerDefaultHttpHandler.");
			rootHandlerClass = ServerDefaultHttpHandler.class;
		}

		log.info("Starting services...");

		Set<BaseService> services = registeredServices.stream().map(sc -> injector.getInstance(sc)).collect(Collectors.toSet());

		injector = injector.createChildInjector(services);

		serviceManager = new ServiceManager(services);

		serviceManager.addListener(new Listener()
		{
			public void stopped()
			{
				undertow.stop();
				running.set(false);
			}

			public void healthy()
			{
				log.info("Services are healthy...");

				buildServer();

				undertow.start();
				
				for(ListenerInfo info : undertow.getListenerInfo())
				{
					log.debug("listener info: " + info);
					SocketAddress address = info.getAddress();
					
					if(address != null)
					{ 
						ports.add( ((java.net.InetSocketAddress) address).getPort());
					}
				}

				printStatus();

				running.set(true);
			}

			public void failure(Service service)
			{
				log.error("Service failure: " + service);
			}

		}, MoreExecutors.directExecutor());

		Runtime.getRuntime().addShutdownHook(new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					shutdown();
				} catch (TimeoutException timeout)
				{
					 log.error(timeout.getMessage(),timeout);
				}
			}
		});

		serviceManager.startAsync();

	}

	public void shutdown() throws TimeoutException
	{
		if (!this.isRunning())
		{
			log.warn("Server is not running..."); 
			return;
		}

		log.info("Shutting down...");

 		  try {
 				serviceManager.stopAsync().awaitStopped(1, TimeUnit.SECONDS);

	         } catch (TimeoutException timeout) {
	        	 log.warn("Failed to shutdown within specified timeout.");
	      }

		log.info("Shutdown complete.");
	}

	public boolean isRunning()
	{
		return this.running.get();
	}

	public void buildServer()
	{

		for (Class<?> controllerClass : registeredControllers)
		{
			HandlerGenerator generator = new HandlerGenerator("io.sinistral.proteus.controllers.handlers", controllerClass);

			injector.injectMembers(generator);

			try
			{
				Supplier<RoutingHandler> generatedRouteSupplier = injector.getInstance(generator.compileClass());

				router.addAll(generatedRouteSupplier.get());

			} catch (Exception e)
			{
				log.error("Exception creating handlers for " + controllerClass.getName() + "!!!\n" + e.getMessage(), e);
			}

		}

		this.addDefaultRoutes(router);

		HttpHandler handler;

		if (rootHandlerClass != null)
		{
			handler = injector.getInstance(rootHandlerClass);
		}
		else
		{
			handler = rootHandler;
		}
		
		int httpPort = config.getInt("application.ports.http");
		
		if(System.getProperty("http.port") != null)
		{
			httpPort = Integer.parseInt(System.getProperty("http.port"));
		}

		Undertow.Builder undertowBuilder = Undertow.builder().addHttpListener(httpPort, config.getString("application.host"))
				.setBufferSize(16 * 1024)
				.setIoThreads(Runtime.getRuntime().availableProcessors() * 2)
				.setServerOption(UndertowOptions.ENABLE_HTTP2, config.getBoolean("undertow.server.enableHttp2"))
				.setServerOption(UndertowOptions.ALWAYS_SET_DATE, config.getBoolean("undertow.server.alwaysSetDate"))
				.setSocketOption(org.xnio.Options.BACKLOG, config.getInt("undertow.socket.backlog"))
				.setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, config.getBoolean("undertow.server.alwaysSetKeepAlive"))
				.setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, config.getBoolean("undertow.server.recordRequestStartTime"))
				.setServerOption(UndertowOptions.MAX_ENTITY_SIZE, config.getBytes("undertow.server.maxEntitySize"))
                .setSocketOption(org.xnio.Options.REUSE_ADDRESSES, config.getBoolean("undertow.socket.reuseAddresses"))
				.setWorkerThreads(config.getInt("undertow.workerThreads"))
				.setHandler(handler);

 
		if (config.getBoolean("undertow.ssl.enabled"))
		{
			try
			{
				int httpsPort = config.getInt("application.ports.https");
				
				if(System.getProperty("https.port") != null)
				{
					httpsPort = Integer.parseInt(System.getProperty("https.port"));
				}
				
				KeyStore keyStore = SecurityOps.loadKeyStore(config.getString("undertow.ssl.keystorePath"), config.getString("undertow.ssl.keystorePassword"));
				KeyStore trustStore = SecurityOps.loadKeyStore(config.getString("undertow.ssl.truststorePath"), config.getString("undertow.ssl.truststorePassword"));

				undertowBuilder.addHttpsListener(httpsPort, config.getString("application.host"), SecurityOps.createSSLContext(keyStore, trustStore, config.getString("undertow.ssl.keystorePassword")));
 

			} catch (Exception e)
			{
				log.error(e.getMessage(), e);
			}
		}

		if (serverConfigurationFunction != null)
		{
			undertowBuilder = serverConfigurationFunction.apply(undertowBuilder);
		}

		this.undertow = undertowBuilder.build();

	}

	public ProteusApplication addService(Class<? extends BaseService> serviceClass)
	{
		registeredServices.add(serviceClass);
		return this;
	}

	public ProteusApplication addController(Class<?> controllerClass)
	{
		registeredControllers.add(controllerClass);
		return this;
	}

	public ProteusApplication addModule(Class<? extends Module> module)
	{
		registeredModules.add(module);
		return this;
	}

	public void setRootHandlerClass(Class<? extends HttpHandler> rootHandlerClass)
	{
		this.rootHandlerClass = rootHandlerClass;
	}

	public void setRootHandler(HttpHandler rootHandler)
	{
		this.rootHandler = rootHandler;
	}

	public Undertow getUndertow()
	{
		return undertow;
	}

	/**
	 * Allows direct access to the Undertow.Builder for custom configuration
	 * 
	 * @param serverConfigurationFunction
	 *            the serverConfigurationFunction
	 */
	public void setServerConfigurationFunction(Function<Undertow.Builder, Undertow.Builder> serverConfigurationFunction)
	{
		this.serverConfigurationFunction = serverConfigurationFunction;
	}

	/**
	 * @return the serviceManager
	 */
	public ServiceManager getServiceManager()
	{
		return serviceManager;
	}

	/**
	 * @return the config
	 */
	public Config getConfig()
	{
		return config;
	}

	public void printStatus()
	{
		Config globalHeaders = config.getConfig("globalHeaders");

		Map<String, String> globalHeadersParameters = globalHeaders.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().render()));

		StringBuilder sb = new StringBuilder();

		sb.append("\nUsing global headers: \n");
		
		List<String> tableHeaders = Arrays.asList("Header","Value");
		
		List<List<String>> tableRows = globalHeadersParameters.entrySet().stream().map( e -> {
			
			return Arrays.asList( e.getKey(), e.getValue() );
			
		}).collect(Collectors.toList());
		
		TablePrinter printer = new TablePrinter(tableHeaders, tableRows);

		sb.append(printer.toString());

		sb.append("\nRegistered endpoints: \n");
		
		tableHeaders = Arrays.asList("Method","Path","Consumes","Produces","Controller");
		
		tableRows = this.registeredEndpoints.stream().sorted().map( e -> {
				
			return Arrays.asList( e.getMethod().toString(), e.getPathTemplate(), String.format("[%s]", e.getConsumes()), String.format("[%s]", e.getProduces()), String.format("(%s.%s)", e.getControllerName() , e.getControllerMethod() ) );
			
		}).collect(Collectors.toList());
		
		printer = new TablePrinter(tableHeaders, tableRows);
		
		sb.append(printer.toString());
		
		sb.append("\nRegistered services: \n");

		ImmutableMultimap<State, Service> serviceStateMap = this.serviceManager.servicesByState();

		tableHeaders = Arrays.asList("Service","State");
		
		tableRows = serviceStateMap.asMap().entrySet().stream().flatMap(e -> {

			return e.getValue().stream().map(s -> {
				return Arrays.asList(s.getClass().getSimpleName() , e.getKey().toString() );
			});

		}).collect(Collectors.toList());

		printer = new TablePrinter(tableHeaders, tableRows);
		
		sb.append(printer.toString());

		sb.append("\nListening on: " + this.ports);

		sb.append("\n");

		log.info(sb.toString());
	}

	public void addDefaultRoutes(RoutingHandler router)
	{

		if (config.hasPath("health.statusPath"))
		{
			try
			{
				final String statusPath = config.getString("health.statusPath");

				router.add(Methods.GET, statusPath, new HttpHandler()
				{

					@Override
					public void handleRequest(HttpServerExchange exchange) throws Exception
					{
						exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, MediaType.TEXT_PLAIN);
						exchange.getResponseSender().send("OK");
					}

				});

				this.registeredEndpoints.add(EndpointInfo.builder().withConsumes("*/*").withProduces("text/plain").withPathTemplate(statusPath).withControllerName("Internal").withMethod(Methods.GET).build());

			} catch (Exception e)
			{
				log.error("Error adding health status route.", e.getMessage());
			}
		}

		if (config.hasPath("application.favicon"))
		{
			try
			{

				final ByteBuffer faviconImageBuffer;
				
				final File faviconFile = new File(config.getString("application.favicon"));

				if (!faviconFile.exists())
				{
					try (final InputStream stream = this.getClass().getResourceAsStream(config.getString("application.favicon")))
					{
						ByteArrayOutputStream baos = new ByteArrayOutputStream();

						byte[] buffer = new byte[4096];
						int read = 0;
						while (read != -1)
						{
							read = stream.read(buffer);
							if (read > 0)
							{
								baos.write(buffer, 0, read);
							}
						}

						faviconImageBuffer = ByteBuffer.wrap(baos.toByteArray());
					}

				}
				else
				{
					try (final InputStream stream = Files.newInputStream(Paths.get(config.getString("application.favicon"))))
					{
						ByteArrayOutputStream baos = new ByteArrayOutputStream();

						byte[] buffer = new byte[4096];
						int read = 0;
						while (read != -1)
						{
							read = stream.read(buffer);
							if (read > 0)
							{
								baos.write(buffer, 0, read);
							}
						}

						faviconImageBuffer = ByteBuffer.wrap(baos.toByteArray());
					}
				}

				if (faviconImageBuffer != null)
				{

					router.add(Methods.GET, "favicon.ico", new HttpHandler()
					{
						@Override
						public void handleRequest(HttpServerExchange exchange) throws Exception
						{
							exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, io.sinistral.proteus.server.MediaType.IMAGE_X_ICON.toString());
							exchange.getResponseSender().send(faviconImageBuffer);
						}

					});

				}

			} catch (Exception e)
			{
				log.error("Error adding favicon route.", e.getMessage());
			}
		}
	}

	/**
	 * @return the router
	 */
	public RoutingHandler getRouter()
	{
		return router;
	}
 

	/**
	 * @return the ports
	 */
	public List<Integer> getPorts()
	{
		return ports;
	}

	 

}
