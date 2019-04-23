/**
 * 
 */
package io.sinistral.proteus.openapi.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.openapi.test.models.Pojo;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import io.undertow.server.HttpServerExchange;
import org.javamoney.moneta.Money;

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
import java.util.HashMap;
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

@Tags({@Tag(name = "tests")})
@Path("/tests")
@Produces((MediaType.APPLICATION_JSON)) 
@Consumes((MediaType.MEDIA_TYPE_WILDCARD)) 
@Singleton
public class OpenAPITests
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
	@Operation(description = "Plaintext endpoint"  )
	public void exchangePlaintext(HttpServerExchange exchange)
	{ 
		response("Hello, World!").textPlain().send(exchange);

	}


	@GET
	@Path("response/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@Operation(description = "Plaintext endpoint"  )
	public ServerResponse<ByteBuffer> responsePlaintext(ServerRequest request)
	{ 
		return response("Hello, World!").textPlain();

	}
	
	@GET
	@Path("response/future/map")
	@Operation(description = "Future map endpoint"  )
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return CompletableFuture.completedFuture(response( map ).applicationJson());
	}
	
	@GET
	@Path("response/map")
	@Operation(description = "Map endpoint"  )
	public ServerResponse<Map<String,String>> futureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return  response( map ).applicationJson();
	}
	
	@POST
	@Path("response/file/path")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Operation(description = "Upload file path endpoint"  )
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{  
		return response(ByteBuffer.wrap(Files.toByteArray(file.toFile()))).applicationOctetStream(); 
	}
	
	@POST
	@Path("response/file/path/optional")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Operation(description = "Upload optional file path endpoint"  )
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
	@Operation(description = "Generic set endpoint"  )
	public ServerResponse<Set<Long>>  genericSet( ServerRequest request, @QueryParam("ids") Set<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}


	@GET
	@Path("types/money")
	@Produces((MediaType.APPLICATION_JSON))
	@Operation(description = "Money type endpoint"  )
	public ServerResponse<Money>  getMoney(ServerRequest request )  throws Exception
	{
		return response( Money.of(123.23,"USD") ).applicationJson();
	}


	@GET
	@Path("types/pojo")
	@Produces((MediaType.APPLICATION_JSON))
	@Operation(description = "Pojo type endpoint"  )
	public ServerResponse<Pojo>  getPojo(ServerRequest request )  throws Exception
	{
		return response( new Pojo(100L,"John Doe") ).applicationJson();
	}

	@POST
	@Path("generic/set/bean")
	@Produces((MediaType.APPLICATION_JSON)) 
 	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(description = "Generic bean set endpoint"  )
	public ServerResponse<Set<Long>>  genericBeanSet( ServerRequest request, @BeanParam Set<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	
	
	@POST
	@Path("generic/list/bean")
	@Produces((MediaType.APPLICATION_JSON)) 
 	@Consumes(MediaType.APPLICATION_JSON)

	@Operation(description = "Generic bean list endpoint"  )
	public ServerResponse<List<Long>>  genericBeanList( ServerRequest request, @BeanParam List<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	  
	@GET
	@Path("optional/set")
	@Produces((MediaType.APPLICATION_JSON)) 
	@Operation(description = "Generic optional set endpoint"  )
	public ServerResponse<Set<Long>>  genericOptionalSet( ServerRequest request, @QueryParam("ids") Optional<Set<Long>> ids )  throws Exception
	{  
		return response( ids.get() ).applicationJson();  
	}


	@POST
	@Path("response/parse/ids")
	@Blocking
	@Produces(MediaType.APPLICATION_JSON) 
 	@Consumes(MediaType.APPLICATION_JSON)
 	@Operation(description = "Convert ids") 
	public ServerResponse<List<Long>> listConversion( ServerRequest request, @BeanParam List<Long> ids ) throws Exception
	{ 
		 
		return response( ids ).applicationJson(); 
		 

	}
	
	@GET
	@Path("response/parse/timestamp")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)  
 	@Operation(description = "Convert timestamp") 
	public ServerResponse<ByteBuffer> timestampConversion( ServerRequest request, @QueryParam("timestamp") Timestamp timestamp ) throws Exception
	{
		return response().body(timestamp.toString()).textPlain();
	}
	
	@GET
	@Path("response/parse/instant")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)  
 	@Operation(description = "Convert instant") 
	public ServerResponse<ByteBuffer> instantConversion( ServerRequest request, @QueryParam("instant") Instant instant ) throws Exception
	{ 
		 
		return response().body(instant.toString()).textPlain(); 
		 

	}
	
	@POST
	@Path("response/bytebuffer")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
 	@Operation(description = "Upload file path endpoint")
	public ServerResponse<ByteBuffer> responseUploadByteBuffer(ServerRequest request, @FormParam("file") ByteBuffer file ) throws Exception
	{ 
		 
		return response(file).applicationOctetStream();
		 

	}
	
	@POST
	@Path("response/file")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
 	@Operation(description = "Upload file path endpoint")
	public ServerResponse<ByteBuffer> responseUploadFile(ServerRequest request, @FormParam("file") File file ) throws Exception
	{ 
		
		ByteBuffer response = ByteBuffer.wrap(Files.asByteSource(file).read());
		
		 
		return response(response).applicationOctetStream();
		 

	}
	
	@GET
	@Path("response/debug")
 	@Operation(description = "Debug endpoint")
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
 	@Operation(description="Debug blocking endpoint")
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


	@GET
	@SecurityRequirement(name = "testRequirement")
	@Path("secure/resource")
	@Operation(description="Secure resource")
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse<Map<String,Object>> responseSecureContext()
	{
		Map<String,Object> responseMap = new HashMap<>();
		responseMap.put("secure",true);

		return response(responseMap);
	}
}
