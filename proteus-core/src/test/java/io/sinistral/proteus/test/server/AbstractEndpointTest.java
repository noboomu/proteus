package io.sinistral.proteus.test.server;

import io.restassured.RestAssured;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.LongStream;


public class AbstractEndpointTest {

    public static File file = null;

    public static List<File> files = new ArrayList<>();

    public static Set<Long> idSet = new HashSet<>();

    public static String PREFIX = "io.sinistral.proteus/test";

    public static Path tmpPath = null;

    public static void initData()
    {
        try
        {
            tmpPath = Paths.get(System.getProperty("java.io.tmpdir")).resolve(PREFIX);

            if (!tmpPath.toFile().exists())
            {
                tmpPath.toFile().mkdirs();
            }

            for (int i = 0; i < 4; i++)
            {
                byte[] bytes = new byte[1388608];
                Random random = new Random();
                random.nextBytes(bytes);

                Path dataPath = tmpPath.resolve("test-asset-" + i + ".mp4");

                dataPath.toFile().deleteOnExit();

                Files.write(dataPath, bytes);

                files.add(dataPath.toFile());

                if(i == 0)
                {
                    file = files.get(0);
                }

                LongStream.range(1L, 10L).forEach(l -> {

                    idSet.add(l);
                });
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @BeforeAll
    public static void init()
    {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();


    }

    @AfterAll
    public static void cleanup()
    {

        try
        {
            Thread.sleep(1000L);
            FileUtils.deleteDirectory(tmpPath.toFile());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp()
    {
        if(files.isEmpty())
        {
            initData();
        }

        file = files.get(0);
    }

}
