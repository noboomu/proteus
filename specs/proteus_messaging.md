# Proteus Messaging Specification

## System Scope

- Provide an in-process typed event bus with publish, send, request-reply, and consumer registration.
- Provide annotation-driven consumer registration for Guice-managed singleton beans.
- Provide a NATS bridge that forwards event bus traffic across processes and machines.
- Provide WebSocket endpoints with annotation-driven lifecycle handlers and server-initiated broadcast.
- Serialize event payloads and WebSocket JSON frames with Jackson 3.
- This specification supersedes the Event Bus, WebSocket, and Vert.x exclusions in the development finalization specification.

## Runtime Baseline

- Source and bytecode release: Java 27.
- Supported build runtime: JDK 27 or newer.
- Required verification runtime: JDK 27.
- Build system: Maven 3.
- Event bus engine: Vert.x core 5.1.8 embedded in `proteus-core`.
- NATS client: jnats 2.23.0.
- Serialization namespace: `tools.jackson`.
- Compatibility annotation namespace: `com.fasterxml.jackson.annotation` only.
- Dependency injection: Guice.
- HTTP and WebSocket server: Undertow.

## Modules

- `proteus-core`
  - Embedded Vert.x platform lifecycle.
  - Event bus service, consumer registration, codecs, metrics.
  - NATS bridge service.
- `proteus-websocket`
  - Undertow WebSocket endpoint handling.
  - Annotation scanning and registration.
  - Connection registry and broadcast service.

## Event Bus

### EventBusService

```java
public interface EventBusService
{
    <T> void publish(String address, T message);

    <T> void send(String address, T message);

    <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType);

    <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType, long timeoutMs);

    <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer);

    <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer, EventBusConsumerOptions options);

    <T> void registerCodec(String codecName, EventBusCodec<T> codec);

    boolean isAvailable();

    EventBusMetrics getMetrics();
}
```

### Consumer Contract

```java
@FunctionalInterface
public interface EventBusConsumer<T>
{
    CompletableFuture<?> handle(Message<T> message);
}

public interface EventBusRegistration
{
    void unregister();

    String getAddress();

    boolean isActive();
}
```

- `Message` is `io.vertx.core.eventbus.Message`.
- A consumer completes its returned future when message processing finishes.
- A failed future increments the error metric and logs the cause at warn level.
- `registerConsumer` is idempotent per address; a second registration on the same address replaces the first.

### EventBusConsumerOptions

- `blocking`: boolean, default `false`; when `true`, the consumer runs on a dedicated blocking executor rather than the event loop.
- `ordered`: boolean, default `false`; when `true`, messages for the consumer are dispatched serially in arrival order.
- `local`: boolean, default `true`; when `true`, the consumer only receives messages delivered within the process.
- `codec`: string, default `""`; empty selects the default JSON codec; a non-empty value selects a registered named codec.
- `timeout`: long milliseconds, default `30000`; applies to request-reply initiated by the consumer.

### @ConsumeEvent

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumeEvent
{
    String value();
    boolean blocking() default false;
    boolean ordered() default false;
    boolean local() default true;
    String codec() default "";
}
```

- `ConsumeEventProcessor` scans Guice singleton beans at application startup.
- The annotated method accepts either the raw payload type or the `Message` type as its single parameter.
- An annotated method may return a value, a `CompletableFuture`, or `void`.
- A returned value or completed future is used as the reply body for request-reply messages.
- A method annotated `blocking() = true` executes on a virtual-thread-backed executor.

### EventBusCodec

```java
public interface EventBusCodec<T>
{
    Buffer encode(T object);

    T decode(Buffer buffer);
}
```

- The default codec serializes payloads to JSON with the application Jackson 3 `JsonMapper` and deserializes on receipt.
- Named codecs override the default codec for the consumers that select them.
- Local delivery between sender and consumer in the same process passes the payload by reference without encoding.

### EventBusMetrics

- Immutable snapshot with counters: `messagesSent`, `messagesReceived`, `messagesPublished`, `requestsSent`, `repliesReceived`, `pendingRequests`, `errorCount`, `timeoutCount`.
- Latency gauges: `averageLatencyMs`, `maxLatencyMs`.
- `getMetrics()` returns a point-in-time snapshot; counters are monotonic.

## NATS Bridge

### NatsBridgeService

```java
public interface NatsBridgeService
{
    void start();

    void stop();

    boolean isRunning();

    void forwardPublish(String address, Object message);

    CompletableFuture<Object> forwardRequest(String address, Object message, long timeoutMs);

    void subscribe(String address, java.util.function.Consumer<io.vertx.core.json.JsonObject> handler);
}
```

- The bridge activates when `proteus.messaging.nats.enabled` is `true`.
- `start()` connects the NATS connection and opens the shared subscription.
- `stop()` drains the connection and closes it; registered subscriptions are dropped.
- `forwardPublish` publishes a JSON envelope to the mapped NATS subject.
- `forwardRequest` performs a NATS request and completes with the decoded envelope payload.

### Address to Subject Mapping

- Event bus address `a.b.c` maps to NATS subject `<subjectPrefix>.a.b.c`.
- The bridge validates that mapped subjects contain only NATS-legal characters; addresses with wildcards are rejected with `IllegalArgumentException`.
- The bridge subscribes to `<subjectPrefix>.>` with queue group `<queueGroup>`.

### Wire Envelope

- Every bridged message is a single JSON document.

```json
{
  "address": "orders.created",
  "contentType": "application/json",
  "sentAt": 1695000000000,
  "payload": { }
}
```

- `address`: string, the originating event bus address.
- `contentType`: string, always `application/json` for the default codec.
- `sentAt`: number, epoch milliseconds at the sender.
- `payload`: the JSON-encoded message body.
- Replies use the same envelope with the reply payload in `payload`.

### Delivery Semantics

- `publish` on the event bus delivers locally and, when the bridge is running and the address is not local-only, to the mapped NATS subject.
- `send` on the event bus delivers locally; when the bridge is running, the NATS queue group ensures exactly one subscriber process receives the message.
- `request` prefers local consumers; when no local consumer answers within the local timeout, the bridge forwards the request to NATS.
- Messages arriving from NATS are dispatched to local consumers on the event loop; blocking consumers dispatch on the blocking executor.
- Envelope decode failures are logged at warn level and increment `errorCount`; the message is not redelivered.

## WebSocket

### Annotations

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocket
{
    String value();
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnOpen {}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMessage {}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClose {}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnError {}
```

- `@WebSocket` marks a Guice singleton as an endpoint at the given path.
- `@OnOpen` methods accept `WebSocketConnection`.
- `@OnMessage` methods accept `String` or `byte[]` plus `WebSocketConnection` in either order.
- `@OnClose` methods accept `WebSocketConnection` and `CloseReason` in either order.
- `@OnError` methods accept `WebSocketConnection` and `Throwable` in either order.
- Handler exceptions close the connection with code `1011` and log the cause at warn level.

### WebSocketService

```java
public interface WebSocketService
{
    void registerEndpoints(RoutingHandler router);

    Set<WebSocketConnection> getActiveConnections();

    Set<WebSocketConnection> getConnectionsForPath(String path);

    WebSocketConnection getConnection(String connectionId);

    CompletableFuture<Void> broadcastText(String message);

    CompletableFuture<Void> broadcastText(String path, String message);

    CompletableFuture<Void> broadcastJson(Object object);

    CompletableFuture<Void> broadcastJson(String path, Object object);

    CompletableFuture<Void> broadcastBinary(byte[] data);

    CompletableFuture<Void> broadcastBinary(String path, byte[] data);
}
```

- `registerEndpoints` adds upgrade handlers for every `@WebSocket` path to the Undertow routing handler.
- Path broadcasts deliver only to connections on that path.
- Broadcast futures complete when all target sends have been initiated; a failed send fails the future and does not abort remaining sends.

### WebSocketConnection

```java
public interface WebSocketConnection
{
    String getConnectionId();

    String getPath();

    CompletableFuture<Void> sendText(String message);

    CompletableFuture<Void> sendJson(Object object);

    CompletableFuture<Void> sendBinary(byte[] data);

    void close();

    void close(CloseReason reason);

    boolean isOpen();

    <T> T getAttribute(String key);

    void setAttribute(String key, Object value);

    io.undertow.websockets.core.WebSocketChannel getChannel();
}
```

- `sendJson` serializes with the application Jackson 3 `JsonMapper` and sends a text frame.
- `close(CloseReason)` sends the status code and reason text from the `CloseReason`.
- Connection ids are UUID strings generated at upgrade.

### CloseReason

```java
public record CloseReason(int code, String reason) {}
```

- `code` is a WebSocket close status code.
- `reason` is a UTF-8 string of at most 123 bytes.

### Frame Protocol

- Text frames carry UTF-8 strings; JSON text frames are produced only by `sendJson` and `broadcastJson`.
- Binary frames carry raw bytes without envelope.

## Configuration

- Root HOCON object: `proteus.messaging`.

### Event Bus

- `proteus.messaging.eventbus.enabled`: boolean, default `true`.
- `proteus.messaging.eventbus.blockingPoolSize`: integer, default `32`; virtual-thread executor pool size for blocking consumers.

### NATS Bridge

- `proteus.messaging.nats.enabled`: boolean, default `false`.
- `proteus.messaging.nats.url`: string, default `nats://tachikoma:4232`.
- `proteus.messaging.nats.subjectPrefix`: string, default `proteus`.
- `proteus.messaging.nats.queueGroup`: string, default `proteus`.
- `proteus.messaging.nats.requestTimeoutMs`: long, default `5000`.
- `proteus.messaging.nats.maxReconnects`: integer, default `60`.
- `proteus.messaging.nats.reconnectWaitMs`: long, default `2000`.
- `proteus.messaging.nats.auth.token`: string, optional; NATS bearer token.
- `proteus.messaging.nats.auth.username`: string, optional; NATS username.
- `proteus.messaging.nats.auth.password`: string, optional; NATS password.
- When token auth and user/password auth are both configured, token auth wins.

### WebSocket

- `proteus.websocket.enabled`: boolean, default `true` when `proteus-websocket` is on the classpath.
- `proteus.websocket.maxFrameSizeBytes`: integer, default `1048576`.
- `proteus.websocket.idleTimeoutMs`: long, default `300000`; zero disables the idle timeout.
- `proteus.websocket.maxConnectionsPerPath`: integer, default `0` for unlimited.
- When `maxConnectionsPerPath` is exceeded, the upgrade is rejected with close code `1013`.

## Dependency Rules

- `proteus-core` depends on `vertx-core` and `jnats`.
- `proteus-websocket` depends on `proteus-core` and Undertow WebSocket.
- No Reactor, RxJava, or Spring dependencies.
- Vert.x is used only for the embedded event bus; HTTP serving remains Undertow.

## Verification

- Event bus tests:
  - Publish delivers to all registered consumers on an address.
  - Send delivers to exactly one consumer.
  - Request-reply round trip returns the consumer reply with the declared response type.
  - Request timeout completes exceptionally with a timeout result.
  - `@ConsumeEvent` registration binds annotated methods with payload and `Message` parameter styles.
  - Blocking consumers execute off the event loop.
  - Metrics counters increase for sent, received, and published messages.
  - Named codec encode and decode round trip a payload.
- NATS bridge tests:
  - A running NATS service is required at `nats://tachikoma:4232`.
  - A publish on one bridge instance is received by a consumer on another bridge instance.
  - Queue-group subscription delivers exactly one copy of a sent message across two bridge instances.
  - `forwardRequest` returns the reply payload from a remote consumer.
  - Stop and start reconnects without losing registrations.
- WebSocket tests:
  - A Java WebSocket client upgrades to a `@WebSocket` path and receives an `@OnOpen` side effect.
  - Text and binary echo round trip through `@OnMessage`.
  - `broadcastText`, `broadcastJson`, and `broadcastBinary` reach all connected clients on the path.
  - Path-scoped broadcast does not reach connections on other paths.
  - Close with `CloseReason` delivers the code and reason to the client.
  - Handler exception closes the connection with code `1011`.
- Reactor build:
  - Full Maven `clean verify` on JDK 27 passes with zero failures, errors, or skipped tests.
  - Targeted strict doclint with warnings treated as errors passes for the eventbus, NATS bridge, and WebSocket production packages.
