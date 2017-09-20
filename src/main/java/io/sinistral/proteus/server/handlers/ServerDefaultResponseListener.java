/**
 * 
 */
package io.sinistral.proteus.server.handlers;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.jsoniter.output.JsonStream;

import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

/**
 * @author jbauer
 *
 */
@Singleton
public class ServerDefaultResponseListener implements DefaultResponseListener
{
	private static Logger log = LoggerFactory.getLogger(ServerDefaultResponseListener.class.getCanonicalName());
 

   public static  AttachmentKey<Throwable> EXCEPTION = AttachmentKey.create(Throwable.class);

	@Inject
	protected XmlMapper xmlMapper;
	 
	
	@Override
	public boolean handleDefaultResponse(HttpServerExchange exchange)
	{
		 if (!exchange.isResponseChannelAvailable()) {
             return false;
         }
		   
         if (exchange.getResponseCode() >= 400) {
              
        	 Throwable throwable = exchange.getAttachment(EXCEPTION);
        	 
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
        	 
        	 if(throwable instanceof IllegalArgumentException )
        	 {
        		 exchange.setResponseCode(StatusCodes.BAD_REQUEST);
        	 }
        	 
        	 if( ServerPredicates.ACCEPT_XML_EXCLUSIVE_PREDICATE.resolve(exchange) )
        	 {  
				try
				{
					
					final String xmlBody = xmlMapper.writeValueAsString(errorMap);
					 exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, xmlBody.length());
		             exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.APPLICATION_XML);
	        		 exchange.getResponseSender().send(xmlBody);
	        		 
				} catch (JsonProcessingException e)
				{
					log.warn("Unable to create XML from error...");
				}
	             
        	 }
        	 else 
        	 { 
	        	 final String jsonBody = JsonStream.serialize(errorMap);
	        	 
	             exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.APPLICATION_JSON);
	             exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, jsonBody.length());
	             exchange.getResponseSender().send(jsonBody); 
        	 }

             return true;
         }
         return false;
	}

}
