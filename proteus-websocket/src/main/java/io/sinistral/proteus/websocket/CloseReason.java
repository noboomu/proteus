package io.sinistral.proteus.websocket;

/**
 * WebSocket close status code and reason.
 *
 * @param code a WebSocket close status code
 * @param reason UTF-8 text of at most 123 bytes
 */
public record CloseReason(int code, String reason) {
}
