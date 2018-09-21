/**
 * 
 */
package io.sinistral.proteus.test.server;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
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
		given().accept(ContentType.JSON).log().uri().when().get("swagger.json").then().statusCode(200).and().body("basePath", is("/v1"));
	}

	@Test
	public void exchangeUserJson()
	{
		User user = given().accept(ContentType.JSON).log().uri().when().get("tests/exchange/user/json").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}
	
	@Test
	public void genericSet()
	{
		given().accept(ContentType.JSON).log().uri().when().queryParam("ids", idSet).get("tests/generic/set").then().statusCode(200).body(containsString("1"));
	}
	
	@Test
	public void optionalGenericSet()
	{
		given().accept(ContentType.JSON).log().uri().when().queryParam("ids",idSet).get("tests/optional/set").then().statusCode(200).body(containsString("1"));
	}

	@Test
	public void exchangeUserXml()
	{
		User user = given().accept(ContentType.XML).log().uri().when().get("tests/exchange/user/xml").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}

	@Test
	public void responseUserJson()
	{
		User user = given().accept(ContentType.JSON).log().uri().when().get("tests/response/user/json").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}

	@Test
	public void responseUserXml()
	{
		User user = given().accept(ContentType.XML).log().uri().when().get("tests/response/user/xml").as(User.class);
		assertThat(user.getId(), CoreMatchers.is(123L));
	}

	@Test
	public void exchangePlaintext()
	{
		given().accept(ContentType.TEXT).log().uri().when().get("tests/exchange/plaintext").then().statusCode(200).and().body(containsString("Hello, World!"));
	}
	
	@Test
	public void exchangePlaintext2()
	{
		given().accept(ContentType.TEXT).log().uri().when().get("tests/exchange/plaintext2").then().statusCode(200).and().body(containsString("Hello, World!"));
	}

	@Test
	public void responsePlaintext()
	{
		given().accept(ContentType.TEXT).log().uri().when().get("tests/response/plaintext").then().statusCode(200).and().body(containsString("Hello, World!"));
	}
	
	@Test
	public void responseEchoModel()
	{
		User model = new User(101L,UserType.ADMIN);
		  
		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(model).log().uri().when().post("tests/response/json/echo").then().statusCode(200).and().body(containsString("101"));

	}
	
	@Test
	public void responseInnerClassModel()
	{
		User.InnerUserModel model = new User.InnerUserModel();
		model.id = 101L;
		  
		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(model).log().uri().when().post("tests/response/json/innerClass").then().statusCode(200).and().body(containsString("101"));

	}
	 

	@Test
	public void responseFutureUser()
	{
		given().accept(ContentType.JSON).log().uri().when().get("tests/response/future/user").then().statusCode(200).and().body(containsString("123"));

	}

	@Test
	public void responseMap()
	{
		given().accept(ContentType.JSON).log().uri().when().get("tests/response/future/map").then().statusCode(200).and().body("message", is("success"));
	}
	
	@Test
	public void testRedirect()
	{
		given().log().uri().when().get("tests/redirect").then().statusCode(200).and().header("Server", "gws");
	}
	
	@Test
	public void testRedirectFoundCode()
	{
		given().log().uri().when().redirects().follow(false).get("tests/redirect").then().statusCode(302);
	}
	
	@Test
	public void testRedirectMovedPermanentlyCode()
	{
		given().log().uri().when().redirects().follow(false).get("tests/redirect/permanent").then().statusCode(301);
	}

	@SuppressWarnings("resource")
	@Test
	public void responseUploadFilePathParameter()
	{

		try
		{

			final InputStream is = given().log().uri().multiPart("file", file).accept(ContentType.ANY).when().post("tests/response/file/path").asInputStream();

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
	public void responseParseListParameter()
	{

		try
		{
			
			List<Long> values = new Random().longs(10, 0L, 20L).boxed().collect(Collectors.toList());
			
			  
			
			given().contentType(ContentType.JSON).accept(ContentType.JSON).body(values).log().all().when().post("tests/response/parse/ids").then().statusCode(200);


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

			final InputStream is = given().multiPart("file", file).log().uri().accept(ContentType.ANY).when().post("tests/response/file/bytebuffer").asInputStream();

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
	public void responseComplexParameters()
	{

		UUID randomUUID = UUID.randomUUID();
		Long longValue = 123456789L;
		List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
		String stringValue = "TESTSTRING123!#$";

		Map<String, Object> map = given()

				.log().uri()
				
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
