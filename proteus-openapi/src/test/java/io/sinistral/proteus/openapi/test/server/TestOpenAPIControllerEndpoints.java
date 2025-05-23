/**
 *
 */
package io.sinistral.proteus.openapi.test.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author jbauer
 */
@ExtendWith(OpenAPIDefaultServer.class)
public class TestOpenAPIControllerEndpoints {

    @Test
    public void testYamlSpec() {

        when().get("v1/openapi.yaml").then().statusCode(200).header("content-type", "application/yaml");
    }

        @Test
    public void testYmlSpec() {

        when().get("v1/openapi.yaml").then().statusCode(200).header("content-type", "application/yaml");
    }


    @Test
    public void testJsonSpec() {

        when().get("v1/openapi.json").then().statusCode(200).header("content-type", "application/json");
    }

    @Test
    public void testDocumentation() {

        when().get("v1/openapi").then().statusCode(200).header("content-type", "text/html");
    }

    @Test
    public void testBearer() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String response = given().header("Authorization", "Bearer 123456").get("v1/tests/bearer").andReturn().asString();

        assertNotNull(response);

        Map<String, Object> map = mapper.readValue(response, new TypeReference<>() {
        });

        assertEquals("123456", map.get("token"));
        assertEquals(true, map.get("result"));

    }

}
