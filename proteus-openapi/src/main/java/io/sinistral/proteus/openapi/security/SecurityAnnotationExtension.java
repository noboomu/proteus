package io.sinistral.proteus.openapi.security;

import io.sinistral.proteus.openapi.jaxrs2.AbstractOpenAPIExtension;
import io.sinistral.proteus.openapi.jaxrs2.OpenAPIExtension;
import io.sinistral.proteus.openapi.models.Operation;
import io.sinistral.proteus.openapi.models.security.SecurityRequirement;
import io.sinistral.proteus.security.SecurityPolicy;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Documents the effective Proteus endpoint security policy.
 * This extension delegates to {@link SecurityPolicy#resolve(Method)} so the OpenAPI operation
 * uses the same method-over-class and claim-fallback precedence as the runtime handler.
 *
 * @since 0.9.5
 */
public class SecurityAnnotationExtension extends AbstractOpenAPIExtension {

    private static final Logger logger = LoggerFactory.getLogger(
        SecurityAnnotationExtension.class
    );

    /** Creates an extension that documents the effective Proteus endpoint policy. */
    public SecurityAnnotationExtension() {}

    @Override
    public void decorateOperation(
        Operation operation,
        Method method,
        Iterator<OpenAPIExtension> chain
    ) {
        processSecurityPolicy(operation, method);
        if (chain.hasNext()) {
            chain.next().decorateOperation(operation, method, chain);
        }
    }

    private void processSecurityPolicy(Operation operation, Method method) {
        SecurityPolicy.Requirement requirement = SecurityPolicy.resolve(method);
        switch (requirement.access()) {
            case DENY_ALL -> {
                operation.addExtension("x-security-forbidden", true);
                operation.setDescription(
                    appendSecurityDescription(
                        operation.getDescription(),
                        "This endpoint is forbidden and not accessible via API."
                    )
                );
            }
            case PERMIT_ALL -> {
                operation.addExtension("x-security-public", true);
                operation.setDescription(
                    appendSecurityDescription(
                        operation.getDescription(),
                        "This endpoint is publicly accessible and requires no authentication."
                    )
                );
            }
            case ROLES -> {
                addBearerRequirement(operation);
                String[] roles = requirement.roles().toArray(String[]::new);
                operation.addExtension("x-required-roles", roles);
                operation.setDescription(
                    appendSecurityDescription(
                        operation.getDescription(),
                        "This endpoint requires authentication and one of the following roles: " +
                        String.join(", ", roles)
                    )
                );
            }
            case AUTHENTICATED -> addBearerRequirement(operation);
            case NONE -> {
                // No Proteus security policy applies.
            }
        }
        logger.debug(
            "Documented {} security policy for endpoint {}",
            requirement.access(),
            method.getName()
        );
    }

    private void addBearerRequirement(Operation operation) {
        SecurityRequirement jwtRequirement = new SecurityRequirement();
        jwtRequirement.addList(OpenApiSecuritySchemeService.JWT_BEARER_SCHEME);
        operation.addSecurityItem(jwtRequirement);
    }

    private String appendSecurityDescription(
        String existingDescription,
        String securityDescription
    ) {
        if (existingDescription == null || existingDescription.isBlank()) {
            return securityDescription;
        }
        return existingDescription + "\n\n" + securityDescription;
    }
}
