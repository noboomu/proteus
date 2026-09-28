/**
 *
 */
package io.sinistral.proteus.server.exceptions;

import io.undertow.util.StatusCodes;

/**
 * Runtime exception carrying an HTTP status code for generated error responses.
 *
 * @author jbauer
 */
public class ServerException extends RuntimeException
{
    /**
     *
     */
    private static final long serialVersionUID = 8360356916374374408L;

    /** the status. */
    private Integer status = StatusCodes.BAD_REQUEST;

    /** Creates an exception with a status and no message.
     *
     * @param status the HTTP status code */
    public ServerException(int status)
    {
        super();

        this.status = status;
    }

    /**
     * Creates an exception with a message and status.
     *
     * @param message the error message
     * @param status the HTTP status code
     */
    public ServerException(String message, int status)
    {
        super(message);

        this.status = status;
    }


    /**
     * Creates an exception wrapping a cause with a status.
     *
     * @param cause the underlying cause
     * @param status the HTTP status code
     */
    public ServerException(Throwable cause, int status)
    {
        super(cause);

        this.status = status;
    }


    /** Creates an exception with a message, cause, and status.
     *
     * @param message the error message
     * @param cause the underlying cause
     * @param status the HTTP status code */
    public ServerException(String message, Throwable cause, int status)
    {
        super(message, cause);

        this.status = status;
    }

    /**
     * Returns the HTTP status code.
     *
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * Sets the HTTP status code.
     *
     * @param status the status to set
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }
}



