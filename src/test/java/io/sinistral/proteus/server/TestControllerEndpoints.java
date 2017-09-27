/**
 * 
 */
package io.sinistral.proteus.server;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.sinistral.proteus.models.User;
import io.sinistral.proteus.models.User.UserType;
 
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

	@Before
	public void setUp()
	{
		try
		{
	 
			file = new File(getClass().getClassLoader().getResource("data/video.mp4").toURI());

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
	public void responseEchoUser()
	{
		User user = new User(101L,UserType.ADMIN);
		  
		given().contentType(ContentType.JSON).accept(ContentType.JSON).body(user).log().uri().when().post("tests/response/json/echo").then().statusCode(200).and().body(containsString("101"));

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

}
