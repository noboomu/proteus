/**
 * 
 */
package io.sinistral.proteus.openapi.test.server;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.LongStream;

import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import io.restassured.http.ContentType;
/**
 * @author jbauer
 */
@RunWith(OpenAPIDefaultServer.class)
public class TestOpenAPIControllerEndpoints
{
 
	private File file = null;


	private List<File> files = new ArrayList<>();

	private Set<Long> idSet = new HashSet<>();



	@Before
	public void setUp()
	{
		try
		{
 			for(int i = 0; i < 4; i++)
			{
				byte[] bytes  = new byte[8388608];
			Random random = new Random();
			random.nextBytes(bytes);

			files.add(Files.createTempFile("test-asset", ".mp4").toFile());

			LongStream.range(1L,10L).forEach( l -> {

				idSet.add(l);
			});
			}

 			file = files.get(0);

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	@Test
	public void testOpenAPIDocs()
	{
		when().get("v1/openapi.yaml").then().statusCode(200);
	}

	@Test
	public void testPojoType()
	{
		when().get("v1/tests/types/pojo").then().statusCode(200).body("id", org.hamcrest.CoreMatchers.equalTo(100),"name", org.hamcrest.CoreMatchers.equalTo("John Doe"));
	}

	@Test
	public void testMoneyType()
	{
		when().get("v1/tests/types/money").then().statusCode(200).body("amount", org.hamcrest.CoreMatchers.equalTo(123.23f),"currency", org.hamcrest.CoreMatchers.equalTo("USD"));
	}


	@Test
	public void testSecurityRequirementEndpoint()
	{
		when().get("v1/tests/secure/resource").then().statusCode(200);
	}

	@Test
	public void testJsonViewEndpoint()
	{
		when().get("v1/tests/response/jsonview").then().statusCode(200);
	}

	@Test
	public void uploadMultipleFileList()
	{

		try
		{


			Map map = given().multiPart("files", files.get(0)).multiPart("files",files.get(1)).multiPart("files",files.get(2)).multiPart("files",files.get(3))
							 .multiPart("names",files.get(0).getName())
							 .multiPart("names",files.get(1).getName())
							 .multiPart("names",files.get(2).getName())
							 .multiPart("names",files.get(3).getName())
							 .accept(ContentType.JSON).when().post("v1/tests/list/file").as(Map.class);


			assertThat(map.size(), org.hamcrest.CoreMatchers.equalTo(4));

			assertThat(map.get(files.get(0).getName()), org.hamcrest.CoreMatchers.equalTo(files.get(0).getTotalSpace()+""));

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void uploadMultiplePathList()
	{

		try
		{


			Map map = given().multiPart("files", files.get(0)).multiPart("files",files.get(1)).multiPart("files",files.get(2)).multiPart("files",files.get(3))
							 .multiPart("names",files.get(0).getName())
							 .multiPart("names",files.get(1).getName())
							 .multiPart("names",files.get(2).getName())
							 .multiPart("names",files.get(3).getName())
							 .accept(ContentType.JSON).when().post("v1/tests/list/file").as(Map.class);


			assertThat(map.size(), org.hamcrest.CoreMatchers.equalTo(4));

			assertThat(map.get(files.get(0).getName()), org.hamcrest.CoreMatchers.equalTo(files.get(0).getTotalSpace()+""));

		} catch (Exception e)
		{
			// TODO Auto-generated catch block

			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void uploadMultipleFileMap()
	{

		try
		{

			Map map = given().multiPart("files", files.get(0)).multiPart("files",files.get(1)).multiPart("files",files.get(2)).multiPart("files",files.get(3))
							 .accept(ContentType.TEXT).when().post("v1/tests/map/file").as(Map.class);


			assertThat(map.size(), org.hamcrest.CoreMatchers.equalTo(4));

			assertThat(map.get(files.get(0).getName()), org.hamcrest.CoreMatchers.equalTo(files.get(0).getTotalSpace()+""));

		} catch (Exception e)
		{
			// TODO Auto-generated catch block

			e.printStackTrace();
			fail(e.getMessage());
		}

	}

	@Test
	public void uploadMultiplePathMap()
	{

		try
		{


			Map map = given().multiPart("files", files.get(0)).multiPart("files",files.get(1)).multiPart("files",files.get(2)).multiPart("files",files.get(3))

							 .accept(ContentType.JSON).when().post("v1/tests/map/file").as(Map.class);


			assertThat(map.size(), org.hamcrest.CoreMatchers.equalTo(4));

			assertThat(map.get(files.get(0).getName()), org.hamcrest.CoreMatchers.equalTo(files.get(0).getTotalSpace()+""));

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}

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
