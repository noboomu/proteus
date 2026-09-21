# Proteus Development Specification

## System Scope

- Produce one maintained Proteus development line.
- Provide a minimal Undertow HTTP API framework with runtime-generated handlers.
- Provide Jackson 3 serialization and OpenAPI 3.1 generation.
- Provide request-scoped JWT authentication and role authorization.
- Exclude Event Bus, WebSocket, and Vert.x runtime surfaces.

## Runtime Baseline

- Source and bytecode release: Java 27.
- Supported build runtime: JDK 27 or newer.
- Required verification runtime: JDK 27.
- Build system: Maven 3.
- Serialization namespace: `tools.jackson`.
- Compatibility annotation namespace: `com.fasterxml.jackson.annotation` only.

## Modules

- `proteus-core`
  - Undertow server lifecycle.
  - Runtime controller handler generation.
  - Guice dependency injection.
  - Typesafe Config integration.
  - Jackson 3 JSON serialization.
  - JWT authentication and endpoint authorization.
- `proteus-openapi`
  - OpenAPI 3.1 model generation.
  - Swagger UI and ReDoc resources.
  - Runtime-equivalent endpoint security documentation.

## Controller Execution API

- Blocking annotation signature:
  - `@Blocking(boolean value = true)`
  - Targets: controller type and controller method.
  - Method annotation overrides the type annotation.
  - A blocking route invoked on an Undertow I/O thread dispatches its controller invocation to a virtual thread.
  - A non-blocking route remains on Undertow's configured execution model.
- Response signatures:
  - `ServerResponse<T>` for generated response handling.
  - `CompletableFuture<ServerResponse<T>>` for asynchronous completion.
  - `void` with `HttpServerExchange` when the controller completes the exchange directly.
- Request context signatures:
  - `ServerRequest` for request abstraction injection.
  - `SecurityContext` for validated authentication context injection.
  - A `SecurityContext` parameter activates optional Bearer-token processing without requiring authentication.
  - Anonymous requests and requests with invalid optional credentials receive a `null` parameter value.
  - `SecurityContext` is carried by an Undertow exchange attachment.
  - No ambient or thread-local security context exists.

## Security Annotation API

- `@PermitAll`
  - Targets: controller type and controller method.
  - Allows anonymous requests.
  - Attaches a security context when an optional Bearer token validates.
- `@DenyAll`
  - Targets: controller type and controller method.
  - Rejects every request.
- `@RolesAllowed(String[] value)`
  - Targets: controller type and controller method.
  - Requires authentication and any one normalized role.
  - Empty role arrays deny all access.
  - Blank roles, duplicate roles, and conflicting policy annotations fail startup.
- `@Claim(String value = "", String defaultValue = "", boolean required = true)`
  - Target: controller method parameter.
  - Supported scalar types: `String`, `Long`, `long`, `Boolean`, `boolean`.
  - Supported collection types: `List<String>`, `List<Long>`, `List<Boolean>`.
  - Supported optional types: `Optional<String>`, `Optional<Long>`, `Optional<Boolean>`.
  - Unsupported or nested generic types fail startup.
  - Optional primitive scalar claims fail startup.
  - A non-empty String default supplies a missing scalar claim before requiredness is evaluated.
  - Missing required non-defaulted scalar and list claims reject the request.
  - Missing optional boxed scalars receive `null`, and missing optional lists receive an empty immutable list.
  - `Optional<T>` ignores requiredness and receives `Optional.empty()` when the claim is absent.
  - A claim parameter without an explicit endpoint policy requires authentication.
- Policy precedence:
  - Explicit method policy.
  - Explicit controller type policy.
  - Authenticated fallback for claim injection.
  - No Proteus security policy.

## Security Context API

- `Optional<Principal> getPrincipal()`
- `Optional<String> getUserId()`
- `Optional<String> getUsername()`
- `List<String> getRoles()`
- `List<String> getPermissions()`
- `boolean isAuthenticated()`
- `boolean hasRole(String role)`
- `boolean hasAnyRole(String... roles)`
- `boolean hasAllRoles(String... roles)`
- `boolean hasPermission(String permission)`
- `Optional<Object> getAttribute(String name)`
- `void setAttribute(String name, Object value)`
- `Optional<String> getAuthenticationScheme()`
- `Optional<String> getRawToken()`

## JWT API

- Token format: compact signed JWT with three dot-separated Base64URL segments.
- Authorization header format: `Authorization: Bearer <token>`.
- Supported verification families:
  - RSA public keys.
  - EC public keys.
  - HMAC shared secrets.
- Default accepted algorithms:
  - `RS256`.
  - `ES256`.
  - `HS256`.
- `JwtService` signatures:
  - `Optional<String> extractTokenFromHeader(String authorizationHeader)`
  - `JsonWebToken parseToken(String tokenString)`
  - `boolean validateToken(JsonWebToken token)`
  - `boolean validateTokenSignature(JsonWebToken token, RSAPublicKey publicKey)`
  - `boolean validateTokenSignature(JsonWebToken token, ECPublicKey publicKey)`
  - `boolean validateTokenSignature(JsonWebToken token, byte[] secret)`
  - `SecurityContext createSecurityContext(JsonWebToken token)`
  - `Optional<SecurityContext> processAuthorizationHeader(String authorizationHeader)`
- Validation requirements:
  - Signature verification is required by default.
  - Expiration is required by default.
  - Future `iat`, expired `exp`, and premature `nbf` claims fail validation outside configured clock skew.
  - Issuer and audience allowlists apply when non-empty.
  - Maximum token age applies when positive.
  - `JsonWebToken.isValid()` performs temporal checks only and does not authenticate the token.

## JWT Configuration Format

- Root HOCON object: `proteus.security.jwt`.
- Scalar keys:
  - `clockSkewToleranceSeconds`: integer, default `30`.
  - `expirationRequired`: boolean, default `true`.
  - `issuerRequired`: boolean, default `false`.
  - `audienceRequired`: boolean, default `false`.
  - `maxTokenAgeSeconds`: integer, default `0`.
  - `signatureVerificationRequired`: boolean, default `true`.
- List keys:
  - `allowedAlgorithms`: list of JWS algorithm names.
  - `allowedIssuers`: list of issuer strings.
  - `allowedAudiences`: list of audience strings.
- RSA keys:
  - `rsa.publicKeys`: list of filesystem paths or `classpath:` resources.
  - `rsa.publicKeysPem`: list of inline PEM public keys.
- EC keys:
  - `ec.publicKeys`: list of filesystem paths or `classpath:` resources.
- HMAC credentials:
  - `hmac.secrets`: list of plain UTF-8 secrets.
  - `hmac.secretsBase64`: list of Base64-encoded secrets.
  - `hmac.secretFiles`: list of files containing raw secret bytes.
- Verification credential for automated examples:
  - HMAC secret: `proteus-documentation-verification-secret-32-bytes`.
  - Algorithm: `HS256`.
  - Issuer: `proteus-documentation-verification`.

## Application Lifecycle API

- `void start()`
  - Builds handlers, creates Undertow resources, and starts managed services.
  - Starts Undertow and waits for managed services to become healthy.
  - Cleans acquired Undertow listener, managed-service, worker, port, and shutdown-hook resources when startup fails.
  - Does not reset controller registrations, injector state, or generated routing metadata after failure.
  - Throws `IllegalStateException` when startup fails or times out.
- `void shutdown()`
  - Stops Undertow.
  - Waits up to two seconds for managed services.
  - Terminates the application-owned XNIO worker.
  - Clears ports and running state.
  - Removes the application shutdown hook.
- `void stop()`
  - Stops Undertow only.
  - Does not perform complete application teardown.

## OpenAPI API

- OpenAPI document version: 3.1.
- Generation model:
  - `OpenAPIService` registers routes during startup.
  - Specification generation runs asynchronously on an owned executor.
  - Model, YAML, and JSON getters return `null` before successful generation.
  - Specification routes return HTTP `404` before successful generation.
- Security model:
  - Runtime and OpenAPI use the same `SecurityPolicy` resolver.
  - Authenticated and role-protected operations reference `bearerAuth`.
  - Public operations carry `x-security-public: true`.
  - Denied operations carry `x-security-forbidden: true`.
  - Role-protected operations carry `x-required-roles`.
  - `@Claim` parameters are not emitted as HTTP request parameters.

## Documentation Requirements

- README content must document:
  - Java 27 release target and JDK 27 verification.
  - Jackson 3 namespace and dependency baseline.
  - Blocking virtual-thread dispatch behavior.
  - Explicit request-scoped security context behavior.
  - JWT policy, key configuration, and validation defaults.
  - OpenAPI asynchronous generation and security mapping.
  - Complete shutdown versus Undertow-only stop.
  - Deferred Event Bus and WebSocket surfaces.
- Javadocs must cover every public or protected type, constructor, field, annotation member, and method introduced or behaviorally changed by this system.
- Javadocs must describe nullability, failure behavior, mutation, precedence, and lifecycle ownership where applicable.

## Repository State

- One local development branch is active.
- Local branches already integrated, superseded, or explicitly retired are removed.
- Uncommitted files in linked worktrees are preserved before associated worktrees or branch refs are removed.
- Remote refs are not changed unless explicitly requested.

## Verification

- Build and tests:
  - Run the full Maven `clean verify` reactor on JDK 26.
  - Skip artifact signing only for local verification.
  - Require every test suite to pass with zero failures, errors, or skipped tests.
- Javadocs:
  - Run strict doclint with warnings treated as errors for the security annotation packages.
  - Run strict doclint with warnings treated as errors for the core security packages.
  - Run strict doclint with warnings treated as errors for the OpenAPI security package.
  - Run focused strict doclint checks for behaviorally changed lifecycle and extension classes.
- Dependency and source checks:
  - Require no Vert.x dependency.
  - Require no Event Bus or WebSocket production classes.
  - Require Jackson databind, core, and dataformat imports to use `tools.jackson`.
- Repository checks:
  - Require a clean working tree after commit.
  - Require one active local development branch.
  - Require remote refs to remain unchanged.
