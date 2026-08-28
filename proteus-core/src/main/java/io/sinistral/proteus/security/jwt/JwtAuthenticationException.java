package io.sinistral.proteus.security.jwt;

/**
 * General-purpose exception for JWT authentication failures raised by applications or custom
 * integrations. The default Proteus request pipeline reports routine invalid credentials through
 * HTTP status responses and optional results instead of throwing this type.
 *
 * @since 0.9.5
 */
public class JwtAuthenticationException extends RuntimeException {

    /**
     * Constructs a new JWT authentication exception with the specified detail message.
     *
     * @param message The detail message
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new JWT authentication exception with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The cause
     */
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new JWT authentication exception with the specified cause.
     *
     * @param cause The cause
     */
    public JwtAuthenticationException(Throwable cause) {
        super(cause);
    }
}
