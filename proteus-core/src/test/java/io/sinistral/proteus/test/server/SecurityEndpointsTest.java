package io.sinistral.proteus.test.server;

import static io.sinistral.proteus.test.util.TestClient.given;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.sinistral.proteus.security.SecurityContext;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(DefaultServer.class)
public class SecurityEndpointsTest {

    private static final String SIGNING_SECRET =
        "proteus-test-hmac-secret-32-bytes!";
    private static final String SIGNING_SECRET_HS512 =
        "proteus-test-hmac-secret-for-hs512-64-bytes-0000000000000000000000!";

    @Test
    public void permitAllAllowsAnonymousRequests() {
        given()
            .when()
            .get("v1/security/public")
            .then()
            .statusCode(200)
            .body(containsString("public endpoint"));
    }

    @Test
    public void denyAllRejectsEveryRequest() {
        given()
            .when()
            .get("v1/security/denied")
            .then()
            .statusCode(403);
    }

    @Test
    public void rolesAllowedRequiresAuthentication() {
        given()
            .when()
            .get("v1/security/admin")
            .then()
            .statusCode(401);
    }

    @Test
    public void emptyRolesAllowedDeniesAllRequests() throws Exception {
        given()
            .when()
            .get("v1/security/empty-roles")
            .then()
            .statusCode(403);

        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "admin-user",
                    List.of("admin"),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/empty-roles")
            .then()
            .statusCode(403);
    }

    @Test
    public void classDenyAllPrecedesClaimAuthenticationFallback() throws Exception {
        given()
            .when()
            .get("v1/security/class-denied/claim")
            .then()
            .statusCode(403);

        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "admin-user",
                    List.of("admin"),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/class-denied/claim")
            .then()
            .statusCode(403);
    }

    @Test
    public void classRolesPrecedeClaimAuthenticationFallback() throws Exception {
        given()
            .when()
            .get("v1/security/class-admin/claim")
            .then()
            .statusCode(401);

        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "ordinary-user",
                    List.of("user"),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/class-admin/claim")
            .then()
            .statusCode(403);

        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "admin-user",
                    List.of("admin"),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/class-admin/claim")
            .then()
            .statusCode(200)
            .body(containsString("admin-user"));
    }

    @Test
    public void methodPoliciesOverrideClassPolicies() {
        given()
            .when()
            .get("v1/security/class-admin/public")
            .then()
            .statusCode(200);

        given()
            .when()
            .get("v1/security/class-public/denied")
            .then()
            .statusCode(403);
    }

    @Test
    public void rolesAllowedRejectsWrongRole() throws Exception {
        given()
            .header("Authorization", "Bearer " + token("test-user", List.of("user"), true, SIGNING_SECRET))
            .when()
            .get("v1/security/admin")
            .then()
            .statusCode(403);
    }

    @Test
    public void rolesAllowedAcceptsSignedTokenAndInjectsClaims() throws Exception {
        var response = given()
            .header("Authorization", "Bearer " + token("admin-user", List.of("admin"), true, SIGNING_SECRET))
            .when()
            .get("v1/security/admin");

        response.then().statusCode(200);
        assertTrue(response.body().contains("admin-user"), response.body());
        assertTrue(response.body().contains("admin"), response.body());
        assertTrue(
            response.body().contains("\"authenticated\":true"),
            response.body()
        );
    }

    @Test
    public void claimDefaultAndExpirationAreInjected() throws Exception {
        given()
            .header("Authorization", "Bearer " + token("test-user", List.of("user"), true, SIGNING_SECRET))
            .when()
            .get("v1/security/user")
            .then()
            .statusCode(200)
            .body(containsString("test-user"))
            .body(containsString("unknown"))
            .body(containsString("expiration"));
    }

    @Test
    public void invalidSignatureIsRejected() throws Exception {
        given()
            .header("Authorization", "Bearer " + token(
                "admin-user",
                List.of("admin"),
                true,
                "different-test-signing-secret-0001"
            ))
            .when()
            .get("v1/security/admin")
            .then()
            .statusCode(401);
    }

    @Test
    public void missingRequiredExpirationIsRejected() throws Exception {
        given()
            .header("Authorization", "Bearer " + token("admin-user", List.of("admin"), false, SIGNING_SECRET))
            .when()
            .get("v1/security/admin")
            .then()
            .statusCode(401);
    }

    @Test
    public void requiredClaimWithoutRoleAnnotationRequiresAuthentication()
        throws Exception {
        given()
            .when()
            .get("v1/security/claim-only")
            .then()
            .statusCode(401);

        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "claim-user",
                    List.of(),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/claim-only")
            .then()
            .statusCode(200)
            .body(containsString("claim-user"));
    }

    @Test
    public void bearerSchemeIsCaseInsensitive() throws Exception {
        given()
            .header(
                "Authorization",
                "bearer " + token(
                    "admin-user",
                    List.of("admin"),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/admin")
            .then()
            .statusCode(200);
    }

    @Test
    public void securityContextIsInjectedExplicitlyAfterVirtualThreadDispatch()
        throws Exception {
        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "admin-user",
                    List.of("admin"),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/blocking-context")
            .then()
            .statusCode(200)
            .body(containsString("\"attached\":true"))
            .body(containsString("\"injected\":true"))
            .body(containsString("\"sameContext\":true"))
            .body(containsString("\"virtual\":true"));

        given()
            .when()
            .get("v1/security/context-state")
            .then()
            .statusCode(200)
            .body(containsString("\"injected\":false"));

        given()
            .header("Authorization", "Bearer not-a-jwt")
            .when()
            .get("v1/security/context-state")
            .then()
            .statusCode(200)
            .body(containsString("\"injected\":false"));

        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "context-user",
                    List.of("user"),
                    true,
                    SIGNING_SECRET
                )
            )
            .when()
            .get("v1/security/context-state")
            .then()
            .statusCode(200)
            .body(containsString("\"injected\":true"));
    }

    @Test
    public void securityContextHasNoAmbientThreadLocalAccessor() {
        assertThrows(
            NoSuchMethodException.class,
            () -> SecurityContext.class.getMethod("getCurrent")
        );
    }

    @Test
    public void disallowedAlgorithmIsRejected() throws Exception {
        Instant now = Instant.now();
        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "admin-user",
                    List.of("admin"),
                    now,
                    now.plusSeconds(300),
                    SIGNING_SECRET_HS512,
                    JWSAlgorithm.HS512
                )
            )
            .when()
            .get("v1/security/admin")
            .then()
            .statusCode(401);
    }

    @Test
    public void tokenIssuedInFutureIsRejected() throws Exception {
        Instant future = Instant.now().plusSeconds(300);
        given()
            .header(
                "Authorization",
                "Bearer " + token(
                    "admin-user",
                    List.of("admin"),
                    future,
                    future.plusSeconds(300),
                    SIGNING_SECRET,
                    JWSAlgorithm.HS256
                )
            )
            .when()
            .get("v1/security/admin")
            .then()
            .statusCode(401);
    }

    private static String token(
        String subject,
        List<String> roles,
        boolean includeExpiration,
        String signingSecret
    ) throws Exception {
        Instant now = Instant.now();
        return token(
            subject,
            roles,
            now,
            includeExpiration ? now.plusSeconds(300) : null,
            signingSecret,
            JWSAlgorithm.HS256
        );
    }

    private static String token(
        String subject,
        List<String> roles,
        Instant issuedAt,
        Instant expiration,
        String signingSecret,
        JWSAlgorithm algorithm
    ) throws Exception {
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
            .subject(subject)
            .issueTime(Date.from(issuedAt))
            .claim("roles", roles);
        if (expiration != null) {
            claims.expirationTime(Date.from(expiration));
        }

        SignedJWT jwt = new SignedJWT(
            new JWSHeader(algorithm),
            claims.build()
        );
        jwt.sign(new MACSigner(signingSecret.getBytes(StandardCharsets.UTF_8)));
        return jwt.serialize();
    }
}
