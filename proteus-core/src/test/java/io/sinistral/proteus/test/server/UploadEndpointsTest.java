/**
 *
 */
package io.sinistral.proteus.test.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.sinistral.proteus.protocol.MediaType;
import io.sinistral.proteus.test.models.User;
import io.sinistral.proteus.test.models.User.UserType;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
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
import static org.junit.Assert.*;

/*
 * import static io.restassured.RestAssured.*; import static io.restassured.matcher.RestAssuredMatchers.*; import static org.hamcrest.Matchers.*;
 */

/**
 * @author jbauer
 */
@RunWith(DefaultServer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class UploadEndpointsTest extends AbstractEndpointTest{

    private static final Logger log = LoggerFactory.getLogger(UploadEndpointsTest.class.getName());


    @Test
    public void testDebugEndpoint()
    {

        given().accept(ContentType.JSON).when().log().all().get("v1/tests/response/debug").then().statusCode(200).body(containsString("testValue"));
    }

    @Test
    public void responseUploadFilePathParameter()
    {

        try
        {

            log.info("file: {}",file);

            final InputStream is = given().log().all()
                                          .multiPart("file", file,MediaType.AUDIO_MP4.contentType())
                                          .accept(ContentType.JSON).when().post("v1/tests/response/file/path")
                    .then().extract().asInputStream();

            try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
            {
                IOUtils.copy(is, byteArrayOutputStream);

                assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));
            }

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void uploadMultipleFileList()
    {

        try
        {

            Map map = given().multiPart("files", files.get(0)).multiPart("files", files.get(1)).multiPart("files", files.get(2)).multiPart("files", files.get(3))
                             .multiPart("names", files.get(0).getName())
                             .multiPart("names", files.get(1).getName())
                             .multiPart("names", files.get(2).getName())
                             .multiPart("names", files.get(3).getName())
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/list/file").as(Map.class);

            assertThat(map.size(), equalTo(4));

            assertThat(map.get(files.get(0).getName()), equalTo(files.get(0).getTotalSpace() + ""));

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

            Map map = given().multiPart("files", files.get(0)).multiPart("files", files.get(1)).multiPart("files", files.get(2)).multiPart("files", files.get(3))
                             .multiPart("names", files.get(0).getName())
                             .multiPart("names", files.get(1).getName())
                             .multiPart("names", files.get(2).getName())
                             .multiPart("names", files.get(3).getName())
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/list/file").as(Map.class);

            assertThat(map.size(), equalTo(4));

            assertThat(map.get(files.get(0).getName()), equalTo(files.get(0).getTotalSpace() + ""));

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

            

            Response mapResponse = given().multiPart("files", files.get(0)).multiPart("files", files.get(1)).multiPart("files", files.get(2)).multiPart("files", files.get(3))
                                  .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                                  .accept(ContentType.JSON).when().post("v1/tests/map/file");

//            

            ObjectMapper mapper = new ObjectMapper();

            JsonNode node = mapper.readTree(mapResponse.asByteArray());

            assertThat(node.size(), equalTo(4));

            assertEquals(node.get(files.get(0).getName()).asText(),  files.get(0).getTotalSpace() + "");

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void responseUploadByteBufferParameter()
    {

        try
        {

            final InputStream is = given().multiPart("file", file).accept(ContentType.ANY).contentType(MediaType.MULTIPART_FORM_DATA.contentType()).when().post("v1/tests/response/bytebuffer").asInputStream();

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(is, byteArrayOutputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);
            IOUtils.closeQuietly(is);

            assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void responseUploadFileParameter()
    {

        try
        {

            final InputStream is = given().multiPart("file", file).accept(ContentType.ANY).contentType(MediaType.MULTIPART_FORM_DATA.contentType()).when().post("v1/tests/response/file").asInputStream();

            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IOUtils.copy(is, byteArrayOutputStream);
            IOUtils.closeQuietly(byteArrayOutputStream);
            IOUtils.closeQuietly(is);

            assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));

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

            Map map = given().multiPart("files", files.get(0)).multiPart("files", files.get(1)).multiPart("files", files.get(2)).multiPart("files", files.get(3))
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/map/file").as(Map.class);

            assertThat(map.size(), equalTo(4));

            assertThat(map.get(files.get(0).getName()), equalTo(files.get(0).getTotalSpace() + ""));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void responseUploadOptionalFilePathParameter()
    {

        try
        {

            final InputStream is = given().multiPart("file", file).accept(ContentType.ANY).contentType(MediaType.MULTIPART_FORM_DATA.contentType()).when().post("v1/tests/response/file/path/optional").asInputStream();

            try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
            {
                IOUtils.copy(is, byteArrayOutputStream);

                assertThat(byteArrayOutputStream.size(), equalTo(Long.valueOf(file.length()).intValue()));
            }

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartMixed()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given().multiPart("buffer", file)
                             .multiPart("user", model, MediaType.JSON.contentType())
                             .formParam("userId", 101)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/mixed").as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("buffer").toString(), containsString(file.length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartFutureMixed()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);



            Map map = given().multiPart("buffer", file, MediaType.APPLICATION_OCTET_STREAM.contentType())
                             .multiPart("user", model, MediaType.JSON.contentType())
                             .formParam("userId", 101)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/future/mixed").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("buffer").toString(), containsString(file.length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void uploadMultipartMixedWithPath()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given().multiPart("path", file)
                             .multiPart("user", model, MediaType.JSON.contentType())
                             .formParam("userId", 101)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/path-mixed").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("path").toString(), containsString(file.length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartFutureMixedWithPath()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given().multiPart("path", file)
                             .multiPart("user", model, MediaType.JSON.contentType())
                             .formParam("userId", 101)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/future/path-mixed").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("path").toString(), containsString(file.length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void uploadMultipartMixedWithFile()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given().multiPart("file", file)
                             .multiPart("user", model, MediaType.JSON.contentType())
                             .formParam("userId", 101)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/file-mixed").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("file").toString(), containsString(file.length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartFutureMixedWithFile()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given().multiPart("file", file)
                             .multiPart("user", model, MediaType.JSON.contentType())
                             .formParam("userId", 101)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/future/file-mixed").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("file").toString(), containsString(file.length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void uploadMultipartMultipleBuffers()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given()
                    .multiPart("file1", files.get(0), MediaType.AUDIO_MP4.contentType())
                    .multiPart("file2", files.get(1), MediaType.AUDIO_MP4.contentType())
                    .multiPart("file3", files.get(2), MediaType.AUDIO_MP4.contentType())
                    .multiPart("user", model, MediaType.JSON.contentType())
                    .formParam("userId", 101)
                    .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                    .accept(ContentType.JSON).when()

                    .post("v1/tests/multipart/multiple-buffers")
                    .as(Map.class);

            assertThat(map.size(), equalTo(5));

            assertThat(map.get("file1").toString(), containsString(files.get(0).length() + ""));
            assertThat(map.get("file2").toString(), containsString(files.get(1).length() + ""));
            assertThat(map.get("file3").toString(), containsString(files.get(2).length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void uploadMultipartMultipleFiles()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given()
                    .multiPart("file1", files.get(0), MediaType.AUDIO_MP4.contentType())
                    .multiPart("file2", files.get(1), MediaType.AUDIO_MP4.contentType())
                    .multiPart("file3", files.get(2), MediaType.AUDIO_MP4.contentType())
                    .multiPart("user", model, MediaType.JSON.contentType())
                    .formParam("userId", 101)
                    .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                    .accept(ContentType.JSON).when().post("v1/tests/multipart/multiple-files").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(5));

            assertThat(map.get("file1").toString(), containsString(files.get(0).length() + ""));
            assertThat(map.get("file2").toString(), containsString(files.get(1).length() + ""));
            assertThat(map.get("file3").toString(), containsString(files.get(2).length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @Test
    public void uploadMultipartMultiplePaths()
    {

        try
        {

            User model = new User(101L, UserType.ADMIN);

            Map map = given()
                    .multiPart("file1", files.get(0), MediaType.AUDIO_MP4.contentType())
                    .multiPart("file2", files.get(1), MediaType.AUDIO_MP4.contentType())
                    .multiPart("file3", files.get(2), MediaType.AUDIO_MP4.contentType())
                    .multiPart("user", model, MediaType.JSON.contentType())
                    .formParam("userId", 101)
                    .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                    .accept(ContentType.JSON).when().post("v1/tests/multipart/multiple-paths").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(5));

            assertThat(map.get("file1").toString(), containsString(files.get(0).length() + ""));
            assertThat(map.get("file2").toString(), containsString(files.get(1).length() + ""));
            assertThat(map.get("file3").toString(), containsString(files.get(2).length() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartByteBuffer()
    {

        try
        {

            Map map = given().multiPart("buffer", file, MediaType.APPLICATION_OCTET_STREAM.contentType())
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/bytebuffer").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(1));

            assertThat(map.get("size").toString(), containsString(file.length() + ""));

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            fail(e.getMessage());
        }

    }

    @SuppressWarnings("resource")
    @Test
    public void uploadMultipartFutureByteBuffer()
    {

        try
        {

            Map map = given().multiPart("buffer", file, MediaType.APPLICATION_OCTET_STREAM.contentType())
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(MediaType.JSON.contentType()).when().post("v1/tests/multipart/future/bytebuffer").then().extract().as(Map.class);

            assertThat(map.size(), equalTo(1));

            assertThat(map.get("size").toString(), containsString(file.length() + ""));

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


}
