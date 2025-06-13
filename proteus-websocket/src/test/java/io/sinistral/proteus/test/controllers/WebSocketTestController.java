package io.sinistral.proteus.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.websocket.*;
import io.sinistral.proteus.security.SecurityContext;
import io.sinistral.proteus.websocket.CloseReason;
import io.sinistral.proteus.websocket.WebSocketConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test controller demonstrating WebSocket functionality.
 * 
 * This controller provides various WebSocket endpoints for testing:
 * - Echo service
 * - Broadcast service  
 * - JSON message handling
 * - Authentication-aware endpoints
 * 
 * @since 1.0
 */
@WebSocket("/websocket/echo")
@Singleton
public class WebSocketTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketTestController.class);
    
    private final ObjectMapper objectMapper;
    
    @Inject
    public WebSocketTestController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    @OnOpen
    public void onOpen(WebSocketConnection connection, SecurityContext securityContext) {
        logger.info("WebSocket connection opened: {}", connection.getId());
        
        if (securityContext != null && securityContext.isAuthenticated()) {
            String username = securityContext.getUsername().orElse("authenticated user");
            logger.info("Authenticated user connected: {}", username);
            connection.sendText("Welcome, " + username + "!");
        } else {
            connection.sendText("Welcome to Echo WebSocket!");
        }
    }
    
    @OnMessage(virtual = true)
    public void onTextMessage(WebSocketConnection connection, String message) {
        logger.debug("Received text message: {}", message);
        
        // Echo the message back
        connection.sendText("Echo: " + message);
        
        // Handle special commands
        if (message.startsWith("/")) {
            handleCommand(connection, message);
        }
    }
    
    @OnMessage
    public void onBinaryMessage(WebSocketConnection connection, byte[] data) {
        logger.debug("Received binary message of {} bytes", data.length);
        
        // Echo binary data back
        connection.sendBinary(data);
    }
    
    @OnMessage
    public void onJsonMessage(WebSocketConnection connection, TestMessage testMessage) {
        logger.debug("Received JSON message: {}", testMessage);
        
        try {
            // Create response
            TestMessage response = new TestMessage();
            response.setType("response");
            response.setContent("Processed: " + testMessage.getContent());
            response.setTimestamp(System.currentTimeMillis());
            response.setSender("server");
            
            String jsonResponse = objectMapper.writeValueAsString(response);
            connection.sendText(jsonResponse);
            
        } catch (Exception e) {
            logger.error("Error processing JSON message", e);
            connection.sendText("{\"error\":\"Failed to process message\"}");
        }
    }
    
    @OnClose
    public void onClose(WebSocketConnection connection, CloseReason closeReason) {
        logger.info("WebSocket connection closed: {} - Code: {}, Reason: {}", 
                   connection.getId(), closeReason.getCode(), closeReason.getReason());
    }
    
    @OnError
    public void onError(WebSocketConnection connection, Throwable error) {
        logger.error("WebSocket error on connection: " + connection.getId(), error);
    }
    
    private void handleCommand(WebSocketConnection connection, String command) {
        String[] parts = command.substring(1).split(" ", 2);
        String cmd = parts[0].toLowerCase();
        
        switch (cmd) {
            case "ping":
                connection.sendText("pong");
                break;
            case "time":
                connection.sendText("Current time: " + java.time.Instant.now());
                break;
            case "info":
                String info = String.format("Connection ID: %s, Open: %s", 
                                           connection.getId(), connection.isOpen());
                connection.sendText(info);
                break;
            case "close":
                connection.close(1000, "Requested by client");
                break;
            default:
                connection.sendText("Unknown command: " + cmd);
        }
    }
    
    /**
     * Test message structure for JSON communication.
     */
    public static class TestMessage {
        private String type;
        private String content;
        private long timestamp;
        private String sender;
        
        // Constructors
        public TestMessage() {}
        
        public TestMessage(String type, String content, String sender) {
            this.type = type;
            this.content = content;
            this.sender = sender;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
        
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        
        @Override
        public String toString() {
            return String.format("TestMessage{type='%s', content='%s', sender='%s', timestamp=%d}", 
                               type, content, sender, timestamp);
        }
    }
}
