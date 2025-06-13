# Proteus WebSocket Module

The `proteus-websocket` module provides modern, annotation-driven WebSocket support for Proteus applications.

## Features

- **Annotation-Based**: Simple `@WebSocket` annotation to define endpoints
- **Lifecycle Management**: `@OnOpen`, `@OnMessage`, `@OnClose`, `@OnError` annotations
- **Virtual Thread Support**: Compatible with Proteus virtual thread features
- **Security Integration**: Automatic security context injection
- **JSON Support**: Built-in JSON serialization/deserialization
- **Modular Design**: Optional dependency - include only when needed

## Quick Start

### 1. Add Dependency

```xml
<dependency>
    <groupId>io.sinistral</groupId>
    <artifactId>proteus-websocket</artifactId>
    <version>0.9.0-SNAPSHOT</version>
</dependency>
```

### 2. Create WebSocket Endpoint

```java
@WebSocket("/websocket/echo")
@Singleton
public class EchoWebSocketController {
    
    @OnOpen
    public void onOpen(WebSocketConnection connection) {
        connection.sendText("Welcome to Echo WebSocket!");
    }
    
    @OnMessage(virtual = true)  // Run on virtual thread
    public void onMessage(WebSocketConnection connection, String message) {
        connection.sendText("Echo: " + message);
    }
    
    @OnMessage
    public void onJsonMessage(WebSocketConnection connection, MyMessage msg) {
        // Automatic JSON deserialization
        MyResponse response = processMessage(msg);
        connection.sendJson(response);
    }
    
    @OnClose
    public void onClose(WebSocketConnection connection, CloseReason reason) {
        System.out.println("Connection closed: " + reason.getReason());
    }
    
    @OnError
    public void onError(WebSocketConnection connection, Throwable error) {
        error.printStackTrace();
    }
}
```

### 3. Use WebSocketApplication

```java
public class MyApplication {
    public static void main(String[] args) {
        new WebSocketApplication()
            .addController(EchoWebSocketController.class)
            .processWebSocketEndpoints()  // Important: call before start()
            .start();
    }
}
```

## Configuration

Add to your `application.conf`:

```hocon
proteus {
  websockets {
    enabled = true
    
    options {
      maxMessageSize = 1048576      # 1MB
      connectionTimeout = 300000     # 5 minutes
      compression = true
      bufferPoolSize = 100
    }
    
    security {
      allowInsecureUpgrade = true
      requireAuthentication = false
      
      cors {
        enabled = false
        allowedOrigins = ["*"]
        allowCredentials = false
      }
    }
  }
}
```

## Advanced Features

### Security Context Integration

```java
@OnMessage
public void onAuthenticatedMessage(WebSocketConnection connection, 
                                 String message, 
                                 SecurityContext securityContext) {
    if (securityContext != null && securityContext.isAuthenticated()) {
        String userId = securityContext.getUserId().orElse("anonymous");
        connection.sendText("Hello " + userId + ": " + message);
    }
}
```

### Connection Management

```java
public class WebSocketConnection {
    // Get unique connection ID
    String getId();
    
    // Send text message
    CompletableFuture<Void> sendText(String message);
    
    // Send binary data
    CompletableFuture<Void> sendBinary(byte[] data);
    
    // Send JSON object
    CompletableFuture<Void> sendJson(Object object);
    
    // Close connection
    void close(int code, String reason);
    
    // Check connection status
    boolean isOpen();
    
    // Get security context
    Optional<SecurityContext> getSecurityContext();
    
    // Custom attributes
    void setAttribute(String key, Object value);
    Object getAttribute(String key);
}
```

### Virtual Thread Support

Mark handler methods with `virtual = true` for virtual thread execution:

```java
@OnMessage(virtual = true)
public void onSlowMessage(WebSocketConnection connection, String message) {
    // This will run on a virtual thread
    String result = performSlowOperation(message);
    connection.sendText(result);
}
```

## Integration with Existing Applications

If you have an existing Proteus application, you can add WebSocket support:

1. Add the dependency
2. Include the WebSocket module: `app.addModule(WebSocketModule.class)`
3. Process endpoints after adding controllers
4. Enable WebSocket in configuration

## Testing

The module includes a comprehensive test controller demonstrating all features:

```java
// See: io.sinistral.proteus.test.controllers.WebSocketTestController
```

## Dependencies

- **proteus-core**: Core Proteus functionality
- **undertow-websockets-jsr**: Undertow WebSocket support
- **jackson-databind**: JSON serialization
- **guice**: Dependency injection
- **slf4j-api**: Logging

## License

MIT License - same as Proteus core
