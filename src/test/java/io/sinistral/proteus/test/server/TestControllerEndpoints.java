/**
 * 
 */
package io.sinistral.proteus.test.server;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.restassured.http.ContentType;
import io.sinistral.proteus.test.models.User;
import io.sinistral.proteus.test.models.User.UserType;
 
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
	public void testSwaggerDocs()
	{
		given().accept(ContentType.JSON).when().get("swagger.json").then().statusCode(200).and().body("basePath", is("/v1"));
	}
	
	@Test
	public void testOpenAPIDocs()
	{
		when().get("openapi.yaml").then().statusCode(200);
	}
	
	@Test
	public void testDebugEndpoint()
	{
		given().accept(ContentType.JSON).when().get("tests/response/debug").then().statusCode(200);
	}

	@Test
	public void testDebugBlockingEndpoint()
	{
		given().accept(ContentType.JSON).when().get("tests/response/debug/blocking").then().statusCode(200);
	}

	
	@Test
	public void exchangeUserJson()
	{
		User user = given().accept(ContentType.JSON).when().get("tests/exchange/user/json").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}
	
	@Test
	public void genericSet()
	{
		given().accept(ContentType.JSON).when().queryParam("ids", idSet).get("tests/generic/set").then().statusCode(200).body(containsString("1"));
	}
	
	@Test
	public void optionalGenericSet()
	{
		given().accept(ContentType.JSON).when().queryParam("ids",idSet).get("tests/optional/set").then().statusCode(200).body(containsString("1"));
	}

	@Test
	public void exchangeUserXml()
	{
		User user = given().accept(ContentType.XML).when().get("tests/exchange/user/xml").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}

	@Test
	public void responseUserJson()
	{
		User user = given().accept(ContentType.JSON).when().get("tests/response/user/json").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}

	@Test
	public void responseUserXml()
	{
		User user = given().accept(ContentType.XML).when().get("tests/response/user/xml").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}

	@Test
	public void exchangePlaintext()
	{
		given().accept(ContentType.TEXT).when().get("tests/exchange/plaintext").then().statusCode(200).and().body(containsString("Hello, World!"));
	}
	
	@Test
	public void exchangePlaintext2()
	{
		given().accept(ContentType.TEXT).when().get("tests/exchange/plaintext2").then().statusCode(200).and().body(containsString("Hello, World!"));
	}

	@Test
	public void responsePlaintext()
	{
		given().accept(ContentType.TEXT).when().get("tests/response/plaintext").then().statusCode(200).and().body(containsString("Hello, World!"));
	}
	
	@Test
	public void responseEchoModel()
	{
		User model = new User(101L,UserType.ADMIN);
		  
		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(model).when().post("tests/response/json/echo").then().statusCode(200).and().body(containsString("101"));

	}
	
	@Test
	public void responseBeanParam()
	{
		User model = new User();
		model.setId(101L);
		  
		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(model).when().post("tests/response/json/beanparam").then().statusCode(200).and().body(containsString("101"));

	}
	 

	@Test
	public void responseFutureUser()
	{
		given().accept(ContentType.JSON).when().get("tests/response/future/user").then().statusCode(200).and().body(containsString("123"));

	}

	@Test
	public void responseMap()
	{
		given().accept(ContentType.JSON).when().get("tests/response/map").then().statusCode(200).and().body("message", is("success"));
	}
	

	@Test
	public void responseFutureMap()
	{
		given().accept(ContentType.JSON).when().get("tests/response/future/map").then().statusCode(200).and().body("message", is("success"));
	}
	
	@Test
	public void testRedirect()
	{
		given().when().get("tests/redirect").then().statusCode(200).and().header("Server", "gws");
	}
	
	@Test
	public void testRedirectFoundCode()
	{
		given().when().redirects().follow(false).get("tests/redirect").then().statusCode(302);
	}
	
	@Test
	public void testRedirectMovedPermanentlyCode()
	{
		given().when().redirects().follow(false).get("tests/redirect/permanent").then().statusCode(301);
	}

 	@Test
	public void responseUploadFilePathParameter()
	{

		try
		{

			final InputStream is = given().multiPart("file", file).accept(ContentType.ANY).when().post("tests/response/file/path").asInputStream();

			try(final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
			{
				IOUtils.copy(is, byteArrayOutputStream);
			  
				assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));
			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
 	@Test
	public void responseUploadOptionalFilePathParameter()
	{

		try
		{

			final InputStream is = given().multiPart("file", file).accept(ContentType.ANY).when().post("tests/response/file/path/optional").asInputStream();

			try(final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
			{
				IOUtils.copy(is, byteArrayOutputStream); 

				assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));
			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void responseParseListParameter()
	{
		try
		{
			List<Long> values = new Random().longs(10, 0L, 20L).boxed().collect(Collectors.toList());
			 
			given().contentType(ContentType.JSON).accept(ContentType.JSON).body(values).when().post("tests/response/parse/ids").then().statusCode(200);


		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("resource")
	@Test
	public void responseUploadByteBufferParameter()
	{

		try
		{

			final InputStream is = given().multiPart("file", file).accept(ContentType.ANY).when().post("tests/response/bytebuffer").asInputStream();

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			IOUtils.copy(is, byteArrayOutputStream);
			IOUtils.closeQuietly(byteArrayOutputStream);
			IOUtils.closeQuietly(is);

			assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("resource")
	@Test
	public void responseUploadFileParameter()
	{

		try
		{

			final InputStream is = given().multiPart("file", file).accept(ContentType.ANY).when().post("tests/response/file").asInputStream();

			final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			IOUtils.copy(is, byteArrayOutputStream);
			IOUtils.closeQuietly(byteArrayOutputStream);
			IOUtils.closeQuietly(is);

			assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void responseParseInstant()
	{

	 
		Instant instant = Instant.now();

		 given()

				
				 
				.queryParam("instant", instant.toString())

				.when()

				.get("tests/response/parse/instant").
				
				then().statusCode(200).and().body(containsString(instant.toString()));

		
 	}
	
	@Test
	public void responseParseTimestamp()
	{

	 
		Timestamp ts = new Timestamp(System.currentTimeMillis());

			given()
  
				.queryParam("timestamp", ts.toString()) 
				.when()

				.get("tests/response/parse/timestamp").
				then().statusCode(200).and().body(containsString(ts.toString()));

		 
	}
	
	@Test
	public void responseComplexParameters()
	{

		UUID randomUUID = UUID.randomUUID();
		Long longValue = 123456789L;
		List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		String stringValue = "TESTSTRING123!#$";

		Map<String, Object> map = given()

				
				
				.accept(ContentType.JSON)

				.contentType("application/json")

				.queryParam("queryUUID", randomUUID)

				.queryParam("optionalQueryUUID", randomUUID)

				.queryParam("queryLong", longValue)

				.queryParam("optionalQueryLong", longValue)

				.queryParam("optionalQueryDate", "1970-01-01T00:00:00.000+00:00")

				.queryParam("queryEnum", User.UserType.ADMIN)

				.queryParam("optionalQueryEnum", User.UserType.ADMIN)

				.queryParam("queryIntegerList", integerList)

				.queryParam("optionalQueryString", stringValue)

				.header("headerString", stringValue)

				.header("optionalHeaderString", stringValue)

				.header("optionalHeaderUUID", randomUUID)

				.when()

				.get("tests/response/parameters/complex/" + longValue.toString())

				.as(Map.class);

		assertThat((map.get("queryUUID").toString()), CoreMatchers.is(randomUUID.toString()));

		assertThat((map.get("optionalQueryUUID").toString()), CoreMatchers.is(randomUUID.toString()));

		assertThat((map.get("optionalHeaderUUID").toString()), CoreMatchers.is(randomUUID.toString()));

		assertThat((map.get("pathLong").toString()), CoreMatchers.is(longValue.toString()));

		assertThat((map.get("optionalQueryLong").toString()), CoreMatchers.is(longValue.toString()));

		assertThat((map.get("optionalQueryEnum").toString()), CoreMatchers.is(User.UserType.ADMIN.name()));

		assertThat((map.get("queryEnum").toString()), CoreMatchers.is(User.UserType.ADMIN.name()));

		assertThat((map.get("headerString").toString()), CoreMatchers.is(stringValue));

		assertThat((map.get("optionalHeaderString").toString()), CoreMatchers.is(stringValue));

		assertThat((map.get("optionalQueryString").toString()), CoreMatchers.is(stringValue));

		assertThat((map.get("optionalQueryDate").toString()), containsString("1970-01-01"));

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
