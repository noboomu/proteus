/**
 *
 */
package io.sinistral.proteus.test.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.sinistral.proteus.protocol.MediaType;
import io.sinistral.proteus.test.models.User;
import io.sinistral.proteus.test.models.User.UserType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.InputStream;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/*
 * import static io.restassured.RestAssured.*; import static io.restassured.matcher.RestAssuredMatchers.*; import static org.hamcrest.Matchers.*;
 */

/**
 * @author jbauer
 */
@ExtendWith(DefaultServer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IsolatedEndpointsTest extends AbstractEndpointTest {


    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartByteBuffer() {

        Map map = given().multiPart("buffer", file, MediaType.APPLICATION_OCTET_STREAM.contentType())
                .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                .accept(ContentType.JSON).when().post("v1/tests/multipart/bytebuffer").as(Map.class);

        assertEquals(1, map.size());

        assertEquals(map.get("size"), (int)file.length() );


    }

    @Test
    public void uploadMultipartJson()  throws Exception {

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


            assertTrue(responseNode.get("id").toString().contains("101"));



    }

    @Test
    public void uploadMultipartFutureJson() {

        try {

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

            assertTrue(responseNode.get("id").toString().contains("101"));

        } catch (Exception e) {
            // TODO Auto-generated catch block
            
            fail(e.getMessage());
        }

    }

    @AfterAll
    public void tearDown() {

        try {
            if (file.exists()) {
                file.delete();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            
            fail(e.getMessage());
        }
    }

}
