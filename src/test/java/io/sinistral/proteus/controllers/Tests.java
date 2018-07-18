/**
 * 
 */
package io.sinistral.proteus.controllers;

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
import io.sinistral.proteus.models.User;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.undertow.server.HttpServerExchange;

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
	@Path("/exchange/json/serialize")
	@ApiOperation(value = "Json serialization endpoint",   httpMethod = "GET" )
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
	@ApiOperation(value = "Json serialization with bytes endpoint",   httpMethod = "GET" )
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
	@ApiOperation(value = "User serialization endpoint",   httpMethod = "GET", response = User.class )
	public void exchangeUserJson(HttpServerExchange exchange)
	{  
		response( new User(123L) ).applicationJson().send(exchange); 
	}
	
	@GET 
	@Path("/exchange/user/xml")
	@Produces((MediaType.APPLICATION_XML))
	@ApiOperation(value = "User serialization endpoint",   httpMethod = "GET", response = User.class )
	public void exchangeUserXml(HttpServerExchange exchange)
	{  
		response( new User(123L) ).applicationXml().send(exchange); 
	}

	@GET
	@Path("/response/user/json")
	@ApiOperation(value = "User serialization endpoint",   httpMethod = "GET" )
	public ServerResponse<User> responseUserJson(ServerRequest request)
	{ 
 		User user = new User(123L);
		 
		return response( user ).applicationJson(); 
	}
	
	@GET
	@Path("/response/user/xml")
	@Produces((MediaType.APPLICATION_XML))
	@ApiOperation(value = "User serialization endpoint",   httpMethod = "GET" )
	public ServerResponse<User> responseUserXml(ServerRequest request)
	{ 
 		User user = new User(123L);
		 
		return response( user ).applicationXml(); 
	}
	
	
	@GET
	@Path("/exchange/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value = "Plaintext endpoint",   httpMethod = "GET" )
	public void exchangePlaintext(HttpServerExchange exchange)
	{ 
		response("Hello, World!").textPlain().send(exchange);

	}
	
	@GET
	@Path("/exchange/plaintext2")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value = "Plaintext endpoint 2",   httpMethod = "GET" )
	public void exchangePlaintext2(HttpServerExchange exchange)
	{ 
		exchange.getResponseHeaders().put(io.undertow.util.Headers.CONTENT_TYPE, "text/plain");
	    exchange.getResponseSender().send(buffer.duplicate());
	}
	
	@GET
	@Path("/response/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value = "Plaintext endpoint",   httpMethod = "GET" )
	public ServerResponse<ByteBuffer> responsePlaintext(ServerRequest request)
	{ 
		return response("Hello, World!").textPlain();

	}
	
	@GET
	@Path("/response/future/map")
	@ApiOperation(value = "Future map endpoint",   httpMethod = "GET" )
	public CompletableFuture<ServerResponse<ImmutableMap<String,String>>> responseFutureMap()
	{ 
		return CompletableFuture.completedFuture(response( ImmutableMap.of("message", "success") ).applicationJson());
	}
	
	@POST
	@Path("/response/file/path")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
	@ApiOperation(value = "Upload file path endpoint",   httpMethod = "POST" )
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{  
		return response(ByteBuffer.wrap(Files.toByteArray(file.toFile()))).applicationOctetStream(); 
	}
	
	@POST
	@Path("/response/json/echo")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
	@ApiOperation(value = "Echo json endpoint",   httpMethod = "POST" )
	public ServerResponse<User> responseEchoJson(ServerRequest request, @FormParam("user") User user ) throws Exception
	{  
		return response(user).applicationJson();
	}
	
	@POST
	@Path("/response/json/innerClass")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
	@ApiOperation(value = "Echo json inner class endpoint",   httpMethod = "POST" )
	public ServerResponse<User.InnerUserModel> responseInnerClassTest(ServerRequest request, @BeanParam User.InnerUserModel userInnerModel ) throws Exception
	{  
		return response(userInnerModel).applicationJson();
	}
	
	  
	@GET
	@Path("/generic/set")
	@ApiOperation(value = "Generic set endpoint",   httpMethod = "GET" )
	public ServerResponse<Set<Long>>  genericSet( ServerRequest request, @QueryParam("ids") Set<Long> ids )  throws Exception
	{  
		 
			return response( ids ).applicationJson(); 
		 
	}
	
	  
	@GET
	@Path("/optional/set")
	@ApiOperation(value = "Generic optional set endpoint",   httpMethod = "GET" )
	public ServerResponse<Set<Long>>  genericOptionalSet( ServerRequest request, @QueryParam("ids") Optional<Set<Long>> ids )  throws Exception
	{  
		 
			return response( ids.get() ).applicationJson(); 
		 
	}
	
	
	@GET
	@Path("/redirect")
	@ApiOperation(value = "Redirect endpoint",   httpMethod = "GET" )
	public ServerResponse<?> testRedirect()
	{ 
		return response().redirect("https://google.com");
	}
	
	@POST
	@Path("/response/parse/ids")
	@Blocking
	@Produces(MediaType.APPLICATION_JSON) 
 	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Convert ids",   httpMethod = "POST" )
	public ServerResponse<List<Long>> listConversion( ServerRequest request, @BeanParam List<Long> ids ) throws Exception
	{ 
		 
		return response( ids ).applicationJson(); 
		 

	}
	
	@POST
	@Path("/response/file/bytebuffer")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
	@ApiOperation(value = "Upload file path endpoint",   httpMethod = "POST" )
	public ServerResponse<ByteBuffer> responseUploadByteBuffer(ServerRequest request, @FormParam("file") ByteBuffer file ) throws Exception
	{ 
		 
		return response(file).applicationOctetStream();
		 

	}
	
	
	@GET
	@Path("/response/future/user")
	@ApiOperation(value = "Future user endpoint",   httpMethod = "GET" )
	public CompletableFuture<ServerResponse<User>> responseFutureUser()
	{ 
		return CompletableFuture.completedFuture(response( new User(123L) ).applicationJson() );
	}
	
	@GET
	@Path("/response/parameters/complex/{pathLong}")
	@ApiOperation(value = "Complex parameters", httpMethod = "GET")
	public ServerResponse<Map<String,Object>> complexParameters(
	                    final ServerRequest serverRequest, 
	                    @PathParam("pathLong") final Long pathLong, 
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
