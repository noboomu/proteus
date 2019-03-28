/**
 * 
 */
package io.sinistral.proteus.swagger.test.server;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.LongStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * import static io.restassured.RestAssured.*; import static io.restassured.matcher.RestAssuredMatchers.*; import static org.hamcrest.Matchers.*;
 */
/**
 * @author jbauer
 */
@RunWith(DefaultServer.class)
public class TestControllerEndpoints
{
 
	private File file = null;
	
	private Set<Long> idSet = new HashSet<>();

	@Before
	public void setUp()
	{
		try
		{
 			byte[] bytes  = new byte[8388608];
			Random random = new Random(); 
			random.nextBytes(bytes);

			file = Files.createTempFile("test-asset", ".mp4").toFile();
			
			LongStream.range(1L,10L).forEach( l -> {
				
				idSet.add(l);
			});
			 

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Test
	public void testOpenAPIDocs()
	{
		when().get("openapi.yaml").then().statusCode(200);
	}


	@Test
	public void testSecurityRequirementEndpoint()
	{
		when().get("tests/secure/resource").then().statusCode(200);
	}

	
	@After
	public void tearDown()
	{
		try
		{
 			if(file.exists())
 			{
 				file.delete();
 			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
