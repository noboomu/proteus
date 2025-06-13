package io.sinistral.proteus.test.controllers;

import io.sinistral.proteus.annotations.eventbus.ConsumeEvent;
import io.sinistral.proteus.eventbus.EventBusService;
import io.sinistral.proteus.server.ServerResponse;
import io.vertx.core.eventbus.Message;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType; 
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

import static io.sinistral.proteus.server.ServerResponse.response;

/**
 * Test controller demonstrating Event Bus functionality.
 * 
 * This controller shows how to:
 * - Publish fire-and-forget messages
 * - Send point-to-point messages
 * - Use request/reply pattern
 * - Consume events with @ConsumeEvent annotation
 * 
 * @since 1.0
 */
@Path("/eventbus")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Singleton
public class EventBusTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(EventBusTestController.class);
    
    private final EventBusService eventBusService;
    
    @Inject
    public EventBusTestController(EventBusService eventBusService) {
        this.eventBusService = eventBusService;
    }
    
    /**
     * Publishes a message to all subscribers (publish/subscribe pattern).
     * 
     * Example: POST /eventbus/publish/user.created
     * Body: {"id": 1, "name": "John Doe", "email": "john@example.com"}
     */
    @POST
    @Path("/publish/{address}")
    public ServerResponse<ByteBuffer> publishMessage(@PathParam("address") String address, TestUser user) {
        try {
            eventBusService.publish(address, user);
            logger.info("Published message to address: {}", address);
            
            return response("Message published to address: " + address).textPlain();
                
        } catch (Exception e) {
            logger.error("Failed to publish message", e);
            return response("Failed to publish message: " + e.getMessage()).status(500).textPlain();
        }
    }
    
    /**
     * Sends a message to one consumer (point-to-point pattern).
     * 
     * Example: POST /eventbus/send/user.validate
     * Body: {"id": 1, "name": "John Doe", "email": "john@example.com"}
     */
    @POST
    @Path("/send/{address}")
    public ServerResponse<ByteBuffer> sendMessage(@PathParam("address") String address, TestUser user) {
        try {
            eventBusService.send(address, user);
            logger.info("Sent message to address: {}", address);
            
            return response("Message sent to address: " + address).textPlain();
                
        } catch (Exception e) {
            logger.error("Failed to send message", e);
            return response("Failed to send message: " + e.getMessage()).status(500).textPlain();
        }
    }
    
    /**
     * Sends a request and waits for a reply (request/reply pattern).
     * 
     * Example: POST /eventbus/request/user.validate
     * Body: {"id": 1, "name": "John Doe", "email": "john@example.com"}
     */
    @POST
    @Path("/request/{address}")
    public CompletableFuture<ServerResponse<ValidationResult>> requestReply(@PathParam("address") String address, TestUser user) {
        return eventBusService.request(address, user, ValidationResult.class)
            .thenApply(result -> {
                logger.info("Received reply from address: {}", address);
                return response(result).applicationJson();
            })
            .exceptionally(throwable -> {
                logger.error("Request failed for address: {}", address, throwable);
                return response(new ValidationResult(false, "Request failed: " + throwable.getMessage())).status(500).applicationJson();
            });
    }
    
    /**
     * Gets event bus metrics.
     */
    @GET
    @Path("/metrics")
    public ServerResponse<?> getMetrics() {
        try {
            return response(eventBusService.getMetrics()).applicationJson();
        } catch (Exception e) {
            logger.error("Failed to get metrics", e);
            return response("Failed to get metrics: " + e.getMessage()).status(500).textPlain();
        }
    }
    
    // Event Consumers using @ConsumeEvent annotation
    
    /**
     * Consumes user creation events (fire-and-forget).
     */
    @ConsumeEvent("user.created")
    public void handleUserCreated(Message<TestUser> message) {
        TestUser user = message.body();
        logger.info("User created: {}", user);
        
        // Simulate some processing
        try {
            Thread.sleep(100);
            logger.info("Processed user creation for: {}", user.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("User creation processing interrupted", e);
        }
    }
    
    /**
     * Consumes user validation requests with reply (request/reply).
     */
    @ConsumeEvent(value = "user.validate", blocking = true)
    public ValidationResult handleUserValidation(Message<TestUser> message) {
        TestUser user = message.body();
        logger.info("Validating user: {}", user);
        
        // Simulate validation logic
        boolean isValid = user.getName() != null && user.getEmail() != null 
                         && user.getEmail().contains("@");
        
        ValidationResult result = new ValidationResult(isValid, 
            isValid ? "User is valid" : "User validation failed");
        
        logger.info("User validation result for {}: {}", user.getName(), result.isValid());
        return result;
    }
    
    /**
     * Consumes user update events asynchronously.
     */
    @ConsumeEvent(value = "user.updated", blocking = false)
    public CompletableFuture<Void> handleUserUpdated(Message<TestUser> message) {
        TestUser user = message.body();
        logger.info("User updated: {}", user);
        
        return CompletableFuture.runAsync(() -> {
            // Simulate async processing
            try {
                Thread.sleep(200);
                logger.info("Async processing completed for user: {}", user.getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("User update processing interrupted", e);
            }
        });
    }
    
    /**
     * Test user model for event bus demonstration.
     */
    public static class TestUser {
        private Long id;
        private String name;
        private String email;
        
        public TestUser() {}
        
        public TestUser(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }
        
        public Long getId() {
            return id;
        }
        
        public void setId(Long id) {
            this.id = id;
        }
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        @Override
        public String toString() {
            return "TestUser{id=" + id + ", name='" + name + "', email='" + email + "'}";
        }
    }
    
    /**
     * Result class for validation responses.
     */
    public static class ValidationResult {
        private boolean valid;
        private String message;
        
        public ValidationResult() {}
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public void setValid(boolean valid) {
            this.valid = valid;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        @Override
        public String toString() {
            return "ValidationResult{valid=" + valid + ", message='" + message + "'}";
        }
    }
}
