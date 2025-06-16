package io.sinistral.proteus.server.handlers.modern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for basic ModernHandlerGenerator functionality.
 * Focus on getting the basics working before adding advanced features.
 */
public class SimpleModernHandlerGeneratorTest {
    
    private static final Logger log = LoggerFactory.getLogger(SimpleModernHandlerGeneratorTest.class);
    
    private ModernHandlerConfig config;
    private SimpleModernHandlerGenerator generator;
    
    @BeforeEach
    void setUp() {
        config = ModernHandlerConfig.testConfig();
        
        generator = new SimpleModernHandlerGenerator(
            "io.sinistral.proteus.test.generated", 
            TestController.class, 
            config
        );
        
        // Initialize the fields that would normally be injected by Guice
        try {
            var applicationPathField = SimpleModernHandlerGenerator.class.getSuperclass().getDeclaredField("applicationPath");
            applicationPathField.setAccessible(true);
            applicationPathField.set(generator, "/test");
            
            var registeredEndpointsField = SimpleModernHandlerGenerator.class.getSuperclass().getDeclaredField("registeredEndpoints");
            registeredEndpointsField.setAccessible(true);
            registeredEndpointsField.set(generator, new java.util.HashSet<>());
            
            var registeredHandlerWrappersField = SimpleModernHandlerGenerator.class.getSuperclass().getDeclaredField("registeredHandlerWrappers");
            registeredHandlerWrappersField.setAccessible(true);
            registeredHandlerWrappersField.set(generator, new java.util.HashMap<>());
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize test fields", e);
        }
    }
    
    @Test
    @DisplayName("Should generate source code successfully")
    void shouldGenerateSourceCode() throws Exception {
        String sourceCode = generator.generateClassSource();
        
        assertNotNull(sourceCode, "Generated source code should not be null");
        assertFalse(sourceCode.isEmpty(), "Generated source code should not be empty");
        
        log.info("Generated source code successfully: {} characters", sourceCode.length());
    }
    
    @Test
    @DisplayName("Should include modern header")
    void shouldIncludeModernHeader() throws Exception {
        String sourceCode = generator.generateClassSource();
        
        assertTrue(sourceCode.contains("GENERATED CODE - DO NOT EDIT (Modern Version)"), 
                   "Should include modern header");
        assertTrue(sourceCode.contains("Proteus Modern HandlerGenerator"), 
                   "Should reference modern generator");
        assertTrue(sourceCode.contains("Generation time:"), 
                   "Should include generation timestamp");
        
        log.info("Modern header validation passed");
    }
    
    @Test
    @DisplayName("Should implement basic caching")
    void shouldImplementBasicCaching() throws Exception {
        // Clear any existing cache
        SimpleModernHandlerGenerator.clearCache();
        
        // First generation
        long start1 = System.nanoTime();
        String sourceCode1 = generator.generateClassSource();
        long duration1 = System.nanoTime() - start1;
        
        // Second generation (should be from cache)
        long start2 = System.nanoTime();
        String sourceCode2 = generator.generateClassSource();
        long duration2 = System.nanoTime() - start2;
        
        // Verify results
        assertEquals(sourceCode1, sourceCode2, "Cached source should be identical");
        assertTrue(duration2 < duration1, "Second generation should be faster (cached)");
        
        log.info("Caching test passed - first: {}μs, second: {}μs", 
                 duration1 / 1000, duration2 / 1000);
    }
    
    @Test
    @DisplayName("Should respect caching configuration")
    void shouldRespectCachingConfig() throws Exception {
        // Test with caching disabled
        ModernHandlerConfig noCacheConfig = new ModernHandlerConfig.Builder()
                .enableCaching(false)
                .debugMode(true)
                .build();
        
        SimpleModernHandlerGenerator noCacheGenerator = new SimpleModernHandlerGenerator(
            "io.sinistral.proteus.test.generated", 
            TestController.class, 
            noCacheConfig
        );
        
        // Clear cache
        SimpleModernHandlerGenerator.clearCache();
        
        // Generate twice
        String source1 = noCacheGenerator.generateClassSource();
        String source2 = noCacheGenerator.generateClassSource();
        
        // Should be different timestamps since caching is disabled
        assertNotEquals(source1, source2, "Without caching, sources should differ (timestamps)");
        
        log.info("Cache configuration test passed");
    }
    
    @Test
    @DisplayName("Should collect basic metrics")
    void shouldCollectBasicMetrics() throws Exception {
        // Clear cache and generate
        SimpleModernHandlerGenerator.clearCache();
        generator.generateClassSource();
        
        var metrics = SimpleModernHandlerGenerator.getMetrics();
        assertNotNull(metrics, "Metrics should not be null");
        assertTrue(metrics.containsKey("cache_size"), "Should track cache size");
        
        log.info("Metrics test passed: {}", metrics);
    }
    
    @Test
    @DisplayName("Should handle controller class correctly")
    void shouldHandleControllerClass() throws Exception {
        String sourceCode = generator.generateClassSource();
        
        // Should contain controller-related content
        assertTrue(sourceCode.contains("TestController"), "Should reference the controller class");
        
        log.info("Controller handling test passed");
    }
    
    // Simple test controller
    @Path("/test")
    public static class TestController {
        
        @GET
        @Path("/hello")
        public String hello(@QueryParam("name") String name) {
            return "Hello " + (name != null ? name : "World");
        }
        
        @POST
        @Path("/data")
        public String postData(String data) {
            return "Received: " + data;
        }
    }
}
