package io.sinistral.proteus.security.handlers;

import io.sinistral.proteus.security.SecurityContext;
import io.sinistral.proteus.security.SecurityPolicy;
import io.sinistral.proteus.security.jwt.JwtService;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Security processor that handles JWT authentication and authorization for HTTP requests.
 * A successfully validated context is attached to the {@code HttpServerExchange}; generated
 * controllers receive that same value when they declare a {@code SecurityContext} parameter.
 * The processor never publishes authentication through a thread-local value.
 *
 * @since 0.9.5
 */
public class SecurityProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SecurityProcessor.class);
    private static final String SECURITY_CONTEXT_ATTRIBUTE = "proteus.security.context";

    private final JwtService jwtService;

    /**
     * Creates a request security processor backed by the supplied JWT service.
     *
     * @param jwtService token extraction and validation service
     */
    public SecurityProcessor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Creates a security-aware HTTP handler that wraps the original handler.
     * This method analyzes the target method's security annotations and applies
     * appropriate authentication and authorization checks.
     *
     * @param originalHandler The original HTTP handler
     * @param targetMethod The target method to be secured
     * @return A wrapped handler with security enforcement
     */
    public HttpHandler createSecureHandler(HttpHandler originalHandler, Method targetMethod) {
        SecurityPolicy.Requirement requirement = SecurityPolicy.resolve(targetMethod);

        return exchange -> {
            if (requirement.access() == SecurityPolicy.Access.DENY_ALL) {
                sendForbidden(exchange, "Access denied");
                return;
            }

            Optional<SecurityContext> securityContext = extractSecurityContext(exchange);

            securityContext.ifPresent(context ->
                exchange.putAttachment(SecurityContextAttachment.KEY, context));

            if (requirement.requiresAuthentication() && securityContext.isEmpty()) {
                sendUnauthorized(exchange, "Authentication required");
                return;
            }

            if (
                requirement.access() == SecurityPolicy.Access.ROLES &&
                !securityContext.get().hasAnyRole(
                    requirement.roles().toArray(String[]::new)
                )
            ) {
                sendForbidden(exchange, "Insufficient privileges");
                return;
            }

            originalHandler.handleRequest(exchange);
        };
    }

    /**
     * Resolves an endpoint method and wraps its handler with the effective security policy.
     *
     * @param originalHandler handler to invoke after security processing
     * @param targetClass controller class declaring the endpoint
     * @param methodName endpoint method name
     * @param parameterTypes exact endpoint parameter types
     * @return security-aware handler
     * @throws IllegalArgumentException if the endpoint method cannot be resolved
     */
    public HttpHandler createSecureHandler(
        HttpHandler originalHandler,
        Class<?> targetClass,
        String methodName,
        Class<?>... parameterTypes
    ) {
        try {
            return createSecureHandler(
                originalHandler,
                targetClass.getDeclaredMethod(methodName, parameterTypes)
            );
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                "Unable to resolve secured endpoint " +
                targetClass.getName() + "." + methodName,
                e
            );
        }
    }

    /**
     * Extracts security context from the HTTP request.
     * This method looks for JWT tokens in the Authorization header.
     *
     * @param exchange The HTTP server exchange
     * @return Security context if authentication is successful, empty otherwise
     */
    public Optional<SecurityContext> extractSecurityContext(HttpServerExchange exchange) {
        String authHeader = exchange.getRequestHeaders().getFirst(Headers.AUTHORIZATION);
        if (authHeader == null || authHeader.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            return jwtService.processAuthorizationHeader(authHeader);
        } catch (Exception e) {
            logger.debug("Failed to process authorization header", e);
            return Optional.empty();
        }
    }

    /**
     * Gets the validated security context attached to an exchange during security processing.
     * This is the authoritative request-scoped context for both synchronous and asynchronous
     * controller code; callers that create asynchronous work must retain it explicitly.
     *
     * @param exchange The HTTP server exchange
     * @return Security context if available, empty otherwise
     */
    public static Optional<SecurityContext> getSecurityContext(HttpServerExchange exchange) {
        return Optional.ofNullable(exchange.getAttachment(SecurityContextAttachment.KEY));
    }

    /**
     * Reads and converts one scalar claim from the exchange-attached security context.
     *
     * @param <T> requested scalar type
     * @param exchange current HTTP exchange
     * @param claimName JWT claim name
     * @param targetType requested scalar class
     * @param defaultValue fallback used only for missing string claims
     * @param required whether a missing claim is an error
     * @return converted value, the configured string fallback, or {@code null}
     * @throws IllegalArgumentException if a required claim is absent or conversion fails
     */
    public static <T> T getClaim(
        HttpServerExchange exchange,
        String claimName,
        Class<T> targetType,
        String defaultValue,
        boolean required
    ) {
        Optional<Object> claim = rawClaim(exchange, claimName);
        if (claim.isEmpty()) {
            if (targetType == String.class && !defaultValue.isEmpty()) {
                return targetType.cast(defaultValue);
            }
            if (required) {
                throw new IllegalArgumentException(
                    "Required JWT claim is missing: " + claimName
                );
            }
            return null;
        }
        return convertClaim(claim.get(), targetType, claimName);
    }

    /**
     * Reads and converts an optional scalar claim.
     *
     * @param <T> requested scalar type
     * @param exchange current HTTP exchange
     * @param claimName JWT claim name
     * @param targetType requested scalar class
     * @return converted value or an empty optional when the claim is absent
     * @throws IllegalArgumentException if conversion fails
     */
    public static <T> Optional<T> getOptionalClaim(
        HttpServerExchange exchange,
        String claimName,
        Class<T> targetType
    ) {
        return rawClaim(exchange, claimName)
            .map(value -> convertClaim(value, targetType, claimName));
    }

    /**
     * Reads and converts a collection-valued claim into an immutable list.
     *
     * @param <T> requested element type
     * @param exchange current HTTP exchange
     * @param claimName JWT claim name
     * @param elementType requested element class
     * @param required whether a missing claim is an error
     * @return immutable converted list, or an empty list when an optional claim is absent
     * @throws IllegalArgumentException if the claim is absent, is not a collection, or conversion fails
     */
    public static <T> List<T> getClaimList(
        HttpServerExchange exchange,
        String claimName,
        Class<T> elementType,
        boolean required
    ) {
        Optional<Object> claim = rawClaim(exchange, claimName);
        if (claim.isEmpty()) {
            if (required) {
                throw new IllegalArgumentException(
                    "Required JWT claim is missing: " + claimName
                );
            }
            return List.of();
        }
        if (!(claim.get() instanceof Collection<?> values)) {
            throw new IllegalArgumentException(
                "JWT claim " + claimName + " is not a collection"
            );
        }
        List<T> converted = new ArrayList<>(values.size());
        for (Object value : values) {
            converted.add(convertClaim(value, elementType, claimName));
        }
        return List.copyOf(converted);
    }

    private static Optional<Object> rawClaim(
        HttpServerExchange exchange,
        String claimName
    ) {
        return getSecurityContext(exchange)
            .flatMap(context -> context.getAttribute(claimName));
    }

    @SuppressWarnings("unchecked")
    private static <T> T convertClaim(
        Object value,
        Class<T> targetType,
        String claimName
    ) {
        if (targetType.isInstance(value)) {
            return targetType.cast(value);
        }
        if (targetType == String.class) {
            return (T) String.valueOf(value);
        }
        if (targetType == Long.class || targetType == long.class) {
            Long converted;
            if (value instanceof Number number) {
                converted = number.longValue();
            } else if (value instanceof Date date) {
                converted = date.toInstant().getEpochSecond();
            } else if (value instanceof Instant instant) {
                converted = instant.getEpochSecond();
            } else {
                converted = Long.valueOf(String.valueOf(value));
            }
            return (T) converted;
        }
        if (targetType == Boolean.class || targetType == boolean.class) {
            return (T) Boolean.valueOf(String.valueOf(value));
        }
        throw new IllegalArgumentException(
            "JWT claim " + claimName + " cannot be converted from " +
            value.getClass().getName() + " to " + targetType.getName()
        );
    }


    private void sendUnauthorized(HttpServerExchange exchange, String message) {
        exchange.setStatusCode(StatusCodes.UNAUTHORIZED);
        exchange.getResponseHeaders().put(Headers.WWW_AUTHENTICATE, "Bearer");
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");

        String response = String.format("{\"error\":\"unauthorized\",\"message\":\"%s\"}", message);
        exchange.getResponseSender().send(response);
    }

    private void sendForbidden(HttpServerExchange exchange, String message) {
        exchange.setStatusCode(StatusCodes.FORBIDDEN);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");

        String response = String.format("{\"error\":\"forbidden\",\"message\":\"%s\"}", message);
        exchange.getResponseSender().send(response);
    }
}
