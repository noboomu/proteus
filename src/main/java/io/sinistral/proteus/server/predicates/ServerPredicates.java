/**
 * 
 */
package io.sinistral.proteus.server.predicates;

import java.util.Collections;

import io.sinistral.proteus.server.MediaType;
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
	public static final Predicate JSON_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), MediaType.APPLICATION_JSON.contentType(), MediaType.APPLICATION_JSON.withCharset("utf-8"));
 	public static final Predicate XML_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), MediaType.APPLICATION_XML.contentType(),MediaType.APPLICATION_XML.withCharset("utf-8"));
	public static final Predicate WILDCARD_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MediaType.ANY.contentType());
	public static final Predicate NO_WILDCARD_PREDICATE = Predicates.not(Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MediaType.ANY.contentType())); 
	public static final Predicate ACCEPT_JSON_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MediaType.APPLICATION_JSON.contentType(),MediaType.APPLICATION_JSON.withCharset("utf-8").toString());
 	public static final Predicate ACCEPT_XML_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), MediaType.APPLICATION_XML.contentType(),MediaType.APPLICATION_XML.withCharset("utf-8").toString()); 
    public static final Predicate ACCEPT_XML_EXCLUSIVE_PREDICATE = Predicates.and(ACCEPT_XML_PREDICATE, NO_WILDCARD_PREDICATE ); 
 	public static final Predicate MAX_CONTENT_SIZE_PREDICATE = new MaxRequestContentLengthPredicate.Builder().build(Collections.singletonMap("value", 0L));
    public static final Predicate STRING_BODY_PREDICATE = Predicates.and(Predicates.or(JSON_PREDICATE,XML_PREDICATE), MAX_CONTENT_SIZE_PREDICATE );
 	public static final Predicate MULTIPART_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), MediaType.APPLICATION_OCTET_STREAM.contentType(), MultiPartParserDefinition.MULTIPART_FORM_DATA );
 	public static final Predicate URL_ENCODED_FORM_PREDICATE = Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), FormEncodedDataDefinition.APPLICATION_X_WWW_FORM_URLENCODED );

 
}
