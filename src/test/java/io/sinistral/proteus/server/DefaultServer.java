/**
 * 
 */
package io.sinistral.proteus.server;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.controllers.Tests;
import io.sinistral.proteus.services.AssetsService;
import io.sinistral.proteus.services.SwaggerService;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * @author jbauer
 */
public class DefaultServer extends BlockJUnit4ClassRunner
{

	private static ProteusApplication app;
	private static boolean first = true;

	/**
	 * @param klass
	 * @throws InitializationError
	 */
	public DefaultServer(Class<?> klass) throws InitializationError
	{
		super(klass);
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
			app = new ProteusApplication();
			app.addService(SwaggerService.class);
			app.addService(AssetsService.class);
			app.addController(Tests.class);

			app.start();

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
