package io.sinistral.proteus.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sinistral.proteus.security.SecurityContext;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a WebSocket connection with enhanced functionality.
 * 
 * This class wraps the Undertow WebSocketChannel and provides a higher-level API
 * for WebSocket communication, including JSON serialization, security context,
 * and connection management.
 * 
 * @since 1.0
 */
public class WebSocketConnection {
    
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    
    private final String id;
    private final WebSocketChannel channel;
    private final WebSocketHttpExchange exchange;
    private final SecurityContext securityContext;
    private final ObjectMapper objectMapper;
    private final Map<String, Object> attributes;
    private volatile boolean closed;
    
    public WebSocketConnection(WebSocketChannel channel, 
                              WebSocketHttpExchange exchange,
                              SecurityContext securityContext,
                              ObjectMapper objectMapper) {
        this.id = "ws-" + ID_GENERATOR.getAndIncrement();
        this.channel = channel;
        this.exchange = exchange;
        this.securityContext = securityContext;
        this.objectMapper = objectMapper;
        this.attributes = new ConcurrentHashMap<>();
        this.closed = false;
    }
    
    /**
     * Gets the unique connection ID.
     * 
     * @return connection ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the underlying Undertow WebSocket channel.
     * 
     * @return the WebSocket channel
     */
    public WebSocketChannel getChannel() {
        return channel;
    }
    
    /**
     * Gets the original HTTP exchange.
     * 
     * @return the HTTP exchange
     */
    public WebSocketHttpExchange getExchange() {
        return exchange;
    }
    
    /**
     * Gets the security context for this connection.
     * 
     * @return optional security context
     */
    public Optional<SecurityContext> getSecurityContext() {
        return Optional.ofNullable(securityContext);
    }
    
    /**
     * Checks if the connection is still open.
     * 
     * @return true if connection is open
     */
    public boolean isOpen() {
        return !closed && channel.isOpen();
    }
    
    /**
     * Sends a text message to the client.
     * 
     * @param message the text message to send
     * @return CompletableFuture that completes when message is sent
     */
    public CompletableFuture<Void> sendText(String message) {
        if (!isOpen()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Connection is closed"));
        }
        
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        try {
            io.undertow.websockets.core.WebSockets.sendText(message, channel, new io.undertow.websockets.core.WebSocketCallback<Void>() {
                @Override
                public void complete(WebSocketChannel channel, Void context) {
                    future.complete(null);
                }
                
                @Override
                public void onError(WebSocketChannel channel, Void context, Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
            });
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    /**
     * Sends a binary message to the client.
     * 
     * @param data the binary data to send
     * @return CompletableFuture that completes when message is sent
     */
    public CompletableFuture<Void> sendBinary(byte[] data) {
        return sendBinary(ByteBuffer.wrap(data));
    }
    
    /**
     * Sends a binary message to the client.
     * 
     * @param buffer the binary data to send
     * @return CompletableFuture that completes when message is sent
     */
    public CompletableFuture<Void> sendBinary(ByteBuffer buffer) {
        if (!isOpen()) {
            return CompletableFuture.failedFuture(new IllegalStateException("Connection is closed"));
        }
        
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        try {
            io.undertow.websockets.core.WebSockets.sendBinary(buffer, channel, new io.undertow.websockets.core.WebSocketCallback<Void>() {
                @Override
                public void complete(WebSocketChannel channel, Void context) {
                    future.complete(null);
                }
                
                @Override
                public void onError(WebSocketChannel channel, Void context, Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
            });
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    /**
     * Sends an object as JSON to the client.
     * 
     * @param object the object to serialize and send
     * @return CompletableFuture that completes when message is sent
     */
    public CompletableFuture<Void> sendJson(Object object) {
        try {
            String json = objectMapper.writeValueAsString(object);
            return sendText(json);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
    
    /**
     * Closes the WebSocket connection.
     * 
     * @return CompletableFuture that completes when connection is closed
     */
    public CompletableFuture<Void> close() {
        return close(1000, "Normal closure");
    }
    
    /**
     * Closes the WebSocket connection with a specific code and reason.
     * 
     * @param code the close code
     * @param reason the close reason
     * @return CompletableFuture that completes when connection is closed
     */
    public CompletableFuture<Void> close(int code, String reason) {
        if (closed) {
            return CompletableFuture.completedFuture(null);
        }
        
        closed = true;
        
        CompletableFuture<Void> future = new CompletableFuture<>();
        
        try {
            io.undertow.websockets.core.WebSockets.sendClose(code, reason, channel, new io.undertow.websockets.core.WebSocketCallback<Void>() {
                @Override
                public void complete(WebSocketChannel channel, Void context) {
                    future.complete(null);
                }
                
                @Override
                public void onError(WebSocketChannel channel, Void context, Throwable throwable) {
                    future.completeExceptionally(throwable);
                }
            });
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        
        return future;
    }
    
    /**
     * Gets a connection attribute.
     * 
     * @param key the attribute key
     * @return the attribute value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }
    
    /**
     * Gets a connection attribute with a default value.
     * 
     * @param key the attribute key
     * @param defaultValue the default value if attribute is not found
     * @return the attribute value, or default value if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, T defaultValue) {
        T value = (T) attributes.get(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Sets a connection attribute.
     * 
     * @param key the attribute key
     * @param value the attribute value
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    
    /**
     * Removes a connection attribute.
     * 
     * @param key the attribute key
     * @return the previous value, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T removeAttribute(String key) {
        return (T) attributes.remove(key);
    }
    
    /**
     * Gets the remote address of this connection.
     * 
     * @return the remote address
     */
    public String getRemoteAddress() {
        return channel.getSourceAddress() != null ? channel.getSourceAddress().toString() : "unknown";
    }
    
    /**
     * Gets the path of the WebSocket endpoint.
     * 
     * @return the endpoint path
     */
    public String getPath() {
        return exchange.getRequestURI();
    }
    
    @Override
    public String toString() {
        return "WebSocketConnection{" +
               "id='" + id + '\'' +
               ", path='" + getPath() + '\'' +
               ", remote='" + getRemoteAddress() + '\'' +
               ", open=" + isOpen() +
               '}';
    }
}
