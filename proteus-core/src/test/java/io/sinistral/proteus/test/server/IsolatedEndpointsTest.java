/**
 *
 */
package io.sinistral.proteus.test.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.sinistral.proteus.protocol.MediaType;
import io.sinistral.proteus.test.models.User;
import io.sinistral.proteus.test.models.User.UserType;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.LongStream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/*
 * import static io.restassured.RestAssured.*; import static io.restassured.matcher.RestAssuredMatchers.*; import static org.hamcrest.Matchers.*;
 */

/**
 * @author jbauer
 */
@RunWith(DefaultServer.class)
@Ignore
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class IsolatedEndpointsTest extends AbstractEndpointTest{


    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartByteBuffer()
    {

        try
        {

            Map map = given().multiPart("buffer", file, MediaType.APPLICATION_OCTET_STREAM.contentType())
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/bytebuffer").as(Map.class);

            assertThat(map.size(), equalTo(1));

            assertThat(map.get("size"), equalTo(file.length() + ""));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartJson()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode node = mapper.valueToTree(model);

              InputStream is = given()
                    .multiPart("json", node.toString(), MediaType.JSON.contentType())
                    .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                    .accept(ContentType.JSON)
                    .when().post("v1/tests/multipart/json")
                    .andReturn().asInputStream();

              JsonNode responseNode = mapper.readTree(is);



          assertThat(responseNode.get("id").toString(), containsString("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartFutureJson()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode node = mapper.valueToTree(model);


              InputStream is = given()
                    .multiPart("json", node.toString(), MediaType.JSON.contentType())
                    .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                    .accept(ContentType.JSON)
                    .when().post("v1/tests/multipart/future/json")
                    .andReturn().asInputStream();

              JsonNode responseNode = mapper.readTree(is);

            assertThat(responseNode.get("id").toString(), containsString("101"));

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
            if (file.exists())
            {
                file.delete();
            }

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
