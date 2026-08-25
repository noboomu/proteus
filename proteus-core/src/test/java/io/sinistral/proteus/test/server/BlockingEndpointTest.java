package io.sinistral.proteus.test.server;

import static io.sinistral.proteus.test.util.TestClient.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(DefaultServer.class)
public class BlockingEndpointTest {

    @Test
    public void classLevelBlockingRunsOnVirtualThread() {
        given()
            .when()
            .get("v1/blocking/virtual")
            .then()
            .statusCode(200)
            .body("virtual", true);
    }

    @Test
    public void methodLevelFalseOverridesClassLevelBlocking() {
        given()
            .when()
            .get("v1/blocking/io")
            .then()
            .statusCode(200)
            .body("virtual", false);
    }
}
