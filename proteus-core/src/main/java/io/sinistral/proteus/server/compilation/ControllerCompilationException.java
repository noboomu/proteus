package io.sinistral.proteus.server.compilation;

/**
 * Reports an unrecoverable generated-controller compilation failure.
 */
public class ControllerCompilationException extends RuntimeException {

    /**
     * Creates an exception with compiler diagnostics.
     *
     * @param message compilation failure description
     */
    public ControllerCompilationException(String message) {
        super(message);
    }

    /**
     * Creates an exception with compiler diagnostics and a cause.
     *
     * @param message compilation failure description
     * @param cause underlying compiler or class-loading failure
     */
    public ControllerCompilationException(String message, Throwable cause) {
        super(message, cause);
    }
}
