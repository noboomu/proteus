/**
 * 
 */
package io.sinistral.proteus.test.controllers;

import static io.sinistral.proteus.server.ServerResponse.response;
import static io.sinistral.proteus.test.wrappers.TestWrapper.DEBUG_TEST_KEY;

import java.io.File;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import com.google.common.io.Files;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.annotations.Chain;
import io.sinistral.proteus.annotations.Debug;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.exceptions.ServerException;
import io.sinistral.proteus.test.models.User;

import io.sinistral.proteus.test.wrappers.TestClassWrapper;
import io.sinistral.proteus.test.wrappers.TestWrapper;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger logger = LoggerFactory.getLogger(Tests.class.getName());

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
	@Debug
	@Path("response/file/path")
	@Produces(MediaType.APPLICATION_OCTET_STREAM) 
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public ServerResponse<ByteBuffer> responseUploadFilePath(ServerRequest request, @FormParam("file") java.nio.file.Path file ) throws Exception
	{

		FormData.FormValue formValue = request.files("file").getFirst();

		logger.info("path: {} file: {} files: {}",file,file.toFile(), formValue.getFileName());


		File f = file.toFile();

		File f2 = java.io.File.createTempFile("test", "mp4");

		if(!f2.exists())
		{
			f2.createNewFile();
		}

		logger.info("responseUploadFilePath: {} {} {}",f.getAbsolutePath(), f.length(), f.getName());

		Files.copy(f, f2);



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

	@POST
	@Path("response/json/beanparam-optional")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
 	@Consumes(MediaType.APPLICATION_JSON)
	public ServerResponse<User> responseInnerClassOptionalTest(ServerRequest request, @BeanParam Optional<User> user ) throws Exception
	{
		return response(user.orElse(null)).applicationJson();
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
	@Path("generic/bean")
	@Produces((MediaType.APPLICATION_JSON)) 
 	@Consumes(MediaType.APPLICATION_JSON)
	public ServerResponse<GenericBean<Long>>  genericBeanList( ServerRequest request, @BeanParam GenericBean<Long> genericBean )  throws Exception
	{  
		return response( genericBean ).applicationJson();
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
		return response().redirectPermanently("v1/response/debug/blocking");
	}
	
	@GET
	@Path("redirect")
	@Produces(MediaType.WILDCARD) 
	public ServerResponse<Void> testRedirect()
	{ 
		return response().redirect("v1/response/debug/blocking");
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

	@POST
	@Path("list/file")
	 @Produces(MediaType.APPLICATION_JSON)
 	@Consumes("*/*")
	@Blocking
	public ServerResponse<Map<String,String>> uploadMultipleFileList(ServerRequest request, @FormParam("files") List<File> files, @FormParam("names") List<String> names ) throws Exception
	{

		Map<String,String> map = new HashMap<>();

		for(int i = 0; i < files.size(); i++)
		{
			map.put(names.get(i),files.get(i).getTotalSpace()+"");
		}


		return response(map).applicationJson();


	}

	@POST
	@Path("list/path")
	 @Produces(MediaType.APPLICATION_JSON)
 	@Consumes("*/*")
	@Blocking
	public ServerResponse<Map<String,String>> uploadMultiplePathList(ServerRequest request, @FormParam("files") List<java.nio.file.Path> files, @FormParam("names") List<String> names ) throws Exception
	{

		Map<String,String> map = new HashMap<>();

		for(int i = 0; i < files.size(); i++)
		{
			map.put(names.get(i),files.get(i).toFile().getTotalSpace()+"");
		}


		return response(map).applicationJson();


	}

	@POST
	@Path("map/file")
	 @Produces(MediaType.APPLICATION_JSON)
 	@Consumes("*/*")
	@Blocking
	public ServerResponse<Map<String,String>> uploadMultipleFileMap(ServerRequest request, @FormParam("files") Map<String,File> files ) throws Exception
	{



		Map<String,String> map = new HashMap<>();

		for(String k : files.keySet())
		{
			map.put(k,files.get(k).getTotalSpace()+"");
		}


		return response(map).applicationJson();


	}

	@POST
	@Path("map/path")
	 @Produces(MediaType.APPLICATION_JSON)
 	@Consumes("*/*")
	@Blocking
	public ServerResponse<Map<String,String>> uploadMultiplePathMap(ServerRequest request, @FormParam("files") Map<String,java.nio.file.Path> files  ) throws Exception
	{

		Map<String,String> map = new HashMap<>();

		for(String k : files.keySet())
		{
			map.put(k,files.get(k).toFile().getTotalSpace()+"");
		}

		return response(map).applicationJson();


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
	@Path("response/badrequest/blocking")
	@Blocking
	public ServerResponse<Map<String,String>> badRequestBlocking(ServerRequest request)
	{

			return response().badRequest(new ServerException("Bad Request", Response.Status.BAD_REQUEST));

	}

	@GET
	@Path("response/badrequest")
	public ServerResponse<Map<String,String>> badRequest(ServerRequest request)
	{

			return response().badRequest(new ServerException("Bad Request", Response.Status.BAD_REQUEST));

	}

	@GET
	@Path("future/badrequest")
	@Produces((MediaType.APPLICATION_JSON))
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureBadRequest(ServerRequest request)
	{

		CompletableFuture<ServerResponse<Map<String,String>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response().badRequest(new ServerException("Bad request", Response.Status.BAD_REQUEST)));

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
	}

	@GET
	@Path("future/badrequest/blocking")
	@Produces((MediaType.APPLICATION_JSON))
	@Blocking
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureBadRequestBlocking(ServerRequest request)
	{

		CompletableFuture<ServerResponse<Map<String,String>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response().badRequest(new ServerException("Bad request", Response.Status.BAD_REQUEST)));

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
	}

	@GET
	@Path("future/notfound/blocking")
	@Produces((MediaType.APPLICATION_JSON))
	@Blocking
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureNotFoundBlocking(ServerRequest request)
	{

		CompletableFuture<ServerResponse<Map<String,String>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response().notFound());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
	}


	@GET
	@Path("response/future/user")
	@Produces((MediaType.APPLICATION_JSON)) 
	public CompletableFuture<User> responseFutureUser()
	{ 
		return CompletableFuture.completedFuture( new User(123L)  );
	}

	@GET
	@Path("response/future/worker")
	@Produces((MediaType.APPLICATION_JSON))
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureUser(ServerRequest request)
	{

		CompletableFuture<ServerResponse<Map<String,String>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response(Map.of("status","OK")).applicationJson().ok());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
	}

	@GET
	@Path("response/future/worker/blocking")
	@Produces((MediaType.APPLICATION_JSON))
	@Blocking
	public CompletableFuture<ServerResponse<Map<String,String>>> responseFutureUserBlocking(ServerRequest request)
	{

		CompletableFuture<ServerResponse<Map<String,String>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response(Map.of("status","OK")).applicationJson().ok());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
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
	public ServerResponse<Map<String,Object>> responseSecureContext(ServerRequest request)
	{
		Map<String,Object> responseMap = new HashMap<>();
		responseMap.put("secure",true);

		return response(responseMap);
	}

	@POST
	@Path("multipart/bytebuffer")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	@Debug
	public ServerResponse<Map<String,Integer>> multipartUploadByteBuffer(ServerRequest request, @FormParam("buffer") ByteBuffer buffer ) throws Exception
	{

		return response(Map.of("size",buffer.array().length)).applicationJson().ok();


	}

	@POST
	@Path("multipart/future/bytebuffer")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public CompletableFuture<ServerResponse<Map<String,Integer>>> multipartFutureUploadByteBuffer(ServerRequest request, @FormParam("buffer") ByteBuffer buffer ) throws Exception
	{

		CompletableFuture<ServerResponse<Map<String,Integer>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response(Map.of("size",buffer.array().length)).applicationJson().ok());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;

	}


	@POST
	@Path("multipart/mixed")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Debug
	@Blocking
	public ServerResponse<Map<String,Object>> multipartUploadMixed(ServerRequest request, @FormParam("buffer") ByteBuffer buffer, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		return response(Map.of("buffer",buffer.array().length,"user",user,"userId",userId)).applicationJson().ok();


	}


	@POST
	@Path("multipart/future/mixed")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Debug
	@Blocking
	public CompletableFuture<ServerResponse<Map<String,Object>>> multipartUploadFutureMixed(ServerRequest request, @FormParam("buffer") ByteBuffer buffer, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		CompletableFuture<ServerResponse<Map<String,Object>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response(Map.of("buffer",buffer.array().length,"user",user,"userId",userId)).applicationJson().ok());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
	}

	@POST
	@Path("multipart/json")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public ServerResponse<JsonNode> multipartUploadJson(ServerRequest request, @FormParam("json") JsonNode node  ) throws Exception
	{

		return response(node).applicationJson().ok();


	}

	@POST
	@Path("multipart/future/json")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public CompletableFuture<ServerResponse<JsonNode>> multipartUploadFutureJson(ServerRequest request, @FormParam("json") JsonNode json ) throws Exception
	{

		CompletableFuture<ServerResponse<JsonNode>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response(json).applicationJson().ok());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;


	}


	@POST
	@Path("multipart/path-mixed")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public ServerResponse<Map<String,Object>> multipartUploadMixedWithPath(ServerRequest request, @FormParam("path") java.nio.file.Path path, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		return response(Map.of("path",path.toFile().length(),"user",user,"userId",userId)).applicationJson().ok();


	}


	@POST
	@Path("multipart/future/path-mixed")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public CompletableFuture<ServerResponse<Map<String,Object>>> multipartUploadFutureMixedWithPath(ServerRequest request, @FormParam("path") java.nio.file.Path path, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		CompletableFuture<ServerResponse<Map<String,Object>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response(Map.of("path",path.toFile().length(),"user",user,"userId",userId)).applicationJson().ok());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
	}

	@POST
	@Path("multipart/file-mixed")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public ServerResponse<Map<String,Object>> multipartUploadMixedWithFile(ServerRequest request, @FormParam("file") java.io.File file, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		return response(Map.of("file",file.length(),"user",user,"userId",userId)).applicationJson().ok();


	}


	@POST
	@Path("multipart/future/file-mixed")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public CompletableFuture<ServerResponse<Map<String,Object>>> multipartUploadFutureMixedWithFile(ServerRequest request, @FormParam("file") java.io.File file, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		CompletableFuture<ServerResponse<Map<String,Object>>> future = new CompletableFuture<>();

		request.getWorker().execute( () -> {

			try
			{

			    Thread.sleep(2000L);

			    future.complete(response(Map.of("file",file.length(),"user",user,"userId",userId)).applicationJson().ok());

			} catch( Exception e )
			{
			    future.completeExceptionally(e);
			}
		});

		return future;
	}

	@POST
	@Path("multipart/multiple-buffers")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public ServerResponse<Map<String,Object>> multipartUploadMultipleBuffers(ServerRequest request, @FormParam("file1") ByteBuffer file1,@FormParam("file2") ByteBuffer file2,@FormParam("file3") ByteBuffer file3, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{


		return response(Map.of("file1",file1.array().length,"file2",file2.array().length,"file3",file3.array().length,"user",user,"userId",userId)).applicationJson().ok();


	}

	@POST
	@Path("multipart/multiple-files")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public ServerResponse<Map<String,Object>> multipartUploadMultipleFiles(ServerRequest request, @FormParam("file1") File file1,@FormParam("file2") File file2,@FormParam("file3") File file3, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		return response(Map.of("file1",file1.length(),"file2",file2.length(),"file3",file3.length(),"user",user,"userId",userId)).applicationJson().ok();


	}

	@POST
	@Path("multipart/multiple-paths")
 	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public ServerResponse<Map<String,Object>> multipartUploadMultipleFiles(ServerRequest request, @FormParam("file1") java.nio.file.Path file1,@FormParam("file2") java.nio.file.Path file2,@FormParam("file3") java.nio.file.Path file3, @FormParam("user") User user, @FormParam("userId") Integer userId ) throws Exception
	{

		return response(Map.of("file1",file1.toFile().length(),"file2",file2.toFile().length(),"file3",file3.toFile().length(),"user",user,"userId",userId)).applicationJson().ok();


	}

	@GET
	@Path("headers/last-modified")
	@Produces(MediaType.APPLICATION_JSON)
	@Blocking
	public ServerResponse<Map<String,String>> lastModifiedResponse(ServerRequest request)
	{
		return response(Map.of("key","value")).lastModified(Instant.now());

	}


}
