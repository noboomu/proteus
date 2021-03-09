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
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
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
@RunWith(DefaultServer.class)
public class TestControllerEndpoints2 {

    private File file = null;

    private List<File> files = new ArrayList<>();

    private Set<Long> idSet = new HashSet<>();

    @Before
    public void setUp()
    {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        try
        {
            for (int i = 0; i < 4; i++)
            {
                byte[] bytes = new byte[8388608];
                Random random = new Random();
                random.nextBytes(bytes);

                files.add(Files.createTempFile("test-asset", ".mp4").toFile());

                LongStream.range(1L, 10L).forEach(l -> {

                    idSet.add(l);
                });
            }

            file = files.get(0);

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
                             .multiPart("userId", 101)
                             .log().all(true)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/mixed").as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("buffer"), equalTo(file.getTotalSpace() + ""));
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
                             .multiPart("userId", 101)
                             .log().all(true)
                             .contentType(MediaType.MULTIPART_FORM_DATA.contentType())
                             .accept(ContentType.JSON).when().post("v1/tests/multipart/future/mixed").as(Map.class);

            assertThat(map.size(), equalTo(3));

            assertThat(map.get("buffer"), equalTo(file.getTotalSpace() + ""));
            assertThat(map.get("user").toString(), containsString("101"));
            assertThat(map.get("userId").toString(), equalTo("101"));

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
