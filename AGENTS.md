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

# Build with specific Java version (requires Java 21+)
mvn clean install -Djava.version=21
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
- `JacksonModule.java`: Configured ObjectMapper with Blackbird, JSR310, JDK8 modules

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
// - Blackbird (fast serialization)
// - JSR310 (Java 8 date/time)
// - JDK8 Module (Optional, etc.)
// - Parameter names preservation
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

## Configuration Reference

### Key Config Paths
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

globalHeaders {
  Server = ${application.name}
  # CORS headers if needed
}
```

## Testing

Test structure follows JUnit 5:
- Base test class: `AbstractEndpointTest`
- Uses REST Assured for HTTP testing
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

## Virtual Threads

Proteus uses Java 21+ virtual threads for Undertow's XNIO worker:
```java
ThreadGroup virtualThreadGroup = Thread.ofVirtual().unstarted(() -> {}).getThreadGroup();
XnioWorker worker = xnio.createWorkerBuilder().setThreadGroup(virtualThreadGroup).build();
```

This enables massive concurrency for blocking operations.

## Performance Characteristics

- Handler generation happens once at startup
- Zero reflection overhead during request processing
- Undertow's direct buffer pool with configurable buffer sizes
- Virtual threads prevent blocking operation bottlenecks
- Regularly ranks top in Techempower benchmarks for Java frameworks

## Important Notes

- **Java Version**: Requires JDK 21+ (enforced by maven-enforcer-plugin)
- **Compiler Args**: `-parameters` flag required for parameter name preservation
- **Current Branch**: `jackson-3.0` (migrating to Jackson 3.x)
- **Main Branch**: `master` (use for PRs)
- **No Spring**: Framework explicitly avoids Spring dependencies
- **Minimal Dependencies**: Core framework < 340KB
