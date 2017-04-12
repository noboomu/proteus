/**
 * 
 */
package com.wurrly.server.handlers;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jsoniter.output.JsonStream;
import com.wurrly.server.MimeTypes;
import com.wurrly.server.ServerPredicates;

import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * @author jbauer
 *
 */
@Singleton
public class ServerDefaultResponseListener implements DefaultResponseListener
{
	private static Logger log = LoggerFactory.getLogger(ServerDefaultResponseListener.class.getCanonicalName());
 
	@Inject
	protected XmlMapper xmlMapper;
	
	@Override
	public boolean handleDefaultResponse(HttpServerExchange exchange)
	{
		 if (!exchange.isResponseChannelAvailable()) {
             return false;
         }
    
         if (exchange.getStatusCode() == 500) {
              
        	 Throwable throwable = exchange.getAttachment(DefaultResponseListener.EXCEPTION);
        	 
        	 if( throwable == null )
        	 {
        		 throwable = new Exception("An unknown error occured");
        	 }
        
        	 Map<String, String> errorMap = new HashMap<>();
        	 
        	 errorMap.put("message", throwable.getMessage());
        	 
        	 if( throwable.getStackTrace() != null )
     		{
     			if( throwable.getStackTrace().length > 0 )
     			{
     				errorMap.put("exceptionClass", throwable.getStackTrace()[0].getClassName()); 
     			}
     		} 
        	 
        	 if( ServerPredicates.ACCEPT_XML_PREDICATE.resolve(exchange) )
        	 {  
				try
				{
					
					final String xmlBody = xmlMapper.writeValueAsString(errorMap);
					 exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, xmlBody.length());
		             exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MimeTypes.APPLICATION_XML_TYPE);
	        		 exchange.getResponseSender().send(xmlBody);
	        		 
				} catch (JsonProcessingException e)
				{
					log.warn("Unable to create XML from error...");
				}
	             
        	 }
        	 else 
        	 { 
	        	 final String jsonBody = JsonStream.serialize(errorMap);
	        	 
	             exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MimeTypes.APPLICATION_JSON_TYPE);
	             exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, jsonBody.length());
	             exchange.getResponseSender().send(jsonBody); 
        	 }

             return true;
         }
         return false;
	}

}
