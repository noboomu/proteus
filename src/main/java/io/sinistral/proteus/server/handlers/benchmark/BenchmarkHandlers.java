/**
 * 
 */
package io.sinistral.proteus.server.handlers.benchmark;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;
import com.jsoniter.output.JsonStream;

import io.sinistral.proteus.server.MediaType;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.handlers.HandlerGenerator.StatementParameterType;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;

/**
 * @author jbauer
 *
 */
public class BenchmarkHandlers implements Supplier<RoutingHandler>
{

	private static Logger log = LoggerFactory.getLogger(BenchmarkHandlers.class.getCanonicalName());

	private final static String PLAIN_TEXT = "text/plain".intern();
	
	public final static class BenchmarkMessage
	{
		public String message = "hello world";
	}
	
    final static ByteBuffer msgBuffer  = ByteBuffer.wrap("hello world".getBytes());

	public RoutingHandler get()
	{
 	     
	    final RoutingHandler handler = new RoutingHandler();
	    
	    handler.add(Methods.GET, "/".intern(), new HttpHandler(){
 
	     
	    	
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
				
				exchange.getResponseSender().send(msgBuffer);  
				
			} 
		} );
		
	    handler.add(Methods.GET, "/json".intern(), new HttpHandler(){
			 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
 				
				exchange.getResponseSender().send(JsonStream.serialize(Collections.singletonMap("message", "Hello, World!")));  
				
			} 
		} );
	    
	 
	    
	    handler.add(Methods.GET, "/string4".intern(), new HttpHandler(){
	    	 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
				exchange.getResponseSender().send(msgBuffer);  
				
			} 
		} );
	    
	    handler.add(Methods.GET, "/string2".intern(), new HttpHandler(){
	    	 
	    				@Override
	    				public void handleRequest(HttpServerExchange exchange) throws Exception
	    				{
	    					// TODO Auto-generated method stub
	    					
	    					final ServerResponse<String> resp = ServerResponse.response(String.class).body(msgBuffer);
	    	 				
	    					resp.send(this, exchange); 
 	    					
	    				} 
	    			} );
	    
	    
 
	    handler.add(Methods.GET, "/mvc/json", new HttpHandler(){
			 
				@Override
				public void handleRequest(HttpServerExchange exchange) throws Exception
				{
					// TODO Auto-generated method stub
					
					final ServerResponse<String> resp = ServerResponse.response(String.class).body(JsonStream.serialize(new BenchmarkMessage()));
	 				
					resp.send(this, exchange); 
					
				} 
			} );
	    
	    handler.add(Methods.GET, "/async", new HttpHandler(){
			 
				@Override
				public void handleRequest(HttpServerExchange exchange) throws Exception
				{
					// TODO Auto-generated method stub
	 				
					//exchange.getResponseSender().send(JsonStream.serialize(new BenchmarkMessage()));  
					
					if (exchange.isInIoThread()) {
				      exchange.dispatch(this);
				      return;
				    }
					
					CompletionStage<Boolean> future = authenticate();
					
					future.thenAccept( (passed) -> {
						
						exchange.setStatusCode(StatusCodes.ACCEPTED);
						exchange.getResponseSender().send("User authenticated"); 
						
					});
					
					future.exceptionally( (throwable) -> {
						
						
						exchange.setStatusCode(StatusCodes.FORBIDDEN);
						exchange.getResponseSender().send(throwable.getMessage()); 
						
						return null;
						
					});
					
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
				
 			 	exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.VIDEO_MP4.toString());
 			 
// 		        java.util.Optional<java.util.List<java.lang.String>> optionalStringArgs = java.util.Optional.ofNullable(exchange.getQueryParameters().get("optionalStringArgs")).map(java.util.Deque::stream).map( p -> p.map(java.lang.String::valueOf).collect(java.util.stream.Collectors.toList() ));

 			 	FileResourceManager mgr = new FileResourceManager(filePath.toFile());

 			 	ResourceHandler hdlr = new ResourceHandler(mgr);
 			 	
 			 	hdlr.handleRequest(exchange);
 			 		
 			  
			} 
		} );
	    
	    handler.add(Methods.GET, "/bytes.mp4".intern(), new HttpHandler(){
			 
			@Override
			public void handleRequest(HttpServerExchange exchange) throws Exception
			{
				// TODO Auto-generated method stub
				
//				if (exchange.isInIoThread()) {
//				      exchange.dispatch(this);
//				      return;
//				    }
//				
				Path filePath = Paths.get("./assets/video.mp4");
				
			 
				byte[] bytes = Files.toByteArray(filePath.toFile());
				
				exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, bytes.length);
 			 	exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, MediaType.AUDIO_MP4.toString());

 			 	exchange.getResponseSender().send(ByteBuffer.wrap(bytes));
			 
 			 		
 			 
 			 	
 				
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
	    
	
	    
	    handler.add(Methods.GET, "/string3".intern(),  new HttpHandler(){
	    	 
	 			@Override
	 			public void handleRequest(HttpServerExchange exchange) throws Exception
	 			{
	 				// TODO Auto-generated method stub
	 				 
	 				exchange.setStatusCode( 200 );
	 				
	 				//exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, PLAIN_TEXT);
	 				
	 				exchange.getResponseSender().send(msgBuffer);
	 					
	 			} 
	 		} );
	      
	    

	    return handler;
	} 
	
	private static ExecutorService EXECUTOR = Executors.newCachedThreadPool();
	
	public static CompletionStage<Boolean> authenticate()
	{
		CompletableFuture<Boolean> future =  new CompletableFuture<>();
		
		 
		int passed = (int)((Math.random() * 1000));
		
		try
		{
			Thread.sleep(4000L);
			
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		if(passed > 500)
		{
			future.complete(true);
		}
		else
		{
			future.completeExceptionally(new Exception("Failed to authenticate"));
		}
		
		return future;
	}
 

}
