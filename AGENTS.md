# AGENTS.md

This file provides guidance to Claude Code (claude.ai/code), Hermes, and other AI agents when working with code in this repository.

## Project Overview

Proteus is a blazing fast minimalist Java API server framework built atop Undertow. It's a multi-module Maven project that generates native Undertow handlers from JAX-RS annotated controller classes at runtime for maximum performance.

**Key Philosophy**: NO MAGIC - Proteus rewrites controller methods into high-performance Undertow handlers at runtime using JavaPoet and SourceBuddy for dynamic code generation.

## Build & Development Commands

### Build

```bash
# Build entire project
mvn clean install

# Build specific module
cd proteus-core && mvn clean install
cd proteus-openapi && mvn clean install

# Skip tests
mvn clean install -DskipTests

# Verify with JDK 26 while retaining the project's Java 25 release target
export JAVA_HOME=/usr/lib/jvm/java-26-openjdk
export PATH="$JAVA_HOME/bin:$PATH"
mvn clean verify
```

### Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StandardEndpointsTest

# Run with specific config
mvn test -Dconfig.file=src/test/resources/application.conf
```

### Release

```bash
# Prepare release (signs artifacts with GPG)
mvn release:prepare

# Deploy to Maven Central (requires credentials in settings.xml)
mvn deploy -P central
```

## Project Structure

### Module Organization

- **proteus-core**: Core framework including Undertow integration, handler generation, DI (Guice), and service management
- **proteus-openapi**: OpenAPI v3 support with auto-generated specs and Swagger UI

Event Bus and WebSocket support are intentionally deferred. They are not part of the current reactor, dependencies, configuration, or runtime surface.

### Key Architectural Components

#### Runtime Handler Generation (`proteus-core`)

The framework's core innovation is in `io.sinistral.proteus.server.handlers.HandlerGenerator`:

- Scans controller classes for JAX-RS annotations at startup
- Generates Undertow `HttpHandler` source code using JavaPoet
- Compiles handlers at runtime using SourceBuddy compiler
- Binds generated handlers to routes via `RoutingHandler`

**Critical Path**: `ProteusApplication.buildServer()` → `HandlerGenerator.generateClassSource()` → Runtime compilation → Handler registration

#### Dependency Injection (Guice)

- Configuration bound via `ConfigModule` (Typesafe Config)
- Services extend `DefaultService` and use `@Singleton` + `@Inject`
- Controllers injected with `ObjectMapper`, `Config`, and custom dependencies
- Module registration: `app.addModule(YourModule.class)` before `app.start()`

#### Service Lifecycle

Services implement `BaseService` or extend `DefaultService`:

- Extend `com.google.common.util.concurrent.AbstractIdleService`
- Override `startUp()` and `shutDown()` for lifecycle management
- Registered via `app.addService(YourService.class)`
- Managed by Guava's `ServiceManager` with health monitoring

#### Configuration System

Uses Typesafe Config (`application.conf`) with HOCON format:

- Default location: `src/test/resources/application.conf` (tests) or custom path
- Configuration injection via `@Named` annotations
- Key sections: `application`, `undertow`, `assets`, `openapi`, `globalHeaders`

### Critical Files

- `ProteusApplication.java`: Main application bootstrap and server builder
- `HandlerGenerator.java`: Runtime code generation for endpoint handlers
- `ServerRequest.java` / `ServerResponse.java`: Request/response abstractions
- `JacksonModule.java`: Jackson 3 `ObjectMapper` configured with Blackbird and a shared concurrent recycler pool

## Development Guidelines

### Controller Development

```java
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.WILDCARD)
public class MyController {

    @GET
    @Path("/resource/{id}")
    public ServerResponse<MyModel> getResource(
        ServerRequest request,
        @PathParam("id") Long id,
        @QueryParam("optional") Optional<String> optional
    ) {
        return response(new MyModel(id)).applicationJson();
    }

    // For max performance, use raw HttpServerExchange
    @GET
    @Path("/fast")
    @Blocking  // Force blocking if needed
    public void fastEndpoint(HttpServerExchange exchange) {
        exchange.getResponseSender().send("Fast!");
    }
}
```

**Important**:

- Methods returning `ServerResponse<T>` benefit from type safety
- Methods taking `HttpServerExchange` must handle response completion
- Use `@Blocking` for operations that must block (DB queries, etc.)
- Use `@Chain` to wrap handlers in custom middleware

### Service Development

```java
@Singleton
public class MyService extends DefaultService {

    @Inject
    protected Config config;

    @Inject
    protected ObjectMapper objectMapper;

    @Override
    protected void startUp() throws Exception {
        log.info("Starting MyService");
        // Initialize resources
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Stopping MyService");
        // Clean up resources
    }
}
```

### JSON Serialization

Access the configured `ObjectMapper` via dependency injection:

```java
@Inject
protected ObjectMapper objectMapper;

// The ObjectMapper is pre-configured in JacksonModule with:
// - Jackson 3 and Blackbird
// - A shared concurrent recycler pool
// - Tolerant request deserialization settings
```

### CompletableFuture Support

```java
@GET
@Path("/async")
public CompletableFuture<ServerResponse<User>> asyncEndpoint() {
    CompletableFuture<ServerResponse<User>> promise = new CompletableFuture<>();

    executor.execute(() -> {
        try {
            User user = fetchUser();
            promise.complete(response(user).applicationJson());
        } catch (Exception e) {
            promise.completeExceptionally(e);
        }
    });

    return promise;
}
```

**Preferred Pattern**: Declare `CompletableFuture` promise, use executor's `execute()` with inlined runnable, handle exceptions via `completeExceptionally()`.

### Request Security Context

`SecurityContext` is request-scoped state attached to the `HttpServerExchange`. Controllers that need it must declare an explicit `SecurityContext` parameter.

A `SecurityContext` parameter activates optional Bearer-token processing but does not require authentication by itself. Use `@RolesAllowed`, `@DenyAll`, `@PermitAll`, or `@Claim` when the endpoint needs an explicit policy; `@Claim` without another policy requires authentication.

There is no ambient `SecurityContext.getCurrent()` API. Controller-created futures, executors, or reactive work must capture and pass the injected context explicitly.

## Configuration Reference

### Key Config Paths

The following is an illustrative application override. Canonical built-in defaults are in `proteus-core/src/main/resources/reference.conf`.

```hocon
application {
  name = "my-app"
  version = "1.0"
  host = "localhost"
  path = "/v1"  # Base API path
  ports {
    http = 8080
    https = 8443  # If SSL enabled
  }
  services.timeout = 30s  # Service startup timeout
}

undertow {
  gracefulShutdown = true
  ioThreadsMultiplier = 2        # x availableProcessors
  workerThreadsMultiplier = 12   # x availableProcessors
  bufferSize = 16K
  directBuffers = true

  server {
    enableHttp2 = false
    maxEntitySize = 100M
    recordRequestStartTime = false
  }

  ssl {
    enabled = false
    keystorePath = "development.jks"
    keystorePassword = "password"
  }
}

proteus.security.jwt {
  allowedAlgorithms = ["RS256", "ES256", "HS256"]
  allowedIssuers = []
  allowedAudiences = []
  clockSkewToleranceSeconds = 30
  expirationRequired = true
  issuerRequired = false
  audienceRequired = false
  maxTokenAgeSeconds = 0
  signatureVerificationRequired = true

  rsa.publicKeys = []
  rsa.publicKeysPem = []
  ec.publicKeys = []
  hmac.secrets = []
  hmac.secretsBase64 = []
  hmac.secretFiles = []
}

globalHeaders {
  Server = ${application.name}
  # CORS headers if needed
}
```

## Testing

Test structure follows JUnit 5:

- Base test class: `AbstractEndpointTest`
- Uses the project `TestClient`, built on Java `HttpClient` and Jackson 3
- Config: `src/test/resources/application.conf` with random port (`ports.http = 0`)
- Test server: `DefaultServer` bootstraps test application

## Module System (Guice)

Creating custom modules for external integrations:

```java
public class MyModule extends AbstractModule {
    @Inject
    @Named("my.config.key")
    protected String configValue;

    @Override
    protected void configure() {
        // Bind services
        bind(MyService.class).toInstance(new MyService(configValue));
    }
}
```

Register before starting:

```java
app.addModule(MyModule.class);
app.start();
```

## OpenAPI Support

When using `proteus-openapi` module:

- Spec generated at `${application.path}/openapi.yaml`
- UI served at `${application.path}/openapi`
- Use `@Operation`, `@Parameter`, `@RequestBody` annotations
- Configure via `openapi {}` block in config
- Generation is asynchronous; getters may be null and spec routes may return `404` until generation completes
- Generated security requirements use the same `SecurityPolicy` resolution as runtime handlers
- `@Claim` parameters are omitted from the public HTTP parameter list
- The generated document is OpenAPI 3.1; no runtime `openapi.openapi` version switch exists

## Virtual Threads

Proteus does not replace Undertow's XNIO worker with a virtual-thread worker. Normal handlers remain on Undertow's configured execution model.

Generated handlers dispatch routes marked `@Blocking` to a virtual thread when Undertow invokes them on an I/O thread. A handler already running on a worker remains on that thread. A method-level `@Blocking(false)` overrides a class-level `@Blocking`, and parameter types that require blocking processing also trigger dispatch.

## Performance Characteristics

- Handler generation happens once at startup
- Zero reflection overhead during request processing
- Undertow's direct buffer pool with configurable buffer sizes
- Explicitly blocking routes are isolated from Undertow I/O threads through virtual-thread dispatch
- Regularly ranks top in Techempower benchmarks for Java frameworks

## Important Notes

- **Java Version**: Requires JDK 25+ and is verified with JDK 26
- **Compiler Args**: `-parameters` flag required for parameter name preservation
- **Development Branch**: `development`, with the Jackson 3 migration integrated
- **Main Branch**: `master` (use for PRs)
- **Security Context**: Exchange-attached and explicitly injected; never ambient or ThreadLocal-backed
- **Deferred Features**: Event Bus and WebSocket support must not be documented or wired as current production features
- **No Spring**: Framework explicitly avoids Spring dependencies
- **Minimal Dependencies**: Core intentionally avoids Spring and Vert.x
