/**
 *
 */
package io.sinistral.proteus.server.exceptions;

import javax.ws.rs.core.Response.Status;

/**
 * @author jbauer
 *
 */
public class ServerException extends RuntimeException
{
    /**
     *
     */
    private static final long serialVersionUID = 8360356916374374408L;

    private Integer status = Status.BAD_REQUEST.getStatusCode();

    public ServerException(int status)
    {
        super();

        this.status = status;
    }

    public ServerException(Status status)
    {
        super();

        this.status = status.getStatusCode();
    }

    /**
     * @param message
     */
    public ServerException(String message, int status)
    {
        super(message);

        this.status = status;
    }

    /**
     * @param message
     */
    public ServerException(String message, Status status)
    {
        super(message);

        this.status = status.getStatusCode();
    }

    /**
     * @param cause
     */
    public ServerException(Throwable cause, int status)
    {
        super(cause);

        this.status = status;
    }

    /**
     * @param cause
     */
    public ServerException(Throwable cause, Status status)
    {
        super(cause);

        this.status = status.getStatusCode();
    }

    public ServerException(String message, Throwable cause, int status)
    {
        super(message, cause);

        this.status = status;
    }

    public ServerException(String message, Throwable cause, Status status)
    {
        super(message, cause);

        this.status = status.getStatusCode();
    }

    /**
     * @return the status
     */
    public Integer getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status)
    {
        this.status = status;
    }
}



