/**
 * 
 */
package com.wurrly.server.handlers.benchmark;

import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.simplemagic.ContentType;
import com.jsoniter.output.JsonStream;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;

/**
 * @author jbauer
 *
 */
public class BenchmarkHandlers implements Supplier<RoutingHandler>
{

	private static Logger log = LoggerFactory.getLogger(BenchmarkHandlers.class.getCanonicalName());

	public final static class BenchmarkMessage
	{
		public String message = "hello world";
	}
	
	public RoutingHandler get()
	{
	    final ByteBuffer msgBuffer  = ByteBuffer.wrap("hello world".getBytes());
	    
	    RoutingHandler handler = new RoutingHandler();
	    
	    handler.add(Methods.GET, "/string", new HttpHandler(){
 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
				exchange.getResponseSender().send(msgBuffer);  
				
			} 
		} );
		
	    handler.add(Methods.GET, "/json", new HttpHandler(){
			 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
 				
				exchange.getResponseSender().send(JsonStream.serialize(new BenchmarkMessage()));  
				
			} 
		} );
	    
	    
	    handler.add(Methods.GET, "/video.mp4", new HttpHandler(){
			 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
//				if (exchange.isInIoThread()) {
//				      exchange.dispatch(this);
//				      return;
//				    }
//				
				
				Path filePath = Paths.get("./assets");
				
 			 	exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, ContentType.MP4A.getMimeType());
 			 	
 			 	FileResourceManager mgr = new FileResourceManager(filePath.toFile());

 			 	ResourceHandler hdlr = new ResourceHandler(mgr);
 			 	
 			 	hdlr.handleRequest(exchange);
 			 		
 			 
 			 	
 				
			 //	exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, length);
// 			try(	SeekableByteChannel channel = Files.newByteChannel(filePath, StandardOpenOption.READ) )
//			{
				//FileChannel fileChannel =   FileChannel.open(filePath, StandardOpenOption.READ);
	 
				
// 				int bufferLength = pooled.getBuffer().capacity();
//				
//				byte[] bytes = Files.readAllBytes(filePath);
//				
//				ByteBuffer allBytes = ByteBuffer.wrap(bytes);
//				
//				int buffers =  (int) Math.ceil(bytes.length / bufferLength);
//				
//				ByteBuffer[] byteBuffers = new ByteBuffer[buffers];
//				
//				int pos = 0;
//				
//				log.debug("buffers: " + buffers + " " + );
//				
//				for( int i = 0; i < buffers; i++ )
//				{
// 					byte[] bb = new byte[bufferLength];
//					int offset = i * bufferLength;
//					
//					int cLength = (offset + bufferLength) > bytes.length ? (bytes.length - offset) : bufferLength;
//					
//					log.debug("bsize: " + bufferLength + " offset: " + offset + " cLength: " + cLength);
//					allBytes.get(bb, offset, cLength);
//					
//					byteBuffers[i] = ByteBuffer.wrap(bb);
//				}
//				
//			//	channel.read(buffer);
//			//	buffer.flip();
//				exchange.getResponseSender().send(byteBuffers);
//				
//			//}
				
				
 
//					SeekableByteChannel channel = Files.newByteChannel(filePath, StandardOpenOption.READ) ;
//				{
//				ChunkedStream stream = new ChunkedStream();
//				stream.send(channel, exchange,new IoCallback(){
//
//					@Override
//					public void onComplete(HttpServerExchange exchange, Sender sender)
//					{
//						// TODO Auto-generated method stub
//						IoCallback.END_EXCHANGE.onComplete(exchange, sender);
//						
//						 
//					}
//
//					@Override
//					public void onException(HttpServerExchange exchange, Sender sender, IOException exception)
//					{
//						// TODO Auto-generated method stub
//						log.debug("exception");
// 
//						IoCallback.END_EXCHANGE.onException(exchange, sender,exception);
//
//						 
//					}});
//				}
 //				exchange.getResponseSender().send(JsonStream.serialize(new BenchmarkMessage()));  
				
			} 
		} );
	    
	    return handler;
	} 
	
 

}
