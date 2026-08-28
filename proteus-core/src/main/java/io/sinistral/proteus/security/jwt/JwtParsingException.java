package io.sinistral.proteus.security.jwt;

/**
 * Exception thrown when a JWT token cannot be parsed.
 * This typically indicates malformed or invalid JWT structure.
 *
 * @since 0.9.5
 */
public class JwtParsingException extends RuntimeException {

    /**
     * Constructs a new JWT parsing exception with the specified detail message.
     *
     * @param message The detail message
     */
    public JwtParsingException(String message) {
        super(message);
    }

    /**
     * Constructs a new JWT parsing exception with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The cause
     */
    public JwtParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new JWT parsing exception with the specified cause.
     *
     * @param cause The cause
     */
    public JwtParsingException(Throwable cause) {
        super(cause);
    }
}
