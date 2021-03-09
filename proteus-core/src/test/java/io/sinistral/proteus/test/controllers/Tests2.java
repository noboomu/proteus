/**
 * 
 */
package io.sinistral.proteus.test.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
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

import static io.sinistral.proteus.server.ServerResponse.response;
import static io.sinistral.proteus.test.wrappers.TestWrapper.DEBUG_TEST_KEY;

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
public class Tests2
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

}
