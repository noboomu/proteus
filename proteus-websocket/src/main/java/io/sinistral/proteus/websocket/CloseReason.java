package io.sinistral.proteus.websocket;

/**
 * Represents the reason why a WebSocket connection was closed.
 * 
 * @since 1.0
 */
public class CloseReason {
    
    public static final int NORMAL_CLOSURE = 1000;
    public static final int GOING_AWAY = 1001;
    public static final int PROTOCOL_ERROR = 1002;
    public static final int UNSUPPORTED_DATA = 1003;
    public static final int NO_STATUS_CODE = 1005;
    public static final int ABNORMAL_CLOSURE = 1006;
    public static final int INVALID_FRAME_PAYLOAD_DATA = 1007;
    public static final int POLICY_VIOLATION = 1008;
    public static final int MESSAGE_TOO_BIG = 1009;
    public static final int MANDATORY_EXTENSION = 1010;
    public static final int INTERNAL_SERVER_ERROR = 1011;
    public static final int SERVICE_RESTART = 1012;
    public static final int TRY_AGAIN_LATER = 1013;
    public static final int BAD_GATEWAY = 1014;
    public static final int TLS_HANDSHAKE_FAILURE = 1015;
    
    private final int code;
    private final String reason;
    
    public CloseReason(int code, String reason) {
        this.code = code;
        this.reason = reason != null ? reason : "";
    }
    
    /**
     * Gets the close code.
     * 
     * @return the close code
     */
    public int getCode() {
        return code;
    }
    
    /**
     * Gets the close reason message.
     * 
     * @return the close reason
     */
    public String getReason() {
        return reason;
    }
    
    /**
     * Checks if this is a normal closure.
     * 
     * @return true if code is 1000 (normal closure)
     */
    public boolean isNormalClosure() {
        return code == NORMAL_CLOSURE;
    }
    
    /**
     * Checks if this is an error closure.
     * 
     * @return true if code indicates an error
     */
    public boolean isError() {
        return code >= PROTOCOL_ERROR && code != NORMAL_CLOSURE && code != GOING_AWAY;
    }
    
    @Override
    public String toString() {
        return "CloseReason{" +
               "code=" + code +
               ", reason='" + reason + '\'' +
               '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        CloseReason that = (CloseReason) o;
        
        if (code != that.code) return false;
        return reason.equals(that.reason);
    }
    
    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + reason.hashCode();
        return result;
    }
}
