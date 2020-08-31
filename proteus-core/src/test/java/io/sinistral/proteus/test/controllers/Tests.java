/**
 * 
 */
package io.sinistral.proteus.test.controllers;

import static io.sinistral.proteus.server.ServerResponse.response;
import static io.sinistral.proteus.test.wrappers.TestWrapper.DEBUG_TEST_KEY;

import java.io.File;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.exceptions.ServerException;
import io.sinistral.proteus.test.models.User;

import io.sinistral.proteus.test.wrappers.TestClassWrapper;
import io.sinistral.proteus.test.wrappers.TestWrapper;
import io.undertow.server.HttpServerExchange;

/**
 * @author jbauer
 *
 */


@SuppressWarnings("ALL")
@Path("/tests")
@Produces((MediaType.APPLICATION_JSON)) 
@Consumes((MediaType.MEDIA_TYPE_WILDCARD)) 
@Singleton
@Chain({TestClassWrapper.class})
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
	@Path("exchange/user/json")
	public void exchangeUserJson(HttpServerExchange exchange)
	{  
		response( new User(123L) ).applicationJson().send(exchange); 
	}
	
	@GET 
	@Path("exchange/user/xml")
	@Produces((MediaType.APPLICATION_XML))
	public void exchangeUserXml(HttpServerExchange exchange)
	{  
		response( new User(123L) ).applicationXml().send(exchange); 
	}

	@GET
	@Path("response/user/json")
	public ServerResponse<User> responseUserJson(ServerRequest request)
	{ 
 		User user = new User(123L);
		 
		return response( user ).applicationJson(); 
	}
	
	@GET
	@Path("response/user/xml")
	@Produces((MediaType.APPLICATION_XML))
	public ServerResponse<User> responseUserXml(ServerRequest request)
	{ 
 		User user = new User(123L);
		 
		return response( user ).applicationXml(); 
	}
	
	
	@GET
	@Path("exchange/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	public void exchangePlaintext(HttpServerExchange exchange)
	{ 
		response("Hello, World!").textPlain().send(exchange);

	}
	
	@GET
	@Path("exchange/plaintext2")
	@Produces((MediaType.TEXT_PLAIN)) 
	public void exchangePlaintext2(HttpServerExchange exchange)
	{ 
		exchange.getResponseHeaders().put(io.undertow.util.Headers.CONTENT_TYPE, "text/plain");
	    exchange.getResponseSender().send(buffer.duplicate());
	}
	
	@GET
	@Path("response/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	public ServerResponse<ByteBuffer> responsePlaintext(ServerRequest request)
	{ 
		return response("Hello, World!").textPlain();

	}
	
	@GET
	@Path("response/future/map")
	public CompletableFuture<Map<String,String>> responseFutureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return CompletableFuture.completedFuture(map);
	}

	@GET
	@Path("response/map")
	public ServerResponse<Map<String,String>> futureMap( ServerRequest request )
	{ 
		Map<String,String> map = ImmutableMap.of("message", "success");
		return  response( map ).applicationJson();
	}
	
	@POST
	@Path("response/file/path")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{  
		return response(ByteBuffer.wrap(Files.toByteArray(file.toFile()))).applicationOctetStream(); 
	}
	
	@POST
	@Path("response/file/path/optional")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
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
	@Path("response/json/echo")
	@Produces(MediaType.APPLICATION_JSON) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ServerResponse<User> responseEchoJson(ServerRequest request, @FormParam("user") User user ) throws Exception
	{  
		return response(user).applicationJson();
	}
	
	@POST
	@Path("response/json/beanparam")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.APPLICATION_JSON)
	public ServerResponse<User> responseInnerClassTest(ServerRequest request, @BeanParam User user ) throws Exception
	{  
		return response(user).applicationJson();
	}
	
	  
	@GET
	@Path("generic/set")
	@Produces((MediaType.APPLICATION_JSON))
	public ServerResponse<Set<Long>>  genericSet( ServerRequest request, @QueryParam("ids") Set<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	
	  
	@POST
	@Path("generic/set/bean")
	@Produces((MediaType.APPLICATION_JSON)) 
 	@Consumes(MediaType.APPLICATION_JSON)
	public ServerResponse<Set<Long>>  genericBeanSet( ServerRequest request, @BeanParam Set<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}

	@POST
	@Path("generic/map/bean")
	@Produces((MediaType.APPLICATION_JSON))
 	@Consumes(MediaType.APPLICATION_JSON)
	public ServerResponse<Map<String,Long>>  genericBeanMap( ServerRequest request, @BeanParam Map<String,Long> ids )  throws Exception
	{
		return response( ids ).applicationJson();
	}
	
	
	@POST
	@Path("generic/list/bean")
	@Produces((MediaType.APPLICATION_JSON)) 
 	@Consumes(MediaType.APPLICATION_JSON)
	public ServerResponse<List<Long>>  genericBeanList( ServerRequest request, @BeanParam List<Long> ids )  throws Exception
	{  
		return response( ids ).applicationJson(); 
	}
	  
	@GET
	@Path("optional/set")
	@Produces((MediaType.APPLICATION_JSON))
	public ServerResponse<Set<Long>>  genericOptionalSet( ServerRequest request, @QueryParam("ids") Optional<Set<Long>> ids )  throws Exception
	{  
		return response( ids.get() ).applicationJson();  
	}

	
	@GET
	@Path("redirect/permanent")
	@Produces(MediaType.WILDCARD) 
	public ServerResponse<Void> testPermanentRedirect()
	{ 
		return response().redirectPermanently("https://google.com");
	}
	
	@GET
	@Path("redirect")
	@Produces(MediaType.WILDCARD) 
	public ServerResponse<Void> testRedirect()
	{ 
		return response().redirect("https://google.com");
	}
	
	@POST
	@Path("response/parse/ids")
	@Blocking
	@Produces(MediaType.APPLICATION_JSON) 
 	@Consumes(MediaType.APPLICATION_JSON)
	public ServerResponse<List<Long>> listConversion( ServerRequest request, @BeanParam List<Long> ids ) throws Exception
	{ 
		 
		return response( ids ).applicationJson(); 
		 

	}
	
	@GET
	@Path("response/parse/timestamp")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)
	public ServerResponse<ByteBuffer> timestampConversion( ServerRequest request, @QueryParam("timestamp") Timestamp timestamp ) throws Exception
	{ 
		 
		return response().body(timestamp.toString()).textPlain(); 
		 

	}


	@GET
	@Path("response/parse/double")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)
	public ServerResponse<ByteBuffer> doubleConversion( ServerRequest request, @QueryParam("value") Double value ) throws Exception
	{
		assert (value instanceof Double);

		return response().body(value.toString()).textPlain();
	}

	@GET
	@Path("response/parse/big-decimal")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)
	public ServerResponse<ByteBuffer> bigDecimalConversion( ServerRequest request, @QueryParam("value") BigDecimal value ) throws Exception
	{
		assert (value instanceof BigDecimal);

		return response().body(value.toString()).textPlain();
	}
	
	@GET
	@Path("response/parse/instant")
	@Blocking
	@Produces(MediaType.TEXT_PLAIN)
	public ServerResponse<ByteBuffer> instantConversion( ServerRequest request, @QueryParam("instant") Instant instant ) throws Exception
	{ 
		 
		return response().body(instant.toString()).textPlain(); 
		 

	}
	
	@POST
	@Path("response/bytebuffer")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
	public ServerResponse<ByteBuffer> responseUploadByteBuffer(ServerRequest request, @FormParam("file") ByteBuffer file ) throws Exception
	{ 
		 
		return response(file).applicationOctetStream();
		 

	}
	
	@POST
	@Path("response/file")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes("*/*")
	public ServerResponse<ByteBuffer> responseUploadFile(ServerRequest request, @FormParam("file") File file ) throws Exception
	{ 
		
		ByteBuffer response = ByteBuffer.wrap(Files.asByteSource(file).read());
		
		 
		return response(response).applicationOctetStream();
		 

	}






	@GET
	@Path("response/params/path/{param}")
	@Produces(MediaType.TEXT_PLAIN)
	public ServerResponse<ByteBuffer> pathParamEndpoint(ServerRequest request, @PathParam("param") String param) {

		return response(param).textPlain();
	}


	@GET
	@Path("response/debug")
	@Chain({TestWrapper.class})
	public ServerResponse<Map<String,String>> debugEndpoint(ServerRequest request)
	{  
		try
		{
			String testString = request.getExchange().getAttachment(DEBUG_TEST_KEY);

			Map<String,String> map = ImmutableMap.of("message", "Hello, World!","attachment",testString);
			
			return response( map ).applicationJson();

		} catch(Exception e)
		{
			return response().badRequest(e);
		}
	}

	// new ServerException("No entity found", Response.Status.NOT_FOUND);

	@GET
	@Path("response/error/404")
	public ServerResponse<Void> notFoundError(ServerRequest request, @QueryParam("test") Optional<String> param ) throws Exception
	{
		if(!param.isPresent()) {
			throw  new ServerException("No entity found", Response.Status.NOT_FOUND);
		}

		return response().notFound();

	}

	@GET
	@Path("response/max")
	public ServerResponse<ByteBuffer> maxValue(ServerRequest request, @QueryParam("param") @Max(100) Integer param ) throws Exception
	{
		return response().body(param.toString());

	}


	@GET
	@Path("response/min")
	public ServerResponse<ByteBuffer> minValue(ServerRequest request, @QueryParam("param") @Min(10) Integer param ) throws Exception
	{
		return response().body(param.toString());

	}


	@GET
	@Path("response/future/response")
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureResponseMap( ServerRequest request )
	{
		Map<String,String> map = ImmutableMap.of("message", "success");
		return CompletableFuture.completedFuture(response(map).applicationJson().ok());
	}


	@GET
	@Path("response/error/401")
	public ServerResponse<Void> unauthorizedError(ServerRequest request, @QueryParam("test") Optional<String> param ) throws Exception
	{
		if(!param.isPresent()) {
			throw  new ServerException("Unauthorized", Response.Status.UNAUTHORIZED);
		}

		return response().unauthorized();

	}

	@GET
	@Path("response/debug/blocking")
	@Blocking
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
	@Path("response/future/user")
	@Produces((MediaType.APPLICATION_JSON)) 
	public CompletableFuture<User> responseFutureUser()
	{ 
		return CompletableFuture.completedFuture( new User(123L)  );
	}
	
	@GET
	@Path("response/parameters/complex/{pathLong}")
	@Produces((MediaType.APPLICATION_JSON)) 
	public ServerResponse<Map<String,Object>> complexParameters(
	                    ServerRequest serverRequest, 
	                    @PathParam("pathLong")   Long pathLong, 
	                    @QueryParam("optionalQueryString")  Optional<String> optionalQueryString, 
	                    @QueryParam("optionalQueryLong")  Optional<Long> optionalQueryLong, 
	                    @QueryParam("optionalQueryDate")   Optional<OffsetDateTime>  optionalQueryDate,
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

	@GET
	@Path("secure/resource")
	@Produces(MediaType.APPLICATION_JSON)
	public ServerResponse<Map<String,Object>> responseSecureContext()
	{
		Map<String,Object> responseMap = new HashMap<>();
		responseMap.put("secure",true);

		return response(responseMap);
	}
}
