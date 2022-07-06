/**
 * 
 */
package io.sinistral.proteus.test.server;

import java.util.List;

import io.restassured.parsing.Parser;
import io.sinistral.proteus.test.controllers.Tests;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;
import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.services.AssetsService;

/**
 * @author jbauer
 */
public class DefaultServer extends BlockJUnit4ClassRunner
{
	private static Logger log = LoggerFactory.getLogger(DefaultServer.class.getCanonicalName());

	static {
RestAssured.defaultParser = Parser.JSON;
		        System.setProperty("logback.configurationFile", "./conf/logback-test.xml");

	}
	private static boolean first = true;

	/**
	 * @param clazz
	 * @throws InitializationError
	 */
	public DefaultServer(Class<?> clazz) throws InitializationError
	{
		super(clazz); 
	}

	@Override
	public void run(final RunNotifier notifier)
	{
		notifier.addListener(new RunListener()
		{
			@Override
			public void testStarted(Description description) throws Exception
			{

				super.testStarted(description);
			}

			@Override
			public void testFinished(Description description) throws Exception
			{

				super.testFinished(description);
			}
		});

		runInternal(notifier);

		super.run(notifier);
	}

	private static void runInternal(final RunNotifier notifier)
	{
	 
		if (first)
		{  
			
			first = false;
			
			final ProteusApplication app = new ProteusApplication();

			app.addService(AssetsService.class);

			app.addController(Tests.class);

			app.start();
			
			int port = 0;
			 
			try
			{
				Thread.sleep(5000);
				
				
				
				List<Integer> ports = app.getPorts();
				
				port = ports.get(0);
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		 
			 
 			
			RestAssured.baseURI = String.format("http://localhost:%d/",port);
			
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			
			while (!app.isRunning())
			{
				try
				{
					Thread.sleep(100L);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			notifier.addListener(new RunListener()
			{
				@Override
				public void testRunFinished(final Result result) throws Exception
				{
					app.shutdown();
				};
			});
		}

	}

}
