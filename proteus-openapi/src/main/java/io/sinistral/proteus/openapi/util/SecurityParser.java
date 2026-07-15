package io.sinistral.proteus.openapi.util;

import io.sinistral.proteus.openapi.models.security.OAuthFlow;
import io.sinistral.proteus.openapi.models.security.OAuthFlows;
import io.sinistral.proteus.openapi.models.security.SecurityRequirement;
import io.sinistral.proteus.openapi.models.security.SecurityScheme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Parser for security-related annotations
 */
public class SecurityParser {

    /**
     * Container for security scheme with its key
     */
    public static class SecuritySchemePair {
        public String key;
        public SecurityScheme securityScheme;

        public SecuritySchemePair(String key, SecurityScheme securityScheme) {
            this.key = key;
            this.securityScheme = securityScheme;
        }
    }

    /**
     * Get SecurityScheme from annotation
     */
    public static Optional<SecuritySchemePair> getSecurityScheme(
        io.swagger.v3.oas.annotations.security.SecurityScheme annotation
    ) {
        if (annotation == null) {
            return Optional.empty();
        }

        SecurityScheme scheme = new SecurityScheme();

        // Map annotation type to model type
        if (annotation.type() != null) {
            switch (annotation.type()) {
                case APIKEY:
                    scheme.setType(SecurityScheme.Type.APIKEY);
                    scheme.setName(annotation.paramName());
                    if (annotation.in() != null) {
                        switch (annotation.in()) {
                            case HEADER:
                                scheme.setIn(SecurityScheme.In.HEADER);
                                break;
                            case QUERY:
                                scheme.setIn(SecurityScheme.In.QUERY);
                                break;
                            case COOKIE:
                                scheme.setIn(SecurityScheme.In.COOKIE);
                                break;
                        }
                    }
                    break;
                case HTTP:
                    scheme.setType(SecurityScheme.Type.HTTP);
                    scheme.setScheme(annotation.scheme());
                    scheme.setBearerFormat(annotation.bearerFormat());
                    break;
                case OAUTH2:
                    scheme.setType(SecurityScheme.Type.OAUTH2);
                    if (annotation.flows() != null) {
                        scheme.setFlows(getOAuthFlows(annotation.flows()));
                    }
                    break;
                case OPENIDCONNECT:
                    scheme.setType(SecurityScheme.Type.OPENIDCONNECT);
                    scheme.setOpenIdConnectUrl(annotation.openIdConnectUrl());
                    break;
                case MUTUALTLS:
                    scheme.setType(SecurityScheme.Type.MUTUALTLS);
                    break;
            }
        }

        scheme.setDescription(annotation.description());

        String key = annotation.name();
        if (key == null || key.isEmpty()) {
            key = "security";
        }

        return Optional.of(new SecuritySchemePair(key, scheme));
    }

    /**
     * Get OAuthFlows from annotation
     */
    private static OAuthFlows getOAuthFlows(io.swagger.v3.oas.annotations.security.OAuthFlows annotation) {
        OAuthFlows flows = new OAuthFlows();

        if (annotation.implicit() != null) {
            OAuthFlow flow = new OAuthFlow();
            flow.setAuthorizationUrl(annotation.implicit().authorizationUrl());
            if (annotation.implicit().scopes() != null) {
                for (io.swagger.v3.oas.annotations.security.OAuthScope scope : annotation.implicit().scopes()) {
                    flow.addScope(scope.name(), scope.description());
                }
            }
            flows.setImplicit(flow);
        }

        if (annotation.password() != null) {
            OAuthFlow flow = new OAuthFlow();
            flow.setTokenUrl(annotation.password().tokenUrl());
            if (annotation.password().scopes() != null) {
                for (io.swagger.v3.oas.annotations.security.OAuthScope scope : annotation.password().scopes()) {
                    flow.addScope(scope.name(), scope.description());
                }
            }
            flows.setPassword(flow);
        }

        if (annotation.clientCredentials() != null) {
            OAuthFlow flow = new OAuthFlow();
            flow.setTokenUrl(annotation.clientCredentials().tokenUrl());
            if (annotation.clientCredentials().scopes() != null) {
                for (io.swagger.v3.oas.annotations.security.OAuthScope scope : annotation.clientCredentials().scopes()) {
                    flow.addScope(scope.name(), scope.description());
                }
            }
            flows.setClientCredentials(flow);
        }

        if (annotation.authorizationCode() != null) {
            OAuthFlow flow = new OAuthFlow();
            flow.setAuthorizationUrl(annotation.authorizationCode().authorizationUrl());
            flow.setTokenUrl(annotation.authorizationCode().tokenUrl());
            if (annotation.authorizationCode().scopes() != null) {
                for (io.swagger.v3.oas.annotations.security.OAuthScope scope : annotation.authorizationCode().scopes()) {
                    flow.addScope(scope.name(), scope.description());
                }
            }
            flows.setAuthorizationCode(flow);
        }

        return flows;
    }

    /**
     * Get SecurityRequirements from annotations
     */
    public static Optional<List<SecurityRequirement>> getSecurityRequirements(
        io.swagger.v3.oas.annotations.security.SecurityRequirement[] annotations
    ) {
        if (annotations == null || annotations.length == 0) {
            return Optional.empty();
        }

        List<SecurityRequirement> requirements = new ArrayList<>();

        for (io.swagger.v3.oas.annotations.security.SecurityRequirement annotation : annotations) {
            SecurityRequirement requirement = new SecurityRequirement();

            String name = annotation.name();
            if (name != null && !name.isEmpty()) {
                List<String> scopes = Arrays.asList(annotation.scopes());
                requirement.addList(name, scopes);
                requirements.add(requirement);
            }
        }

        return requirements.isEmpty() ? Optional.empty() : Optional.of(requirements);
    }
}
