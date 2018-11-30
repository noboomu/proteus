/**
 * 
 */
package io.sinistral.proteus.test.controllers;

import static io.sinistral.proteus.server.ServerResponse.response;

import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.test.models.User;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import io.undertow.server.HttpServerExchange;

/**
 * @author jbauer
 *
 */

@Tags({@Tag(name = "tests")})
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
	@Path("/exchange/json/serialize")
	@Operation(description = "Json serialization endpoint"  )
	public void exchangeJsonSerialize(HttpServerExchange exchange) 
	{  
		try
		{
			response( objectMapper.writeValueAsString(ImmutableMap.of("message", "Hello, World!")) ).applicationJson().send(exchange);
		} catch(Exception e)
		{
			response().badRequest(e);
		}
	}
	
	@GET
	@Path("/exchange/json/serializeToBytes")
	@Operation(description = "Json serialization with bytes endpoint"  )
	public void exchangeJsonSerializeToBytes(HttpServerExchange exchange)
	{ 
		try
		{
			response( objectMapper.writeValueAsString(ImmutableMap.of("message", "Hello, World!")) ).applicationJson().send(exchange);
		} catch(Exception e)
		{
			response().badRequest(e);
		}
	}
	

	
	@GET 
	@Path("/exchange/user/json")
	@Operation(description = "User serialization endpoint" )
	public void exchangeUserJson(HttpServerExchange exchange)
	{  
		response( new User(123L) ).applicationJson().send(exchange); 
	}
	
	@GET 
	@Path("/exchange/user/xml")
	@Produces((MediaType.APPLICATION_XML))
	@Operation(description = "User serialization endpoint"  )
	public void exchangeUserXml(HttpServerExchange exchange)
	{  
		response( new User(123L) ).applicationXml().send(exchange); 
	}

	@GET
	@Path("/response/user/json")
	@Operation(description = "User serialization endpoint"  )
	public ServerResponse<User> responseUserJson(ServerRequest request)
	{ 
 		User user = new User(123L);
		 
		return response( user ).applicationJson(); 
	}
	
	@GET
	@Path("/response/user/xml")
	@Produces((MediaType.APPLICATION_XML))
	@Operation(description = "User serialization endpoint"  )
	public ServerResponse<User> responseUserXml(ServerRequest request)
	{ 
 		User user = new User(123L);
		 
		return response( user ).applicationXml(); 
	}
	
	
	@GET
	@Path("/exchange/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@Operation(description = "Plaintext endpoint"  )
	public void exchangePlaintext(HttpServerExchange exchange)
	{ 
		response("Hello, World!").textPlain().send(exchange);

	}
	
	@GET
	@Path("/exchange/plaintext2")
	@Produces((MediaType.TEXT_PLAIN)) 
	@Operation(description = "Plaintext endpoint 2"  )
	public void exchangePlaintext2(HttpServerExchange exchange)
	{ 
		exchange.getResponseHeaders().put(io.undertow.util.Headers.CONTENT_TYPE, "text/plain");
	    exchange.getResponseSender().send(buffer.duplicate());
	}
	
	@GET
	@Path("/response/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@Operation(description = "Plaintext endpoint"  )
	public ServerResponse<ByteBuffer> responsePlaintext(ServerRequest request)
	{ 
		return response("Hello, World!").textPlain();

	}
	
	@GET
	@Path("/response/future/map")
	@Operation(description = "Future map endpoint"  )
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return CompletableFuture.completedFuture(response( map ).applicationJson());
	}
	
	@GET
	@Path("/response/map")
	@Operation(description = "Map endpoint"  )
	public ServerResponse<Map<String,String>> futureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return  response( map ).applicationJson();
	}
	
	@POST
	@Path("/response/file/path")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Operation(description = "Upload file path endpoint"  )
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{  
		return response(ByteBuffer.wrap(Files.toByteArray(file.toFile()))).applicationOctetStream(); 
	}
	
	@POST
	@Path("/response/file/path/optional")
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
	
	@POST
	@Path("/response/json/echo")
	@Produces(MediaType.APPLICATION_JSON) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Operation(description = "Echo json endpoint"  )
	public ServerResponse<User> responseEchoJson(ServerRequest request, @FormParam("user") User user ) throws Exception
	{  
		return response(user).applicationJson();
	}
	
	@POST
	@Path("/response/json/beanparam")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(description = "Echo json inner class endpoint"  )
	public ServerResponse<User> responseInnerClassTest(ServerRequest request, @BeanParam User user ) throws Exception
	{  
		return response(user).applicationJson();
	}
	
	  
	@GET
	@Path("/generic/set")
	@Produces((MediaType.APPLICATION_JSON)) 
	@Operation(description = "Generic set endpoint"  )
	public ServerResponse<Set<Long>>  genericSet( ServerRequest request, @QueryParam("ids") Set<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	
	  
	@GET
	@Path("/optional/set")
	@Produces((MediaType.APPLICATION_JSON)) 
	@Operation(description = "Generic optional set endpoint"  )
	public ServerResponse<Set<Long>>  genericOptionalSet( ServerRequest request, @QueryParam("ids") Optional<Set<Long>> ids )  throws Exception
	{  
		return response( ids.get() ).applicationJson();  
	}

	
	@GET
	@Path("/redirect/permanent")
	@Operation(description = "Permanent redirect endpoint"  )
	@Produces(MediaType.WILDCARD) 
	public ServerResponse<Void> testPermanentRedirect()
	{ 
		return response().redirectPermanently("https://google.com");
	}
	
	@GET
	@Path("/redirect")
	@Operation(description = "Redirect endpoint" )
	@Produces(MediaType.WILDCARD) 
	public ServerResponse<Void> testRedirect()
	{ 
		return response().redirect("https://google.com");
	}
	
	@POST
	@Path("/response/parse/ids")
	@Blocking
	@Produces(MediaType.APPLICATION_JSON) 
 	@Consumes(MediaType.APPLICATION_JSON)
 	@Operation(description = "Convert ids") 
	public ServerResponse<List<Long>> listConversion( ServerRequest request, @BeanParam List<Long> ids ) throws Exception
	{ 
		 
		return response( ids ).applicationJson(); 
		 

	}
	
	@POST
	@Path("/response/file/bytebuffer")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
 	@Operation(description = "Upload file path endpoint")
	public ServerResponse<ByteBuffer> responseUploadByteBuffer(ServerRequest request, @FormParam("file") ByteBuffer file ) throws Exception
	{ 
		 
		return response(file).applicationOctetStream();
		 

	}
	
	@GET
	@Path("/response/debug")
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
	@Path("/response/debug/blocking")
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
	@Path("/response/future/user") 
	@Operation(description="Future user endpoint")
	@Produces((MediaType.APPLICATION_JSON)) 
	public CompletableFuture<ServerResponse<User>> responseFutureUser()
	{ 
		return CompletableFuture.completedFuture(response( new User(123L) ).applicationJson() );
	}
	
	@GET
	@Path("/response/parameters/complex/{pathLong}")
	@Operation(description = "Complex parameters" )
	@Produces((MediaType.APPLICATION_JSON)) 
	public ServerResponse<Map<String,Object>> complexParameters(
	                    ServerRequest serverRequest, 
	                    @PathParam("pathLong")   Long pathLong, 
	                    @QueryParam("optionalQueryString")  Optional<String> optionalQueryString, 
	                    @QueryParam("optionalQueryLong")  Optional<Long> optionalQueryLong, 
	                    @QueryParam("optionalQueryDate") @ApiParam(format="date")  Optional<OffsetDateTime>  optionalQueryDate, 
	                    @QueryParam("optionalQueryUUID") Optional<UUID> optionalQueryUUID, 
	                    @HeaderParam("optionalHeaderUUID") Optional<UUID> optionalHeaderUUID,
	                    @QueryParam("optionalQueryEnum") Optional<User.UserType> optionalQueryEnum,
	                    @HeaderParam("optionalHeaderString") Optional<String> optionalHeaderString,
	                    @QueryParam("queryUUID") UUID queryUUID,  
	                    @HeaderParam("headerString") String headerString,
 	                    @QueryParam("queryEnum") User.UserType queryEnum, 
	                    @QueryParam("queryIntegerList")    List<Integer>  queryIntegerList, 
	                    @QueryParam("queryLong")   Long  queryLong
 	                    
	                    )
	{
 			
		Map<String,Object> responseMap = new HashMap<>();
		
		responseMap.put("optionalQueryString", optionalQueryString.orElse(null));
		responseMap.put("optionalQueryLong", optionalQueryLong.orElse(null));
	 	responseMap.put("optionalQueryDate", optionalQueryDate.map(OffsetDateTime::toString).orElse(null));
		responseMap.put("optionalQueryUUID", optionalQueryUUID.map(UUID::toString).orElse(null));
		responseMap.put("optionalHeaderUUID", optionalHeaderUUID.map(UUID::toString).orElse(null));
		responseMap.put("optionalHeaderString", optionalHeaderString.orElse(null));
		responseMap.put("optionalQueryEnum", optionalQueryEnum.orElse(null));
		responseMap.put("queryEnum", queryEnum);
		responseMap.put("queryUUID", queryUUID.toString()); 
		responseMap.put("queryLong", queryLong);
		responseMap.put("pathLong", pathLong);
		responseMap.put("headerString", headerString); 
		responseMap.put("queryIntegerList", queryIntegerList); 
		return response(responseMap).applicationJson(); 
	}
}
