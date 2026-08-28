package io.sinistral.proteus.openapi.security;

import io.sinistral.proteus.security.jwt.JwtConfiguration;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.OpenAPI;
import io.sinistral.proteus.openapi.models.Operation;
import io.sinistral.proteus.openapi.models.PathItem;
import io.sinistral.proteus.openapi.models.security.SecurityRequirement;
import io.sinistral.proteus.openapi.models.security.SecurityScheme;
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
 * <p>Note: The {@code SecurityAnnotationExtension} deliberately delegates to
 * {@code SecurityPolicy.resolve} so documentation and runtime precedence share one resolver.
 *
 * @since 0.9.5
 */
@Singleton
public class OpenApiSecuritySchemeService {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiSecuritySchemeService.class);

    /** Component name used by generated Bearer security requirements. */
    public static final String JWT_BEARER_SCHEME = "bearerAuth";
    private final JwtConfiguration jwtConfiguration;

    /**
     * Creates a scheme service backed by the runtime JWT configuration.
     *
     * @param jwtConfiguration verification configuration used to detect enabled JWT support
     */
    @Inject
    public OpenApiSecuritySchemeService(JwtConfiguration jwtConfiguration) {
        this.jwtConfiguration = jwtConfiguration;
    }

    /**
     * Configures security schemes in the OpenAPI specification based on JWT configuration.
     *
     * <p>Add bearerAuth when verification is configured or when any generated operation
     * references it. The latter keeps the OpenAPI document internally complete and is not
     * an assertion that runtime verification keys are available.
     *
     * <p>If a scheme already exists with the name "bearerAuth", it is unconditionally overwritten.
     *
     * @param openApi The OpenAPI specification to configure (mutated by this method)
     */
    public void configureSecuritySchemes(OpenAPI openApi) {
        if (openApi.getComponents() == null) {
            openApi.setComponents(new Components());
        }

        Map<String, SecurityScheme> securitySchemes = openApi.getComponents().getSecuritySchemes();

        if (!isJwtConfigured() && !referencesBearerScheme(openApi)) {
            return;
        }

        SecurityScheme jwtScheme = createJwtBearerScheme();
        if (securitySchemes != null) {
            securitySchemes.put(JWT_BEARER_SCHEME, jwtScheme);
        } else {
            openApi.getComponents().addSecuritySchemes(
                JWT_BEARER_SCHEME,
                jwtScheme
            );
        }

        logger.info("Added JWT Bearer security scheme to OpenAPI specification");
    }

    private boolean referencesBearerScheme(OpenAPI openApi) {
        if (referencesBearerScheme(openApi.getSecurity())) {
            return true;
        }
        if (openApi.getPaths() == null) {
            return false;
        }
        return openApi.getPaths().values().stream()
            .flatMap(this::operations)
            .map(Operation::getSecurity)
            .anyMatch(this::referencesBearerScheme);
    }

    private java.util.stream.Stream<Operation> operations(PathItem pathItem) {
        return java.util.stream.Stream.of(
            pathItem.getGet(),
            pathItem.getPut(),
            pathItem.getPost(),
            pathItem.getDelete(),
            pathItem.getOptions(),
            pathItem.getHead(),
            pathItem.getPatch(),
            pathItem.getTrace()
        ).filter(java.util.Objects::nonNull);
    }

    private boolean referencesBearerScheme(
        List<SecurityRequirement> requirements
    ) {
        return requirements != null && requirements.stream()
            .anyMatch(requirement ->
                requirement.containsKey(JWT_BEARER_SCHEME)
            );
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
