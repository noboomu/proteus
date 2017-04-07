/**
 * 
 */
package com.wurrly.server;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.HeaderMap;
import io.undertow.util.StatusCodes;;

/**
 * @author jbauer
 *
 */
public class ServerResponse
{
	private ByteBuffer buffer;
	private int status = StatusCodes.OK;
	private HeaderMap headers = new HeaderMap();
	private Map<String,Cookie> cookies = new HashMap<>(); 
  	
	public ServerResponse()
	{
		
	}
	 
	
	public ByteBuffer buffer()
	{
		return this.buffer;
	}
	
	public int status()
	{
		return this.status;
	}
	
	public Map<String,Cookie> cookies()
	{
		return this.cookies;
	}
	
	public HeaderMap headers()
	{
		return this.headers;
	}
	
	 
}
