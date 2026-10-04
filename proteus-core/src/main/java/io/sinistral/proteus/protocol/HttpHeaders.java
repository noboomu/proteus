package io.sinistral.proteus.protocol;

/** HTTP header name and related token constants. */

public final class HttpHeaders {
    /** Accept header, listing acceptable response media types. */
    public static final String ACCEPT = "Accept";
    /** Accept-CH header, client hint request. */
    public static final String ACCEPT_CH = "Accept-CH";
    /** Accept-Charset header, listing acceptable character sets. */
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    /** Accept-CH-Lifetime header, client hint lifetime. */
    public static final String ACCEPT_CH_LIFETIME = "Accept-CH-Lifetime";
    /** Accept-Encoding header, listing acceptable content codings. */
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    /** Accept-Language header, listing preferred natural languages. */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    /** Accept-Push-Policy header, acceptable push policy for a request. */
    public static final String ACCEPT_PUSH_POLICY = "Accept-Push-Policy";
    /** Accept-Ranges header, signaling support for range requests. */
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    /** Accept-Signature header, acceptable signature metadata fields. */
    public static final String ACCEPT_SIGNATURE = "Accept-Signature";
    /** Access-Control-Allow-Credentials CORS response header. */
    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    /** Access-Control-Allow-Headers CORS response header. */
    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    /** Access-Control-Allow-Methods CORS response header. */
    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
    /** Access-Control-Allow-Origin CORS response header. */
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    /** Access-Control-Expose-Headers CORS response header. */
    public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    /** Access-Control-Max-Age CORS response header. */
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    /** Access-Control-Request-Headers CORS request header. */
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    /** Access-Control-Request-Method CORS request header. */
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    /** Age header, object age in cache. */
    public static final String AGE = "Age";
    /** Allow header, methods supported by the target resource. */
    public static final String ALLOW = "Allow";
    /** Alt-Svc header, alternative service advertisement. */
    public static final String ALT_SVC = "Alt-Svc";
    /** Audio request destination. */
    public static final String AUDIO = "audio";
    /** Audio worklet request destination. */
    public static final String AUDIOWORKLET = "audioworklet";
    /** Authorization header, credentials for authentication. */
    public static final String AUTHORIZATION = "Authorization";
    /** Legacy misspelled alias for Access-Control-Allow-Headers. */
    public static final String Access_Control_Allow_Headers = "Access-Control-Allow-Headers";
    /** Cache-Control header, caching directives. */
    public static final String CACHE_CONTROL = "Cache-Control";
    /** Clear-Site-Data header, site data clearing directive. */
    public static final String CLEAR_SITE_DATA = "Clear-Site-Data";
    /** Connection header, connection control options. */
    public static final String CONNECTION = "Connection";
    /** Content-Disposition header, presentation hint for the body. */
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    /** Content-DPR header, client device pixel ratio for the request. */
    public static final String CONTENT_DPR = "Content-DPR";
    /** Content-Encoding header, applied content codings. */
    public static final String CONTENT_ENCODING = "Content-Encoding";
    /** Content-Language header, intended audience languages. */
    public static final String CONTENT_LANGUAGE = "Content-Language";
    /** Content-Length header, body size in bytes. */
    public static final String CONTENT_LENGTH = "Content-Length";
    /** Content-Location header, alternate location for the body. */
    public static final String CONTENT_LOCATION = "Content-Location";
    /** Content-MD5 header, body MD5 digest. */
    public static final String CONTENT_MD5 = "Content-MD5";
    /** Content-Range header, partial body range. */
    public static final String CONTENT_RANGE = "Content-Range";
    /** Content-Security-Policy header, CSP directives. */
    public static final String CONTENT_SECURITY_POLICY = "Content-Security-Policy";
    /** Content-Security-Policy-Report-Only header. */
    public static final String CONTENT_SECURITY_POLICY_REPORT_ONLY = "Content-Security-Policy-Report-Only";
    /** Content-Type header, body media type. */
    public static final String CONTENT_TYPE = "Content-Type";
    /** Cookie request header. */
    public static final String COOKIE = "Cookie";
    /** Cookie2 request header, RFC 2965. */
    public static final String COOKIE2 = "Cookie2";
    /** Cors request mode. */
    public static final String CORS = "cors";
    /** Crossdomain.xml policy file path. */
    public static final String CROSSDOMAIN_XML = "crossdomain.xml";
    /** Cross-Origin-Embedder-Policy header. */
    public static final String CROSS_ORIGIN_EMBEDDER_POLICY = "Cross-Origin-Embedder-Policy";
    /** Cross-Origin-Opener-Policy header. */
    public static final String CROSS_ORIGIN_OPENER_POLICY = "Cross-Origin-Opener-Policy";
    /** Cross-Origin-Resource-Policy header. */
    public static final String CROSS_ORIGIN_RESOURCE_POLICY = "Cross-Origin-Resource-Policy";
    /** Cross-site request destination. */
    public static final String CROSS_SITE = "cross-site";
    /** Date header, message origination time. */
    public static final String DATE = "Date";
    /** Dav header, WebDAV class compliance. */
    public static final String DAV = "Dav";
    /** Depth header, WebDAV operation depth. */
    public static final String DEPTH = "Depth";
    /** Destination header, WebDAV COPY/MOVE target. */
    public static final String DESTINATION = "Destination";
    /** Device-Memory client hint header. */
    public static final String DEVICE_MEMORY = "Device-Memory";
    /** DNT header, do-not-track preference. */
    public static final String DNT = "DNT";
    /** Document request destination. */
    public static final String DOCUMENT = "document";
    /** DPR client hint header, device pixel ratio. */
    public static final String DPR = "DPR";
    /** Early-Data header, replay-safe request marking. */
    public static final String EARLY_DATA = "Early-Data";
    /** Embed element destination token. */
    public static final String EMBED = "<embed>";
    /** Empty request destination. */
    public static final String EMPTY = "empty";
    /** ETag header, entity tag for the selected representation. */
    public static final String ETAG = "ETag";
    /** Expect header, expectations that must be met by the server. */
    public static final String EXPECT = "Expect";
    /** Expect-CT header, certificate transparency expectation. */
    public static final String EXPECT_CT = "Expect-CT";
    /** Expires header, response expiration time. */
    public static final String EXPIRES = "Expires";
    /** Feature-Policy header, feature control directives. */
    public static final String FEATURE_POLICY = "Feature-Policy";
    /** Font request destination. */
    public static final String FONT = "font";
    /** Forwarded header, proxy path information. */
    public static final String FORWARDED = "Forwarded";
    /** Frame element destination token. */
    public static final String FRAME = "<frame>";
    /** From header, sender email address. */
    public static final String FROM = "From";
    /** Host header, target host and port. */
    public static final String HOST = "Host";
    /** Http-equiv attribute token. */
    public static final String HTTP_EQUIV = "http-equiv";
    /** If header, WebDAV conditional request token. */
    public static final String IF = "If";
    /** Iframe element destination token. */
    public static final String IFRAME = "<iframe>";
    /** If-Match header, precondition on entity tags. */
    public static final String IF_MATCH = "If-Match";
    /** If-Modified-Since header, precondition on modification time. */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    /** If-None-Match header, precondition on entity tags. */
    public static final String IF_NONE_MATCH = "If-None-Match";
    /** If-Range header, conditional range request. */
    public static final String IF_RANGE = "If-Range";
    /** If-Unmodified-Since header, precondition on modification time. */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    /** Image request destination. */
    public static final String IMAGE = "image";
    /** Keep-Alive header, persistent connection parameters. */
    public static final String KEEP_ALIVE = "Keep-Alive";
    /** Large-Allocation header, process allocation hint. */
    public static final String LARGE_ALLOCATION = "Large-Allocation";
    /** Last-Event-ID header, server-sent events resume point. */
    public static final String LAST_EVENT_ID = "Last-Event-ID";
    /** Last-Modified header, last modification time. */
    public static final String LAST_MODIFIED = "Last-Modified";
    /** Link header, typed relationship to another resource. */
    public static final String LINK = "Link";
    /** Location header, redirect target URI. */
    public static final String LOCATION = "Location";
    /** Lock-Token header, WebDAV lock token. */
    public static final String LOCK_TOKEN = "Lock-Token";
    /** Manifest request destination. */
    public static final String MANIFEST = "manifest";
    /** Max-Forwards header, proxy hop limit for TRACE/OPTIONS. */
    public static final String MAX_FORWARDS = "Max-Forwards";
    /** Meta element destination token. */
    public static final String META = "<meta>";
    /** Navigate request mode. */
    public static final String NAVIGATE = "navigate";
    /** NEL header, network error logging policy. */
    public static final String NEL = "NEL";
    /** Nested-document request destination. */
    public static final String NESTED_DOCUMENT = "nested-document";
    /** Nested-navigate request destination. */
    public static final String NESTED_NAVIGATE = "nested-navigate";
    /** None request destination. */
    public static final String NONE = "none";
    /** No-cors request mode. */
    public static final String NO_CORS = "no-cors";
    /** Object element destination token. */
    public static final String OBJECT = "<object>";
    /** Origin header, request origin. */
    public static final String ORIGIN = "Origin";
    /** Overwrite header, WebDAV overwrite flag. */
    public static final String OVERWRITE = "Overwrite";
    /** Paint worklet request destination. */
    public static final String PAINTWORKLET = "paintworklet";
    /** Ping-From header, ping source document. */
    public static final String PING_FROM = "Ping-From";
    /** Ping-To header, ping target address. */
    public static final String PING_TO = "Ping-To";
    /** Post request method. */
    public static final String POST = "POST";
    /** Pragma header, implementation-specific directives. */
    public static final String PRAGMA = "Pragma";
    /** Proxy-Authenticate header, proxy authentication challenge. */
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    /** Proxy-Authorization header, proxy credentials. */
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    /** Public-Key-Pins header, pinned certificate hashes. */
    public static final String PUBLIC_KEY_PINS = "Public-Key-Pins";
    /** Public-Key-Pins-Report-Only header. */
    public static final String PUBLIC_KEY_PINS_REPORT_ONLY = "Public-Key-Pins-Report-Only";
    /** Push-Policy header, acceptable push policy for a response. */
    public static final String PUSH_POLICY = "Push-Policy";
    /** Range header, partial content request. */
    public static final String RANGE = "Range";
    /** Referer header, referring page address. */
    public static final String REFERER = "Referer";
    /** Referrer-Policy header, referrer disclosure policy. */
    public static final String REFERRER_POLICY = "Referrer-Policy";
    /** Report request destination. */
    public static final String REPORT = "report";
    /** Report-To header, reporting endpoint group. */
    public static final String REPORT_TO = "Report-To";
    /** Retry-After header, retry delay after throttling. */
    public static final String RETRY_AFTER = "Retry-After";
    /** Same-origin request destination. */
    public static final String SAME_ORIGIN = "same-origin";
    /** Same-site request destination. */
    public static final String SAME_SITE = "same-site";
    /** Save-Data client hint header. */
    public static final String SAVE_DATA = "Save-Data";
    /** Script request destination. */
    public static final String SCRIPT = "script";
    /** Sec-Fetch-Dest header, request destination. */
    public static final String SEC_FETCH_DEST = "Sec-Fetch-Dest";
    /** Sec-Fetch-Mode header, request mode. */
    public static final String SEC_FETCH_MODE = "Sec-Fetch-Mode";
    /** Sec-Fetch-Site header, request origin relation. */
    public static final String SEC_FETCH_SITE = "Sec-Fetch-Site";
    /** Sec-Fetch-User header, user activation flag. */
    public static final String SEC_FETCH_USER = "Sec-Fetch-User";
    /** Sec-WebSocket-Accept handshake header. */
    public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
    /** Sec-WebSocket-Extensions handshake header. */
    public static final String SEC_WEBSOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";
    /** Sec-WebSocket-Key handshake header. */
    public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
    /** Sec-WebSocket-Protocol handshake header. */
    public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    /** Sec-WebSocket-Version handshake header. */
    public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
    /** Server header, origin server software. */
    public static final String SERVER = "Server";
    /** Server-Timing header, server timing metrics. */
    public static final String SERVER_TIMING = "Server-Timing";
    /** Service worker request destination. */
    public static final String SERVICEWORKER = "serviceworker";
    /** Service-Worker-Allowed header, worker scope control. */
    public static final String SERVICE_WORKER_ALLOWED = "Service-Worker-Allowed";
    /** Set-Cookie response header. */
    public static final String SET_COOKIE = "Set-Cookie";
    /** Set-Cookie2 response header, RFC 2965. */
    public static final String SET_COOKIE2 = "Set-Cookie2";
    /** Shared worker request destination. */
    public static final String SHAREDWORKER = "sharedworker";
    /** Signature header, signed HTTP exchange signature. */
    public static final String SIGNATURE = "Signature";
    /** Signed-Headers header, signed header list. */
    public static final String SIGNED_HEADERS = "Signed-Headers";
    /** SourceMap header, source map link. */
    public static final String SOURCEMAP = "SourceMap";
    /** Status-URI header, signature status resource. */
    public static final String STATUS_URI = "Status-URI";
    /** Strict-Transport-Security header, HSTS policy. */
    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
    /** Style request destination. */
    public static final String STYLE = "style";
    /** TE header, acceptable transfer codings. */
    public static final String TE = "TE";
    /** Timeout header, WebDAV lock timeout. */
    public static final String TIMEOUT = "Timeout";
    /** Timing-Allow-Origin header, timing visibility origins. */
    public static final String TIMING_ALLOW_ORIGIN = "Timing-Allow-Origin";
    /** Tk header, tracking status. */
    public static final String TK = "Tk";
    /** Track request destination. */
    public static final String TRACK = "track";
    /** Trailer header, header fields in the trailer. */
    public static final String TRAILER = "Trailer";
    /** Transfer-Encoding header, transfer coding list. */
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    /** Upgrade header, protocol upgrade request. */
    public static final String UPGRADE = "Upgrade";
    /** Upgrade-Insecure-Requests header. */
    public static final String UPGRADE_INSECURE_REQUESTS = "Upgrade-Insecure-Requests";
    /** User-Agent header, client software identity. */
    public static final String USER_AGENT = "User-Agent";
    /** Vary header, request headers that select this representation. */
    public static final String VARY = "Vary";
    /** Via header, proxy chain information. */
    public static final String VIA = "Via";
    /** Video request destination. */
    public static final String VIDEO = "video";
    /** Viewport-Width client hint header. */
    public static final String VIEWPORT_WIDTH = "Viewport-Width";
    /** Warning header, additional warning information. */
    public static final String WARNING = "Warning";
    /** Websocket request destination. */
    public static final String WEBSOCKET = "websocket";
    /** Width client hint header, resource width. */
    public static final String WIDTH = "Width";
    /** Worker request destination. */
    public static final String WORKER = "worker";
    /** WWW-Authenticate header, authentication challenge. */
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    /** XSLT request destination. */
    public static final String XSLT = "xslt";
    /** X-Content-Type-Options header, MIME sniffing control. */
    public static final String X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options";
    /** X-DNS-Prefetch-Control header. */
    public static final String X_DNS_PREFETCH_CONTROL = "X-DNS-Prefetch-Control";
    /** X-Download-Options header. */
    public static final String X_DOWNLOAD_OPTIONS = "X-Download-Options";
    /** X-Firefox-Spdy header. */
    public static final String X_FIREFOX_SPDY = "X-Firefox-Spdy";
    /** X-Forwarded-For header, originating client chain. */
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";
    /** X-Forwarded-Host header, original host. */
    public static final String X_FORWARDED_HOST = "X-Forwarded-Host";
    /** X-Forwarded-Proto header, original protocol. */
    public static final String X_FORWARDED_PROTO = "X-Forwarded-Proto";
    /** X-Frame-Options header, framing control. */
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";
    /** X-Permitted-Cross-Domain-Policies header. */
    public static final String X_PERMITTED_CROSS_DOMAIN_POLICIES = "X-Permitted-Cross-Domain-Policies";
    /** X-Pingback header, pingback server URL. */
    public static final String X_PINGBACK = "X-Pingback";
    /** X-Powered-By header, server technology. */
    public static final String X_POWERED_BY = "X-Powered-By";
    /** X-Requested-With header, XHR marker. */
    public static final String X_REQUESTED_WITH = "X-Requested-With";
    /** X-Robots-Tag header, robot control. */
    public static final String X_ROBOTS_TAG = "X-Robots-Tag";
    /** X-UA-Compatible header, legacy browser mode. */
    public static final String X_UA_COMPATIBLE = "X-UA-Compatible";
    /** X-XSS-Protection header, legacy XSS filter. */
    public static final String X_XSS_PROTECTION = "X-XSS-Protection";

    /** Utility class, not instantiated. */
    private HttpHeaders() {
    }
}
