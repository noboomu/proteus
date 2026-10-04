/**
 *
 */
package io.sinistral.proteus.server.predicates;

import io.sinistral.proteus.protocol.MediaType;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.Headers;

import java.util.Collections;

/**
 * Shared exchange predicates for content-type matching used by generation and extraction.
 *
 * @author jbauer
 */
public class ServerPredicates
{
    /** Utility class; use the static constants. */
    private ServerPredicates() {
    }

    /** The json_regex. */
    public static final String JSON_REGEX = "^(application\\/(json|x-javascript)|text\\/(json|x-javascript|x-json))(;.*)?$";
    /** The xml_regex. */
    public static final String XML_REGEX = "^(application\\/(xml|xhtml\\+xml)|text\\/xml)(;.*)?$";
    /** The html_regex. */
    public static final String HTML_REGEX = "^(text\\/html)(;.*)?$";
    /** The text_plain_regex. */
    public static final String TEXT_PLAIN_REGEX = "^(text\\/plain)(;.*)?$";
    /** The text_regex. */
    public static final String TEXT_REGEX = "^(?!(text)$).*";


    /** The json_predicate. */
    public static final Predicate JSON_PREDICATE = Predicates.regex(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), JSON_REGEX);
    /** The xml_predicate. */
    public static final Predicate XML_PREDICATE = Predicates.regex(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), XML_REGEX);
    /** The html_predicate. */
    public static final Predicate HTML_PREDICATE = Predicates.regex(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), HTML_REGEX);
    /** The binary_stream_predicate. */
    public static final Predicate BINARY_STREAM_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE),
            MediaType.APPLICATION_OCTET_STREAM.contentType());
    /** The wildcard_predicate. */
    public static final Predicate WILDCARD_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MediaType.ANY.contentType());
    /** The no_wildcard_predicate. */
    public static final Predicate NO_WILDCARD_PREDICATE = Predicates.not(Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MediaType.ANY.contentType()));
    /** The accept_json_predicate. */
    public static final Predicate ACCEPT_JSON_PREDICATE = Predicates.regex(ExchangeAttributes.requestHeader(Headers.ACCEPT), JSON_REGEX);
    /** The accept_xml_predicate. */
    public static final Predicate ACCEPT_XML_PREDICATE = Predicates.regex(ExchangeAttributes.requestHeader(Headers.ACCEPT), XML_REGEX);
    /** The accept_html_predicate. */
    public static final Predicate ACCEPT_HTML_PREDICATE = Predicates.regex(ExchangeAttributes.requestHeader(Headers.ACCEPT), HTML_REGEX);
    /** The accept_text_predicate. */
    public static final Predicate ACCEPT_TEXT_PREDICATE = Predicates.regex(ExchangeAttributes.requestHeader(Headers.ACCEPT), TEXT_PLAIN_REGEX);
    /** The accept_xml_exclusive_predicate. */
    public static final Predicate ACCEPT_XML_EXCLUSIVE_PREDICATE = Predicates.and(ACCEPT_XML_PREDICATE, NO_WILDCARD_PREDICATE);
    /** The max_content_size_predicate. */
    public static final Predicate MAX_CONTENT_SIZE_PREDICATE = new MaxRequestContentLengthPredicate.Builder().build(Collections.singletonMap("value", 0L));
    /** The string_body_predicate. */
    public static final Predicate STRING_BODY_PREDICATE = Predicates.and(Predicates.or(JSON_PREDICATE, XML_PREDICATE), MAX_CONTENT_SIZE_PREDICATE);
    /** The multipart_form_predicate. */
    public static final Predicate MULTIPART_FORM_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE),
            MultiPartParserDefinition.MULTIPART_FORM_DATA);
    /** The url_encoded_form_predicate. */
    public static final Predicate URL_ENCODED_FORM_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), FormEncodedDataDefinition.APPLICATION_X_WWW_FORM_URLENCODED);
}



