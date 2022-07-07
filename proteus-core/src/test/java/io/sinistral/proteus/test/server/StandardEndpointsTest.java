/**
 *
 */
package io.sinistral.proteus.test.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.sinistral.proteus.protocol.MediaType;
import io.sinistral.proteus.test.controllers.GenericBean;
import io.sinistral.proteus.test.models.User;
import io.sinistral.proteus.test.models.User.UserType;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/*
 * import static io.restassured.RestAssured.*; import static io.restassured.matcher.RestAssuredMatchers.*; import static org.hamcrest.Matchers.*;
 */

/**
 * @author jbauer
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunWith(DefaultServer.class)
public class StandardEndpointsTest extends AbstractEndpointTest {
@Test
    public void testDebugEndpoint()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/debug").then().statusCode(200).body(containsString("testValue"));
    }

    @Test
    public void testDebugBlockingEndpoint()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/debug/blocking").then().statusCode(200);
    }

    @Test
    public void exchangeUserJson()
    {

        User user = given().accept(ContentType.JSON).when().get("v1/tests/exchange/user/json").as(User.class);
        assertThat(user.getId(), CoreMatchers.is(123L));
    }

    @Test
    public void genericSet()
    {

        given().accept(ContentType.JSON).when().queryParam("ids", idSet).get("v1/tests/generic/set").then().statusCode(200).body(containsString("1"));
    }

    @Test
    public void genericBeanSet()
    {

        Set<Long> randomLongs = new HashSet<>();

        Random random = new Random();

        Long firstNumber = null;

        for (int i = 0; i < 10; i++)
        {
            Long v = random.nextLong();

            randomLongs.add(v);

            if (firstNumber == null)
            {
                firstNumber = v;
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        try
        {

            String body = mapper.writeValueAsString(randomLongs);

            given().contentType(ContentType.JSON).accept(ContentType.JSON).body(body).post("v1/tests/generic/set/bean").then().statusCode(200).body(containsString(firstNumber.toString()));

        } catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

     @Test
    public void genericBean()
    {

        GenericBean<Long> genericBean = new GenericBean<>();

        Long value = 1234234L;

        genericBean.setValue(value);

        ObjectMapper mapper = new ObjectMapper();

        try
        {

            String body = mapper.writeValueAsString(genericBean);

            given().contentType(ContentType.JSON).accept(ContentType.JSON).body(body).post("v1/tests/generic/bean").then().statusCode(200).body(containsString(genericBean.getValue().toString()));

        } catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void genericBeanList()
    {

        List<Long> randomLongs = new ArrayList<>();

        Random random = new Random();

        Long firstNumber = null;

        for (int i = 0; i < 10; i++)
        {
            Long v = random.nextLong();

            randomLongs.add(v);

            if (firstNumber == null)
            {
                firstNumber = v;
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        try
        {

            String body = mapper.writeValueAsString(randomLongs);

            given().contentType(ContentType.JSON).accept(ContentType.JSON).body(body).post("v1/tests/generic/list/bean").then().statusCode(200).body(containsString(firstNumber.toString()));

        } catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void genericBeanMap()
    {

        Map<String, Long> randomLongs = new java.util.HashMap<>();

        Random random = new Random();

        Long firstNumber = null;

        for (int i = 0; i < 10; i++)
        {
            Long v = random.nextLong();

            randomLongs.put(v.toString(), v);

            if (firstNumber == null)
            {
                firstNumber = v;
            }
        }

        ObjectMapper mapper = new ObjectMapper();

        try
        {

            String body = mapper.writeValueAsString(randomLongs);

            given().contentType(ContentType.JSON).accept(ContentType.JSON).body(body).post("v1/tests/generic/map/bean").then().statusCode(200).body(containsString(firstNumber.toString()));

        } catch (Exception e)
        {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void optionalGenericSet()
    {

        given().accept(ContentType.JSON).when().queryParam("ids", idSet).get("v1/tests/optional/set").then().statusCode(200).body(containsString("1"));
    }

    @Test
    public void exchangeUserXml()
    {

        User user = given().accept(ContentType.XML).when().get("v1/tests/exchange/user/xml").as(User.class);
        assertThat(user.getId(), CoreMatchers.is(123L));
    }

    @Test
    public void responseUserJson()
    {

        User user = given().accept(ContentType.JSON).when().get("v1/tests/response/user/json").as(User.class);
        assertThat(user.getId(), CoreMatchers.is(123L));
    }

    @Test
    public void responseWorkerFuture()
    {

        Map response = given().accept(ContentType.JSON).when().get("v1/tests/response/future/worker").as(Map.class);
        assertThat(response.get("status").toString(), CoreMatchers.is("OK"));
    }

    @Test
    public void responseWorkerFutureBlocking()
    {

        Map response = given().accept(ContentType.JSON).when().get("v1/tests/response/future/worker/blocking").as(Map.class);
        assertThat(response.get("status").toString(), CoreMatchers.is("OK"));
    }

    @Test
    public void healthCheck()
    {

        given().accept(ContentType.TEXT).when().get("health").then().statusCode(200).and().body(containsString("OK"));
        ;
    }

    @Test
    public void responseUserXml()
    {

        User user = given().accept(ContentType.XML).when().get("v1/tests/response/user/xml").as(User.class);
        assertThat(user.getId(), CoreMatchers.is(123L));
    }

    @Test
    public void badRequest()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/response/badrequest").then().statusCode(400);
    }

    @Test
    public void badRequestBlocking()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/response/badrequest/blocking").then().statusCode(400);
    }

    @Test
    public void badRequestFuture()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/future/badrequest").then().statusCode(400);
    }

    @Test
    public void notFoundFuture()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/future/notfound/blocking").then().statusCode(404);
    }

    @Test
    public void badRequestFutureBlocking()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/future/badrequest/blocking").then().statusCode(400);
    }

    @Test
    public void exchangePlaintext()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/exchange/plaintext").then().statusCode(200).and().body(containsString("Hello, World!"));
    }

    @Test
    public void exchangePlaintext2()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/exchange/plaintext2").then().statusCode(200).and().body(containsString("Hello, World!"));
    }

    @Test
    public void responsePlaintext()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/response/plaintext").then().statusCode(200).and().body(containsString("Hello, World!"));
    }

    @Test
    public void responseEchoModel()
    {

        User model = new User(101L, UserType.ADMIN);

        given().contentType(ContentType.JSON).accept(ContentType.JSON).multiPart("user", model).contentType(MediaType.MULTIPART_FORM_DATA.contentType()).when().post("v1/tests/response/json/echo").then().statusCode(200).and().body(containsString("101"));

    }

    @Test
    public void responseBeanParam()
    {

        User model = new User();
        model.setId(101L);

        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(model).when().post("v1/tests/response/json/beanparam").then().statusCode(200).and().body(containsString("101"));

    }

    @Test
    public void responseOptionalBeanParam()
    {

        User model = new User();
        model.setId(101L);

        given().contentType(ContentType.JSON).accept(ContentType.JSON).body(model).when().post("v1/tests/response/json/beanparam-optional").then().statusCode(200).and().body(containsString("101"));

    }


    @Test
    public void responseFutureUser()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/future/user").then().statusCode(200).and().body(containsString("123"));

    }

    @Test
    public void responseMap()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/map").then().statusCode(200).and().body("message", is("success"));
    }

    @Test
    public void responseFutureMap()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/future/map").then().statusCode(200).and().body("message", is("success"));
    }

    @Test
    public void responseFutureResponseMap()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/future/response").then().statusCode(200).and().body("message", is("success"));
    }

    @Test
    public void testRedirect()
    {

        given().when().redirects().follow(false).get("v1/tests/redirect").then().statusCode(302).and().header("Server", "proteus").and().header("Location","v1/response/debug/blocking");
    }

    @Test
    public void testRedirectFoundCode()
    {

        given().when().redirects().follow(false).get("v1/tests/redirect").then().statusCode(302).and().header("Location","v1/response/debug/blocking");
    }

    @Test
    public void testRedirectMovedPermanentlyCode()
    {

        given().when().redirects().follow(false).get("v1/tests/redirect/permanent").then().statusCode(301).and().header("Location","v1/response/debug/blocking");
    }

    @Test
    public void pathParam()
    {

        given().accept(ContentType.TEXT).when().get("v1/tests/response/params/path/foobar").then().statusCode(200).and().body(containsString("foobar"));

    }

//	@Test
//	public void regexPathParam()
//	{
//		given().accept(ContentType.TEXT).when().get("v1/tests/response/params/regexpath/fooBar").then().statusCode(200).and().body(containsString("fooBar"));
//
//	}
//
//	@Test
//	public void invalidRegexPathParam()
//	{
//		given().accept(ContentType.TEXT).when().get("v1/tests/response/params/regexpath/fooBar101").then().statusCode(400);
//
//	}


    @Test
    public void responseParseListParameter()
    {

        try
        {
            List<Long> values = new Random().longs(10, 0L, 20L).boxed().collect(Collectors.toList());

            given().contentType(ContentType.JSON).accept(ContentType.JSON).body(values).when().post("v1/tests/response/parse/ids").then().statusCode(200);

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }


    @Test
    public void responseParseInstant()
    {

        Instant instant = Instant.now();

        given()

                .queryParam("instant", instant.toString())

                .when()

                .get("v1/tests/response/parse/instant").

                        then().statusCode(200).and().body(containsString(instant.toString()));

    }

    @Test
    public void responseParseTimestamp()
    {

        Timestamp ts = new Timestamp(System.currentTimeMillis());

        given()

                .queryParam("timestamp", ts.toString())
                .when()

                .get("v1/tests/response/parse/timestamp").
                        then().statusCode(200).and().body(containsString(ts.toString()));

    }

    @Test
    public void responseParseBigDecimal()
    {

        BigDecimal value = new BigDecimal(23234.34);

        given()

                .queryParam("value", value.toString())

                .when()

                .get("v1/tests/response/parse/big-decimal").

                        then().statusCode(200).and().body(containsString(value.toString()));
    }

    @Test
    public void responseParseDouble()
    {

        Double value = 23234.34;

        given()

                .queryParam("value", Double.toString(value))

                .when()

                .get("v1/tests/response/parse/double").

                        then().statusCode(200).and().body(containsString(value.toString()));
    }

    @Test
    public void notFound()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/error/404").then().statusCode(404).log().body().content(containsString("No entity found"));

    }

    @Test
    public void unauthorized()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/response/error/401").then().statusCode(401).log().body().content(containsString("Unauthorized"));

    }

    @Test
    public void maxValueError()
    {

        given().queryParam("param", 105).when().get("v1/tests/response/max").then().statusCode(400).log();

    }

    @Test
    public void minValueError()
    {

        given().queryParam("param", 5).when().get("v1/tests/response/min").then().statusCode(400).log();

    }

    @Test
    public void lastModified()
    {

        given().accept(ContentType.JSON).when().get("v1/tests/headers/last-modified").then().statusCode(200).body(containsString("value")).header("last-modified", CoreMatchers.anything());
    }

    @Test
    public void maxValue()
    {

        given().queryParam("param", 50).when().get("v1/tests/response/max").then().statusCode(200).log();

    }

    @Test
    public void minValue()
    {

        given().queryParam("param", 15).when().get("v1/tests/response/min").then().statusCode(200).log();

    }

    @Test
    public void responseComplexParameters()
    {

        UUID randomUUID = UUID.randomUUID();
        Long longValue = 123456789L;
        List<Integer> integerList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        String stringValue = "v1/testsTRING123!#$";

        Map map = given()

                .accept(ContentType.JSON)

                .contentType("application/json")

                .queryParam("queryUUID", randomUUID)

                .queryParam("optionalQueryUUID", randomUUID)

                .queryParam("queryLong", longValue)

                .queryParam("optionalQueryLong", longValue)

                .queryParam("optionalQueryDate", "1970-01-01T00:00:00.000+00:00")

                .queryParam("queryEnum", UserType.ADMIN)

                .queryParam("optionalQueryEnum", UserType.ADMIN)

                .queryParam("queryIntegerList", integerList)

                .queryParam("optionalQueryString", stringValue)

                .header("headerString", stringValue)

                .header("optionalHeaderString", stringValue)

                .header("optionalHeaderUUID", randomUUID)

                .when()

                .get("v1/tests/response/parameters/complex/" + longValue.toString())

                .as(Map.class);

        assertThat((map.get("queryUUID").toString()), CoreMatchers.is(randomUUID.toString()));

        assertThat((map.get("optionalQueryUUID").toString()), CoreMatchers.is(randomUUID.toString()));

        assertThat((map.get("optionalHeaderUUID").toString()), CoreMatchers.is(randomUUID.toString()));

        assertThat((map.get("pathLong").toString()), CoreMatchers.is(longValue.toString()));

        assertThat((map.get("optionalQueryLong").toString()), CoreMatchers.is(longValue.toString()));

        assertThat((map.get("optionalQueryEnum").toString()), CoreMatchers.is(UserType.ADMIN.name()));

        assertThat((map.get("queryEnum").toString()), CoreMatchers.is(UserType.ADMIN.name()));

        assertThat((map.get("headerString").toString()), CoreMatchers.is(stringValue));

        assertThat((map.get("optionalHeaderString").toString()), CoreMatchers.is(stringValue));

        assertThat((map.get("optionalQueryString").toString()), CoreMatchers.is(stringValue));

        assertThat((map.get("optionalQueryDate").toString()), containsString("1970-01-01"));

    }
}
