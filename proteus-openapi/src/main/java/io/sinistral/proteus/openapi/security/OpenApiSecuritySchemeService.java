package io.sinistral.proteus.openapi.security;

import io.sinistral.proteus.security.jwt.JwtConfiguration;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Service that automatically configures OpenAPI security schemes based on JWT configuration.
 * This service integrates the JWT security framework with OpenAPI documentation generation.
 * 
 * @since 1.0
 */
@Singleton
public class OpenApiSecuritySchemeService {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenApiSecuritySchemeService.class);
    
    // Standard security scheme names
    public static final String JWT_BEARER_SCHEME = "bearerAuth";
    public static final String API_KEY_SCHEME = "apiKeyAuth";
    public static final String OAUTH2_SCHEME = "oauth2Auth";
    
    private final JwtConfiguration jwtConfiguration;
    
    @Inject
    public OpenApiSecuritySchemeService(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }
    
    /**
     * Configures security schemes in the OpenAPI specification based on JWT configuration.
     * This method automatically adds appropriate security schemes when JWT is configured.
     * 
     * @param openApi The OpenAPI specification to configure
     */
    public void configureSecuritySchemes(OpenAPI openApi) {
        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }
        
        Map<String, SecurityScheme> securitySchemes = openApi.getComponents().getSecuritySchemes();
        
        // Add JWT Bearer authentication if JWT is configured
        if (isJwtConfigured()) {
            SecurityScheme jwtScheme = createJwtBearerScheme();
            if (securitySchemes != null) {
                securitySchemes.put(JWT_BEARER_SCHEME, jwtScheme);
            } else {
                openApi.getComponents().addSecuritySchemes(JWT_BEARER_SCHEME, jwtScheme);
            }
            
            logger.info("Added JWT Bearer security scheme to OpenAPI specification");
        }
        
        // Add API Key scheme as an alternative
        SecurityScheme apiKeyScheme = createApiKeyScheme();
        if (securitySchemes != null) {
            securitySchemes.put(API_KEY_SCHEME, apiKeyScheme);
        } else {
            openApi.getComponents().addSecuritySchemes(API_KEY_SCHEME, apiKeyScheme);
        }
        
        logger.info("Added API Key security scheme to OpenAPI specification");
    }
    
    /**
     * Creates a global security requirement for the OpenAPI specification.
     * This makes JWT authentication the default requirement for all endpoints.
     * 
     * @param openApi The OpenAPI specification to configure
     */
    public void configureGlobalSecurity(OpenAPI openApi) {
        if (isJwtConfigured()) {
            SecurityRequirement jwtRequirement = new SecurityRequirement();
            jwtRequirement.addList(JWT_BEARER_SCHEME);
            
            openApi.addSecurityItem(jwtRequirement);
            
            logger.info("Added global JWT security requirement to OpenAPI specification");
        }
    }
    
    /**
     * Creates a JWT Bearer token security scheme.
     * 
     * @return The configured SecurityScheme
     */
    private SecurityScheme createJwtBearerScheme() {
        SecurityScheme scheme = new SecurityScheme();
        scheme.setType(SecurityScheme.Type.HTTP);
        scheme.setScheme("bearer");
        scheme.setBearerFormat("JWT");
        scheme.setDescription("JWT Bearer token authentication. " +
                            "Provide a valid JWT token in the Authorization header as 'Bearer {token}'. " +
                            "The token should contain appropriate roles and claims for authorization.");
        
        // Add extensions for OpenAPI 3.1 features
        scheme.addExtension("x-tokenInfoFunc", "proteus.security.jwt.validateToken");
        scheme.addExtension("x-scopeValidateFunc", "proteus.security.jwt.validateScopes");
        
        return scheme;
    }
    
    /**
     * Creates an API Key security scheme as an alternative to JWT.
     * 
     * @return The configured SecurityScheme
     */
    private SecurityScheme createApiKeyScheme() {
        SecurityScheme scheme = new SecurityScheme();
        scheme.setType(SecurityScheme.Type.APIKEY);
        scheme.setIn(SecurityScheme.In.HEADER);
        scheme.setName("X-API-Key");
        scheme.setDescription("API Key authentication. " +
                            "Provide a valid API key in the X-API-Key header.");
        
        return scheme;
    }
    
    /**
     * Creates an OAuth2 security scheme for more complex authentication flows.
     * 
     * @return The configured SecurityScheme
     */
    private SecurityScheme createOAuth2Scheme() {
        SecurityScheme scheme = new SecurityScheme();
        scheme.setType(SecurityScheme.Type.OAUTH2);
        scheme.setDescription("OAuth2 authentication with various flows supported.");
        
        // Add OAuth2 flows - this would be configured based on requirements
        io.swagger.v3.oas.models.security.OAuthFlows flows = new io.swagger.v3.oas.models.security.OAuthFlows();
        
        // Authorization Code flow
        io.swagger.v3.oas.models.security.OAuthFlow authCodeFlow = new io.swagger.v3.oas.models.security.OAuthFlow();
        authCodeFlow.setAuthorizationUrl("https://auth.example.com/oauth/authorize");
        authCodeFlow.setTokenUrl("https://auth.example.com/oauth/token");
        
        // Add scopes using the scopes map
        io.swagger.v3.oas.models.security.Scopes scopes = new io.swagger.v3.oas.models.security.Scopes();
        scopes.put("read", "Read access to resources");
        scopes.put("write", "Write access to resources");
        scopes.put("admin", "Administrative access");
        authCodeFlow.setScopes(scopes);
        flows.setAuthorizationCode(authCodeFlow);
        
        // Client Credentials flow
        io.swagger.v3.oas.models.security.OAuthFlow clientCredentialsFlow = new io.swagger.v3.oas.models.security.OAuthFlow();
        clientCredentialsFlow.setTokenUrl("https://auth.example.com/oauth/token");
        
        io.swagger.v3.oas.models.security.Scopes clientScopes = new io.swagger.v3.oas.models.security.Scopes();
        clientScopes.put("api", "API access");
        clientCredentialsFlow.setScopes(clientScopes);
        flows.setClientCredentials(clientCredentialsFlow);
        
        scheme.setFlows(flows);
        return scheme;
    }
    
    /**
     * Checks if JWT is properly configured.
     * 
     * @return true if JWT configuration is available and valid
     */
    private boolean isJwtConfigured() {
        try {
            // Check if we have any signing keys or if signature verification is disabled
            return !jwtConfiguration.getRsaPublicKeys().isEmpty() ||
                   !jwtConfiguration.getEcPublicKeys().isEmpty() ||
                   !jwtConfiguration.getHmacSecrets().isEmpty() ||
                   !jwtConfiguration.isSignatureVerificationRequired();
        } catch (Exception e) {
            logger.debug("JWT configuration check failed", e);
            return false;
        }
    }
    
    /**
     * Gets the configured JWT issuers for documentation purposes.
     * 
     * @return List of allowed issuers or empty list if not configured
     */
    public List<String> getAllowedIssuers() {
        try {
            return jwtConfiguration.getAllowedIssuers();
        } catch (Exception e) {
            logger.debug("Failed to get allowed issuers", e);
            return List.of();
        }
    }
    
    /**
     * Gets the configured JWT audiences for documentation purposes.
     * 
     * @return List of allowed audiences or empty list if not configured
     */
    public List<String> getAllowedAudiences() {
        try {
            return jwtConfiguration.getAllowedAudiences();
        } catch (Exception e) {
            logger.debug("Failed to get allowed audiences", e);
            return List.of();
        }
    }
}
