package io.sinistral.proteus.websocket.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import io.sinistral.proteus.websocket.WebSocketService;
import io.sinistral.proteus.websocket.DefaultWebSocketService;
import io.sinistral.proteus.websocket.processors.WebSocketProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Guice module for WebSocket support in Proteus applications.
 * 
 * This module provides:
 * - WebSocket service for endpoint registration and management
 * - WebSocket processor for annotation-driven endpoint discovery
 * - Integration with Proteus security and virtual thread features
 * 
 * @since 1.0
 */
@Singleton
public class WebSocketModule extends AbstractModule {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketModule.class);
    
    private final Config config;
    
    public WebSocketModule(Config config) {
        this.config = config;
    }
    
    @Override
    protected void configure() {
        // Check if WebSocket support is enabled
        if (config.hasPath("proteus.websockets.enabled") && config.getBoolean("proteus.websockets.enabled")) {
            
            // Bind WebSocket service
            this.bind(WebSocketService.class).to(DefaultWebSocketService.class).in(Singleton.class);
            
            // Bind WebSocket processor
            this.bind(WebSocketProcessor.class).in(Singleton.class);
            
            logger.info("WebSocket support enabled");
        } else {
            logger.info("WebSocket support disabled in configuration");
        }
    }
}
