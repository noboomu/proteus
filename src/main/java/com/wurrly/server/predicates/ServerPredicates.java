/**
 * 
 */
package com.wurrly.server.predicates;

import java.util.Collections;

import com.wurrly.server.MimeTypes;

import io.undertow.attribute.ExchangeAttributes;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.Headers;

/**
 * @author jbauer
 *
 */
public class ServerPredicates
{

	public static final Predicate ACCEPT_JSON_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MimeTypes.APPLICATION_JSON_TYPE);
 	public static final Predicate ACCEPT_XML_PREDICATE = io.undertow.predicate.Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MimeTypes.APPLICATION_XML_TYPE);
 	public static final Predicate MAX_CONTENT_SIZE_PREDICATE = new MaxRequestContentLengthPredicate.Builder().build(Collections.singletonMap("value", 0L));
    public static final Predicate STRING_BODY_PREDICATE = io.undertow.predicate.Predicates.and(io.undertow.predicate.Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), MimeTypes.APPLICATION_JSON_TYPE, MimeTypes.APPLICATION_XML_TYPE), MAX_CONTENT_SIZE_PREDICATE );
 	public static final Predicate MULTIPART_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), MimeTypes.OCTET_STREAM_TYPE, MultiPartParserDefinition.MULTIPART_FORM_DATA );
 	public static final Predicate URL_ENCODED_FORM_PREDICATE = io.undertow.predicate.Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), FormEncodedDataDefinition.APPLICATION_X_WWW_FORM_URLENCODED );
	public static final Predicate JSON_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), MimeTypes.APPLICATION_JSON_TYPE);
 	public static final Predicate XML_PREDICATE = io.undertow.predicate.Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), MimeTypes.APPLICATION_XML_TYPE);
}
