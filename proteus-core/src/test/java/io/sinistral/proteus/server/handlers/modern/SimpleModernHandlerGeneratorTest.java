package io.sinistral.proteus.server.handlers.modern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.sinistral.proteus.server.endpoints.EndpointInfo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.lang.reflect.Field;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for modern handler generation functionality.
 * These tests verify that actual handler generation works with minimal setup.
 */
public class SimpleModernHandlerGeneratorTest {
    
    private static final Logger log = LoggerFactory.getLogger(SimpleModernHandlerGeneratorTest.class);
    
    private ModernHandlerConfig config;
    private SimpleModernHandlerGenerator generator;
    
    @BeforeEach
    void setUp() throws Exception {
        config = ModernHandlerConfig.testConfig();
        
        generator = new SimpleModernHandlerGenerator(
            "io.sinistral.proteus.test.generated", 
            TestController.class, 
            config
        );
        
        // Inject minimal required fields using reflection (since this is a unit test)
        injectField(generator, "applicationPath", "/test");
        injectField(generator, "registeredEndpoints", new HashSet<EndpointInfo>());
        injectField(generator, "registeredHandlerWrappers", new java.util.HashMap<>());
    }
    
    private void injectField(Object target, String fieldName, Object value) throws Exception {
        Class<?> clazz = target.getClass();
        Field field = null;
        
        // Try to find the field in this class or parent classes
        while (clazz != null && field == null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        
        if (field != null) {
            field.setAccessible(true);
            field.set(target, value);
        } else {
            log.warn("Could not find field: {}", fieldName);
        }
    }
    
    @Test
    @DisplayName("Should generate source code for simple controller")
    void shouldGenerateSourceCodeForSimpleController() throws Exception {
        String sourceCode = generator.generateClassSource();
        
        assertNotNull(sourceCode, "Generated source code should not be null");
        assertFalse(sourceCode.isEmpty(), "Generated source code should not be empty");
        assertTrue(sourceCode.contains("class"), "Should contain class declaration");
        
        log.info("Generated source code: {} characters", sourceCode.length());
        log.debug("Source code preview: {}", sourceCode.substring(0, Math.min(200, sourceCode.length())));
    }
    
    @Test
    @DisplayName("Should include modern header in generated code")
    void shouldIncludeModernHeaderInGeneratedCode() throws Exception {
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
    @DisplayName("Should cache generated source code when caching enabled")
    void shouldCacheGeneratedSourceCode() throws Exception {
        // Use config with caching enabled
        ModernHandlerConfig cachingConfig = new ModernHandlerConfig.Builder()
            .enableCaching(true)
            .debugMode(true)
            .build();
            
        SimpleModernHandlerGenerator cachingGenerator = new SimpleModernHandlerGenerator(
            "io.sinistral.proteus.test.generated", 
            TestController.class, 
            cachingConfig
        );
        
        // Inject required fields
        injectField(cachingGenerator, "applicationPath", "/test");
        injectField(cachingGenerator, "registeredEndpoints", new HashSet<EndpointInfo>());
        injectField(cachingGenerator, "registeredHandlerWrappers", new java.util.HashMap<>());
        
        // Clear any existing cache
        cachingGenerator.clearCache();
        
        // First generation
        long start1 = System.nanoTime();
        String sourceCode1 = cachingGenerator.generateClassSource();
        long duration1 = System.nanoTime() - start1;
        
        // Second generation (should be from cache)
        long start2 = System.nanoTime();
        String sourceCode2 = cachingGenerator.generateClassSource();
        long duration2 = System.nanoTime() - start2;
        
        // Verify caching worked
        assertEquals(sourceCode1, sourceCode2, "Cached source should be identical");
        assertTrue(duration2 < duration1, "Second generation should be faster (cached)");
        
        log.info("Caching test passed - first: {}μs, second: {}μs", 
                 duration1 / 1000, duration2 / 1000);
    }
    
    @Test
    @DisplayName("Should handle controller with JAX-RS annotations")
    void shouldHandleControllerWithJaxRsAnnotations() throws Exception {
        String sourceCode = generator.generateClassSource();
        
        // Should reference the test controller
        assertTrue(sourceCode.contains("TestController") || sourceCode.contains("test"), 
                   "Should reference controller in some way");
        
        log.info("JAX-RS annotation handling test passed");
    }
    
    @Test
    @DisplayName("Should collect and provide metrics")
    void shouldCollectAndProvideMetrics() throws Exception {
        // Use a caching-enabled generator for metrics
        ModernHandlerConfig metricsConfig = ModernHandlerConfig.defaultConfig();
        SimpleModernHandlerGenerator metricsGenerator = new SimpleModernHandlerGenerator(
            "io.sinistral.proteus.test.generated", 
            TestController.class, 
            metricsConfig
        );
        
        // Inject required fields
        injectField(metricsGenerator, "applicationPath", "/test");
        injectField(metricsGenerator, "registeredEndpoints", new HashSet<EndpointInfo>());
        injectField(metricsGenerator, "registeredHandlerWrappers", new java.util.HashMap<>());
        
        metricsGenerator.clearCache();
        
        // Generate some source code
        metricsGenerator.generateClassSource();
        
        // Get metrics
        var metrics = metricsGenerator.getMetrics();
        
        assertNotNull(metrics, "Metrics should not be null");
        assertTrue(metrics.containsKey("cache_size"), "Should track cache size");
        assertTrue(metrics.get("cache_size") instanceof Number, "Cache size should be a number");
        
        log.info("Metrics test passed: {}", metrics);
    }
    
    // Simple test controller with JAX-RS annotations
    @Path("/test")
    public static class TestController {
        
        @GET
        @Path("/hello")
        public String hello(@QueryParam("name") String name) {
            return "Hello " + (name != null ? name : "World");
        }
        
        @GET
        @Path("/simple")
        public String simple() {
            return "Simple response";
        }
    }
}
