package io.sinistral.proteus.websocket;

import com.google.inject.Module;
import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.websocket.modules.WebSocketModule;
import io.sinistral.proteus.websocket.processors.WebSocketProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of ProteusApplication that includes WebSocket support.
 * 
 * This class provides a convenient way to create Proteus applications
 * with built-in WebSocket capabilities. It automatically registers the
 * WebSocket module and processes WebSocket endpoints.
 * 
 * Usage:
 * <pre>
 * public class MyApplication {
 *     public static void main(String[] args) {
 *         new WebSocketApplication()
 *             .addController(MyWebSocketController.class)
 *             .start();
 *     }
 * }
 * </pre>
 * 
 * @since 1.0
 */
public class WebSocketApplication extends ProteusApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketApplication.class);
    
    public WebSocketApplication() {
        super();
        // Add WebSocket module during construction
        this.addModule(WebSocketModule.class);
        logger.info("WebSocket support enabled");
    }
    
    public WebSocketApplication(String configFile) {
        super(configFile);
        this.addModule(WebSocketModule.class);
        logger.info("WebSocket support enabled");
    }
    
    /**
     * Processes controllers to discover and register WebSocket endpoints.
     * This method should be called after adding all controllers but before starting the server.
     */
    public WebSocketApplication processWebSocketEndpoints() {
        try {
            WebSocketProcessor webSocketProcessor = injector.getInstance(WebSocketProcessor.class);
            
            for (Class<?> controllerClass : registeredControllers) {
                try {
                    Object controllerInstance = injector.getInstance(controllerClass);
                    webSocketProcessor.processInstance(controllerInstance);
                } catch (Exception e) {
                    logger.debug("Could not process controller {} for WebSocket endpoints: {}", 
                               controllerClass.getSimpleName(), e.getMessage());
                }
            }
            
            logger.info("WebSocket endpoints processed");
        } catch (Exception e) {
            logger.debug("WebSocket endpoint processing skipped (WebSocket not enabled): {}", e.getMessage());
        }
        
        return this;
    }
}
