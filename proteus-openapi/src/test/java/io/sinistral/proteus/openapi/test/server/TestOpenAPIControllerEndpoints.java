/**
 *
 */
package io.sinistral.proteus.openapi.test.server;

import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.when;

/**
 * @author jbauer
 */
@RunWith(OpenAPIDefaultServer.class)
public class TestOpenAPIControllerEndpoints {





    @Test
    public void testYamlSpec()
    {

        when().get("v1/openapi.yaml").then().statusCode(200).header("content-type", "text/yaml");
    }

    @Test
    public void testJsonSpec()
    {

        when().get("v1/openapi.json").then().statusCode(200).header("content-type", "application/json");
    }

    @Test
    public void testDocumentation()
    {

        when().get("v1/openapi").then().statusCode(200).header("content-type", "text/html");
    }


    @Test
    public void testRedoc()
    {

        when().get("v1/openapi/redoc").then().statusCode(200).header("content-type", "text/html");
    }

    @Test
    public void testElements()
    {

        when().get("v1/openapi/elements").then().statusCode(200).header("content-type", "text/html");
    }

    @Test
    public void testRapidoc()
    {

        when().get("v1/openapi/rapidoc").then().statusCode(200).header("content-type", "text/html");
    }

}
