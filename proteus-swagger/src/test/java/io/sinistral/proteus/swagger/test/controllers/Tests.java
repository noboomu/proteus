/**
 * 
 */
package io.sinistral.proteus.swagger.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.undertow.server.HttpServerExchange;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static io.sinistral.proteus.server.ServerResponse.response;

/**
 * @author jbauer
 *
 */

@Api(tags="tests")
@Path("/tests")
@Produces((MediaType.APPLICATION_JSON)) 
@Consumes((MediaType.MEDIA_TYPE_WILDCARD)) 
@Singleton
public class Tests
{
	 private static final ByteBuffer buffer;
	  static {
	    String message = "Hello, World!";
	    byte[] messageBytes = message.getBytes(java.nio.charset.StandardCharsets.US_ASCII);
	    buffer = ByteBuffer.allocateDirect(messageBytes.length);
	    buffer.put(messageBytes);
	    buffer.flip();
	  }
	  
	@Inject
	protected ObjectMapper objectMapper;

	
	@GET
	@Path("exchange/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value =  "Plaintext endpoint"  )
	public void exchangePlaintext(HttpServerExchange exchange)
	{ 
		response("Hello, World!").textPlain().send(exchange);

	}


	@GET
	@Path("response/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value =  "Plaintext endpoint"  )
	public ServerResponse<ByteBuffer> responsePlaintext(ServerRequest request)
	{ 
		return response("Hello, World!").textPlain();

	}
	
	@GET
	@Path("response/future/map")
	@ApiOperation(value =  "Future map endpoint"  )
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return CompletableFuture.completedFuture(response( map ).applicationJson());
	}
	
	@GET
	@Path("response/map")
	@ApiOperation(value =  "Map endpoint"  )
	public ServerResponse<Map<String,String>> futureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return  response( map ).applicationJson();
	}
	
	@POST
	@Path("response/file/path")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value =  "Upload file path endpoint"  )
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{  
		return response(ByteBuffer.wrap(Files.toByteArray(file.toFile()))).applicationOctetStream(); 
	}
	
	@POST
	@Path("response/file/path/optional")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@ApiOperation(value =  "Upload optional file path endpoint"  )
	public ServerResponse<ByteBuffer> responseUploadOptionalFilePath(ServerRequest request, @FormParam("file") Optional<java.nio.file.Path> file ) throws Exception
	{  
		if(file.isPresent())
		{
			return response(ByteBuffer.wrap(Files.toByteArray(file.get().toFile()))).applicationOctetStream(); 
		}
		else
		{
			return response().noContent(); 
		}
	}

	@GET
	@Path("generic/set")
	@Produces((MediaType.APPLICATION_JSON)) 
	@ApiOperation(value =  "Generic set endpoint"  )
	public ServerResponse<Set<Long>>  genericSet( ServerRequest request, @QueryParam("ids") Set<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	
	  
	@POST
	@Path("generic/set/bean")
	@Produces((MediaType.APPLICATION_JSON)) 
 	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value =  "Generic bean set endpoint"  )
	public ServerResponse<Set<Long>>  genericBeanSet( ServerRequest request, @BeanParam Set<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	
	
	@POST
	@Path("generic/list/bean")
	@Produces((MediaType.APPLICATION_JSON)) 
 	@Consumes(MediaType.APPLICATION_JSON)

	@ApiOperation(value =  "Generic bean list endpoint"  )
	public ServerResponse<List<Long>>  genericBeanList( ServerRequest request, @BeanParam List<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	  
	@GET
	@Path("optional/set")
	@Produces((MediaType.APPLICATION_JSON)) 
	@ApiOperation(value =  "Generic optional set endpoint"  )
	public ServerResponse<Set<Long>>  genericOptionalSet( ServerRequest request, @QueryParam("ids") Optional<Set<Long>> ids )  throws Exception
	{  
		return response( ids.get() ).applicationJson();  
	}


	@POST
	@Path("response/parse/ids")
	@Blocking
	@Produces(MediaType.APPLICATION_JSON) 
 	@Consumes(MediaType.APPLICATION_JSON)
 	@ApiOperation(value =  "Convert ids") 
	public ServerResponse<List<Long>> listConversion( ServerRequest request, @BeanParam List<Long> ids ) throws Exception
	{ 
		 
		return response( ids ).applicationJson(); 
		 

	}
	
	@GET
	@Path("response/parse/timestamp")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)  
 	@ApiOperation(value =  "Convert timestamp") 
	public ServerResponse<ByteBuffer> timestampConversion( ServerRequest request, @QueryParam("timestamp") Timestamp timestamp ) throws Exception
	{
		return response().body(timestamp.toString()).textPlain();
	}
	
	@GET
	@Path("response/parse/instant")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)  
 	@ApiOperation(value =  "Convert instant") 
	public ServerResponse<ByteBuffer> instantConversion( ServerRequest request, @QueryParam("instant") Instant instant ) throws Exception
	{ 
		 
		return response().body(instant.toString()).textPlain(); 
		 

	}
	
	@POST
	@Path("response/bytebuffer")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
 	@ApiOperation(value =  "Upload file path endpoint")
	public ServerResponse<ByteBuffer> responseUploadByteBuffer(ServerRequest request, @FormParam("file") ByteBuffer file ) throws Exception
	{ 
		 
		return response(file).applicationOctetStream();
		 

	}
	
	@POST
	@Path("response/file")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
 	@ApiOperation(value =  "Upload file path endpoint")
	public ServerResponse<ByteBuffer> responseUploadFile(ServerRequest request, @FormParam("file") File file ) throws Exception
	{ 
		
		ByteBuffer response = ByteBuffer.wrap(Files.asByteSource(file).read());
		
		 
		return response(response).applicationOctetStream();
		 

	}
	
	@GET
	@Path("response/debug")
 	@ApiOperation(value =  "Debug endpoint")
	public ServerResponse<Map<String,String>> debugEndpoint(ServerRequest request) 
	{  
		try
		{
			Map<String,String> map = ImmutableMap.of("message", "Hello, World!");
			
			return response( map ).applicationJson();
		} catch(Exception e)
		{
			return response().badRequest(e);
		}
	}

	@GET
	@Path("response/debug/blocking")
	@Blocking
 	@ApiOperation(value="Debug blocking endpoint")
	public ServerResponse<Map<String,String>> debugBlockingEndpoint(ServerRequest request) 
	{  
		try
		{
			Map<String,String> map = ImmutableMap.of("message", "Hello, World!");
			
			return response( map ).applicationJson();
		} catch(Exception e)
		{
			return response().badRequest(e);
		}
	}

}
