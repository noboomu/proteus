package io.sinistral.proteus.server.http1;

import io.undertow.UndertowOptions;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.BadRequestException;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import io.undertow.util.ParameterLimitException;
import io.undertow.util.Protocols;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.xnio.OptionMap;

/**
 * Resumable HTTP/1.x request-head parser.
 *
 * <p>Consumes bytes from pooled buffers across reads, filling the exchange's method, protocol,
 * path, query, and request headers. Known method, protocol, and header names resolve to the
 * interned {@link HttpString} constants by length and byte compare; nothing is hashed on the
 * hot path. Header values become one Latin-1 {@link String} each.
 */
final class Http1Parser {

    private static final int S_METHOD = 0;
    private static final int S_TARGET = 1;
    private static final int S_VERSION = 2;
    private static final int S_LINE_LF = 3;
    private static final int S_HEADER_START = 4;
    private static final int S_HEADER_NAME = 5;
    private static final int S_HEADER_SP = 6;
    private static final int S_HEADER_VALUE = 7;
    private static final int S_HEADER_LF = 8;
    private static final int S_FINAL_LF = 9;
    private static final int S_DONE = 10;

    private static final byte CR = '\r';
    private static final byte LF = '\n';
    private static final byte SP = ' ';
    private static final byte HT = '\t';
    private static final byte COLON = ':';

    /** Token characters per RFC 9110 5.6.2; indexed by unsigned byte. */
    private static final boolean[] TOKEN = new boolean[256];
    /** Bytes allowed in a field value: VCHAR, obs-text, SP, HTAB. */
    private static final boolean[] FIELD_VALUE = new boolean[256];

    static {
        for (int c = 0; c < 256; c++) {
            boolean alnum = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9');
            TOKEN[c] = alnum || "!#$%&'*+-.^_`|~".indexOf(c) >= 0;
            FIELD_VALUE[c] = (c >= 0x21 && c <= 0x7E) || c >= 0x80 || c == SP || c == HT;
        }
    }

    /** Interned headers bucketed by name length then first byte (case-insensitive). */
    private static final HttpString[][] HEADERS_BY_LENGTH = new HttpString[40][];

    static {
        HttpString[] all = {
            Headers.ACCEPT, Headers.ACCEPT_CHARSET, Headers.ACCEPT_ENCODING, Headers.ACCEPT_LANGUAGE,
            Headers.ACCEPT_RANGES, Headers.AUTHORIZATION, Headers.CACHE_CONTROL, Headers.CONNECTION,
            Headers.CONTENT_DISPOSITION, Headers.CONTENT_ENCODING, Headers.CONTENT_LANGUAGE,
            Headers.CONTENT_LENGTH, Headers.CONTENT_LOCATION, Headers.CONTENT_MD5, Headers.CONTENT_RANGE,
            Headers.CONTENT_TYPE, Headers.COOKIE, Headers.DATE, Headers.EXPECT, Headers.FORWARDED,
            Headers.FROM, Headers.HOST, Headers.IF_MATCH, Headers.IF_MODIFIED_SINCE, Headers.IF_NONE_MATCH,
            Headers.IF_RANGE, Headers.IF_UNMODIFIED_SINCE, Headers.MAX_FORWARDS, Headers.ORIGIN,
            Headers.PRAGMA, Headers.PROXY_AUTHORIZATION, Headers.RANGE, Headers.REFERER,
            Headers.SEC_WEB_SOCKET_EXTENSIONS, Headers.SEC_WEB_SOCKET_KEY, Headers.SEC_WEB_SOCKET_PROTOCOL,
            Headers.SEC_WEB_SOCKET_VERSION, Headers.TE, Headers.TRAILER, Headers.TRANSFER_ENCODING,
            Headers.UPGRADE, Headers.USER_AGENT, Headers.VIA, Headers.WARNING, Headers.X_FORWARDED_FOR,
            Headers.X_FORWARDED_HOST, Headers.X_FORWARDED_PORT, Headers.X_FORWARDED_PROTO,
            Headers.X_FORWARDED_SERVER,
        };
        for (HttpString h : all) {
            int len = h.length();
            if (len >= HEADERS_BY_LENGTH.length) {
                continue;
            }
            HttpString[] bucket = HEADERS_BY_LENGTH[len];
            HEADERS_BY_LENGTH[len] = bucket == null
                ? new HttpString[] { h }
                : Arrays.copyOf(bucket, bucket.length + 1);
            HEADERS_BY_LENGTH[len][HEADERS_BY_LENGTH[len].length - 1] = h;
        }
    }

    private static final HttpString[] METHODS = {
        Methods.GET, Methods.POST, Methods.PUT, Methods.DELETE, Methods.HEAD, Methods.OPTIONS,
        Methods.PATCH, Methods.TRACE, Methods.CONNECT,
    };

    private final int maxHeaderSize;
    private final int maxHeaders;
    private final boolean decodeUrl;
    private final String urlCharset;
    private final boolean allowEncodedSlash;
    private final boolean allowUnescaped;

    /** Scratch for method, header name, header value, and path bytes. Grows to the largest field seen. */
    private byte[] scratch = new byte[256];
    private int scratchLen;
    private final StringBuilder decodeBuffer = new StringBuilder();

    private int state;
    private int consumed;
    private int headerCount;
    private HttpString headerName;
    private boolean bodyExpected;

    Http1Parser(OptionMap options) {
        this.maxHeaderSize = options.get(UndertowOptions.MAX_HEADER_SIZE, UndertowOptions.DEFAULT_MAX_HEADER_SIZE);
        this.maxHeaders = options.get(UndertowOptions.MAX_HEADERS, UndertowOptions.DEFAULT_MAX_HEADERS);
        this.decodeUrl = options.get(UndertowOptions.DECODE_URL, true);
        this.urlCharset = options.get(UndertowOptions.URL_CHARSET, StandardCharsets.UTF_8.name());
        this.allowEncodedSlash = options.get(UndertowOptions.ALLOW_ENCODED_SLASH, false);
        this.allowUnescaped = options.get(UndertowOptions.ALLOW_UNESCAPED_CHARACTERS_IN_URL, false);
        reset();
    }

    /** Prepares for the next request on the same connection. */
    void reset() {
        state = S_METHOD;
        consumed = 0;
        headerCount = 0;
        scratchLen = 0;
        headerName = null;
        bodyExpected = false;
    }

    boolean isComplete() {
        return state == S_DONE;
    }

    /**
     * Parses as much of the request head as the buffer holds.
     *
     * @return true when the head is complete; remaining bytes in the buffer belong to the body or next request
     * @throws BadRequestException on any syntax violation or limit breach
     */
    boolean parse(ByteBuffer buf, HttpServerExchange exchange) throws BadRequestException {
        int start = buf.position();
        try {
            while (buf.hasRemaining() && state != S_DONE) {
                byte b = buf.get();
                switch (state) {
                    case S_METHOD -> {
                        if (b == SP) {
                            exchange.setRequestMethod(internMethod());
                            scratchLen = 0;
                            state = S_TARGET;
                        } else if (TOKEN[b & 0xFF]) {
                            push(b);
                        } else {
                            throw bad("invalid method byte");
                        }
                    }
                    case S_TARGET -> {
                        if (b == SP) {
                            if (scratchLen == 0) {
                                throw bad("empty request target");
                            }
                            applyTarget(exchange);
                            scratchLen = 0;
                            state = S_VERSION;
                        } else if (b == CR || b == LF) {
                            throw bad("request target without version");
                        } else if (b > 0x20 && b != 0x7F) {
                            push(b);
                        } else {
                            throw bad("invalid byte in request target");
                        }
                    }
                    case S_VERSION -> {
                        if (b == CR) {
                            exchange.setProtocol(internProtocol());
                            scratchLen = 0;
                            state = S_LINE_LF;
                        } else if (scratchLen < 8 && b > 0x20) {
                            push(b);
                        } else {
                            throw bad("invalid HTTP version");
                        }
                    }
                    case S_LINE_LF -> {
                        if (b != LF) {
                            throw bad("bare CR after request line");
                        }
                        state = S_HEADER_START;
                    }
                    case S_HEADER_START -> {
                        if (b == CR) {
                            state = S_FINAL_LF;
                        } else if (b == SP || b == HT) {
                            throw bad("obsolete line folding");
                        } else if (TOKEN[b & 0xFF]) {
                            push(b);
                            state = S_HEADER_NAME;
                        } else {
                            throw bad("invalid header name byte");
                        }
                    }
                    case S_HEADER_NAME -> {
                        if (b == COLON) {
                            headerName = internHeader();
                            scratchLen = 0;
                            state = S_HEADER_SP;
                        } else if (TOKEN[b & 0xFF]) {
                            push(b);
                        } else {
                            throw bad("invalid header name byte");
                        }
                    }
                    case S_HEADER_SP -> {
                        if (b == SP || b == HT) {
                            // optional whitespace before value
                        } else if (b == CR) {
                            addHeader(exchange, "");
                            state = S_HEADER_LF;
                        } else if (FIELD_VALUE[b & 0xFF]) {
                            push(b);
                            state = S_HEADER_VALUE;
                        } else {
                            throw bad("invalid header value byte");
                        }
                    }
                    case S_HEADER_VALUE -> {
                        if (b == CR) {
                            addHeader(exchange, valueString());
                            scratchLen = 0;
                            state = S_HEADER_LF;
                        } else if (FIELD_VALUE[b & 0xFF]) {
                            push(b);
                        } else if (b == LF) {
                            throw bad("bare LF in header value");
                        } else {
                            throw bad("invalid header value byte");
                        }
                    }
                    case S_HEADER_LF -> {
                        if (b != LF) {
                            throw bad("bare CR after header");
                        }
                        state = S_HEADER_START;
                    }
                    case S_FINAL_LF -> {
                        if (b != LF) {
                            throw bad("bare CR after headers");
                        }
                        state = S_DONE;
                    }
                    default -> throw new IllegalStateException();
                }
            }
        } finally {
            consumed += buf.position() - start;
            if (consumed > maxHeaderSize) {
                throw new BadRequestException("request head exceeds " + maxHeaderSize + " bytes");
            }
        }
        return state == S_DONE;
    }

    private void push(byte b) {
        if (scratchLen == scratch.length) {
            if (scratch.length >= maxHeaderSize) {
                throw new IllegalStateException("field exceeds max header size");
            }
            scratch = Arrays.copyOf(scratch, Math.min(scratch.length * 2, maxHeaderSize));
        }
        scratch[scratchLen++] = b;
    }

    private static BadRequestException bad(String msg) {
        return new BadRequestException(msg);
    }

    private HttpString internMethod() throws BadRequestException {
        if (scratchLen == 0) {
            throw bad("empty method");
        }
        for (HttpString m : METHODS) {
            if (matches(m, false)) {
                return m;
            }
        }
        return new HttpString(scratch, 0, scratchLen);
    }

    private HttpString internProtocol() throws BadRequestException {
        if (scratchLen == 8 && scratch[0] == 'H' && scratch[1] == 'T' && scratch[2] == 'T' && scratch[3] == 'P'
            && scratch[4] == '/' && scratch[5] == '1' && scratch[6] == '.') {
            if (scratch[7] == '1') {
                return Protocols.HTTP_1_1;
            }
            if (scratch[7] == '0') {
                return Protocols.HTTP_1_0;
            }
        }
        throw bad("unsupported HTTP version");
    }

    private HttpString internHeader() throws BadRequestException {
        if (scratchLen < HEADERS_BY_LENGTH.length) {
            HttpString[] bucket = HEADERS_BY_LENGTH[scratchLen];
            if (bucket != null) {
                for (HttpString h : bucket) {
                    if (matches(h, true)) {
                        return h;
                    }
                }
            }
        }
        return new HttpString(scratch, 0, scratchLen);
    }

    /** Compares scratch against an interned name; header names compare case-insensitively. */
    private boolean matches(HttpString h, boolean caseInsensitive) {
        if (h.length() != scratchLen) {
            return false;
        }
        for (int i = 0; i < scratchLen; i++) {
            byte a = scratch[i];
            byte b = h.byteAt(i);
            if (a == b) {
                continue;
            }
            if (!caseInsensitive) {
                return false;
            }
            if ((a | 0x20) != (b | 0x20) || (a | 0x20) < 'a' || (a | 0x20) > 'z') {
                return false;
            }
        }
        return true;
    }

    private String valueString() {
        int end = scratchLen;
        while (end > 0 && (scratch[end - 1] == SP || scratch[end - 1] == HT)) {
            end--;
        }
        return new String(scratch, 0, end, StandardCharsets.ISO_8859_1);
    }

    private void addHeader(HttpServerExchange exchange, String value) throws BadRequestException {
        if (++headerCount > maxHeaders) {
            throw bad("too many headers");
        }
        exchange.getRequestHeaders().add(headerName, value);
        if (headerName == Headers.CONTENT_LENGTH || headerName == Headers.TRANSFER_ENCODING) {
            bodyExpected = true;
        }
    }

    private void applyTarget(HttpServerExchange exchange) throws BadRequestException {
        String target = new String(scratch, 0, scratchLen, StandardCharsets.ISO_8859_1);
        if (!allowUnescaped) {
            for (int i = 0; i < scratchLen; i++) {
                int c = scratch[i] & 0xFF;
                if (c >= 0x80 || c == '"' || c == '<' || c == '>' || c == '\\' || c == '^' || c == '`' || c == '{' || c == '|' || c == '}') {
                    throw bad("unescaped character in request target");
                }
            }
        }
        exchange.setRequestURI(target);
        try {
            io.undertow.server.Connectors.setExchangeRequestPath(exchange, target, urlCharset, decodeUrl, allowEncodedSlash, decodeBuffer);
        } catch (RuntimeException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ParameterLimitException || cause instanceof BadRequestException) {
                throw bad(cause.getMessage());
            }
            throw e;
        }
        decodeBuffer.setLength(0);
    }

    boolean isBodyExpected() {
        return bodyExpected;
    }
}
