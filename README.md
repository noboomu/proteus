# Proteus

Proteus is a minimal Java API framework built on Undertow. At startup it turns Jakarta REST controller methods into generated Undertow handlers, compiles them with SourceBuddy, and registers them on a `RoutingHandler`.

## Requirements

- JDK 25 or newer; Maven Enforcer requires the configured project version. The consolidated development line is verified with JDK 26.
- Maven 3.

The current development version is `0.9.5-SNAPSHOT`.

## Modules

- `proteus-core`: Undertow integration, generated handlers, Guice, configuration, services, Jackson 3, and JWT security.
- `proteus-openapi`: OpenAPI 3.1 generation and Swagger UI/ReDoc serving.

Event Bus and WebSocket support are intentionally deferred. The current reactor does not publish or configure either feature.

## Build and test

```bash
mvn clean verify
mvn test -Dtest=SecurityEndpointsTest
mvn test -Dtest=OpenApiSecurityTest
```

## Basic application

```java
import static io.sinistral.proteus.server.ServerResponse.response;

import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public final class ExampleController {
    @GET
    @Path("/hello")
    public ServerResponse<?> hello(ServerRequest request) {
        return response(java.util.Map.of("message", "hello")).applicationJson();
    }
}

public final class Application {
    public static void main(String[] args) {
        new ProteusApplication()
            .addController(ExampleController.class)
            .start();
    }
}
```

`ServerResponse<T>` is the preferred controller return type. A controller that accepts a raw `HttpServerExchange` owns completing that exchange and should not return a response value.

## Request execution

Controllers use Jakarta REST annotations such as `@Path`, `@GET`, `@POST`, `@Produces`, `@Consumes`, `@PathParam`, `@QueryParam`, `@HeaderParam`, `@CookieParam`, `@FormParam`, and `@BeanParam`.

Use `@Blocking` for a controller method that performs blocking work. If Undertow invokes that handler on an I/O thread, Proteus dispatches the controller invocation to a virtual thread. Normal handlers remain on Undertow’s configured worker model; Proteus does not make the XNIO worker virtual-thread based by default.

`CompletableFuture<ServerResponse<T>>` is supported for asynchronous HTTP responses. Authentication context is never ambient: pass or capture the explicitly injected `SecurityContext` for any controller-created asynchronous work.

## JWT security

JWT validation is configured under `proteus.security.jwt` in HOCON. By default, signature verification and an expiration claim are required. Configure at least one supported verification key before authenticating requests.

```hocon
proteus.security.jwt {
  allowedAlgorithms = ["HS256"]
  signatureVerificationRequired = true
  expirationRequired = true
  hmac.secrets = [${?PROTEUS_JWT_HMAC_SECRET}]
}
```

Supported endpoint policies:

- `@PermitAll`: admits anonymous requests. A valid optional Bearer token still creates a request context; an invalid token does not grant access or create one.
- `@DenyAll`: rejects every request.
- `@RolesAllowed({"admin", "operator"})`: requires authentication and any one declared role. An empty role list denies all access. Blank or duplicate roles, and conflicting security annotations on one element, fail application startup.
- `@Claim`: injects a JWT claim and, without an explicit policy, makes the endpoint authenticated. An explicit method policy overrides a class policy, and either explicit policy overrides the claim fallback.

A controller can receive the validated context explicitly:

```java
@GET
@Path("/admin")
@RolesAllowed("admin")
public ServerResponse<?> admin(
    @Claim("sub") String subject,
    io.sinistral.proteus.security.SecurityContext securityContext
) {
    return response(java.util.Map.of(
        "subject", subject,
        "roles", securityContext.getRoles()
    )).applicationJson();
}
```

`SecurityContext` is attached to the request exchange and injected as a controller parameter. There is no `SecurityContext.getCurrent()` thread-local API. Code that starts its own futures, executors, or reactive work must retain the injected value explicitly.

`JsonWebToken.isValid()` only checks the token’s `exp` and `nbf` times against the current clock. It does not authenticate a token. Use `JwtService.validateToken` or normal request processing before trusting claims.

## OpenAPI

Include `proteus-openapi` to generate an OpenAPI 3.1 document and UI. With the default configuration, the UI is at `${application.path}/openapi` and the YAML document is at `${application.path}/openapi.yaml`.

OpenAPI generation starts asynchronously with `OpenAPIService`. The spec getters are null and spec routes can return `404` until generation completes. Generated endpoint security uses the same `SecurityPolicy` resolver as runtime handlers.

## Services and modules

Services implement `BaseService` or extend `DefaultService` and are registered with `addService`. Guice modules are registered with `addModule`, and controllers with `addController`, before `start()`.

Use `shutdown()` for complete teardown. It stops Undertow, waits up to two seconds for managed services, terminates the application-owned XNIO worker, clears the application state, and removes the shutdown hook. `stop()` is Undertow-only and does not perform complete teardown.

## Dependencies

Proteus uses Undertow, Guice, Typesafe Config, Jackson 3, JavaPoet, SourceBuddy, Nimbus JOSE + JWT, and Jakarta REST annotations. It does not use Spring.
