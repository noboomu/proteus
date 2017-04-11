/**
 * 
 */
package com.wurrly.server.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.jsoniter.output.JsonStream;

import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

/**
 * @author jbauer
 *
 */
public class ServerDefaultResponseListener implements DefaultResponseListener
{
 

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
        
        	 final String jsonBody = JsonStream.serialize(throwable);
        	 
             exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, org.apache.http.entity.ContentType.APPLICATION_JSON.getMimeType());
             exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, jsonBody.length());
             exchange.getResponseSender().send(jsonBody);
             
             return true;
         }
         return false;
	}

}
