/**
 * 
 */
package io.sinistral.proteus.controllers;

import static io.sinistral.proteus.server.ServerResponse.response;

import java.io.ByteArrayOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import com.jsoniter.output.JsonStream;

import io.sinistral.proteus.models.User;
import io.sinistral.proteus.models.World;
import io.sinistral.proteus.server.ServerResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.undertow.server.HttpServerExchange;

/**
 * @author jbauer
 *
 */
@Api(tags="benchmark")
@Path("/benchmark")
@Produces((MediaType.APPLICATION_JSON)) 
@Consumes((MediaType.MEDIA_TYPE_WILDCARD)) 
@Singleton
public class Benchmarks
{

 	
	@GET
	@Path("/json")
	@ApiOperation(value = "Json serialization endpoint",   httpMethod = "GET" )
	public void json(HttpServerExchange exchange)
	{ 
		response( JsonStream.serialize(ImmutableMap.of("message", "Hello, World!")) ).applicationJson().send(exchange);
	}
	
	@GET
	@Path("/json2")
	@ApiOperation(value = "Json serialization endpoint",   httpMethod = "GET" )
	public void json2(HttpServerExchange exchange)
	{ 
		response( JsonStream.serializeToBytes(ImmutableMap.of("message", "Hello, World!")) ).applicationJson().send(exchange);
	}
	
	@GET 
	@Path("/world")
	@ApiOperation(value = "World serialization endpoint",   httpMethod = "GET", response = World.class )
	public void world(HttpServerExchange exchange)
	{ 
		JsonStream stream = JsonStream.localStream();
		World world = new World(123,123);
		try
		{
			world.serialize(stream);
			response( stream.toString() ).applicationJson().send(exchange);

			stream.close();

		} catch (Exception e)
		{
			 
		}
 		 
	}

	@GET
	@Path("/world2")
	@ApiOperation(value = "World serialization endpoint",   httpMethod = "GET" )
	public ServerResponse<World> world2()
	{ 
 		World world = new World(123,123);
		 
		return response( world ).applicationJson(); 
	}
	
	@GET
	@Path("/world3")
	@ApiOperation(value = "World serialization endpoint",   httpMethod = "GET" , response = World.class)
	public void world3(HttpServerExchange exchange)
	{ 
 		World world = new World(123,123);
		 
		response( JsonStream.serialize(world) ).applicationJson().send(exchange);
 
 		 
	}

	
	@GET
	@Path("/plaintext")
	@Produces((MediaType.TEXT_PLAIN)) 
	@ApiOperation(value = "Plaintext endpoint",   httpMethod = "GET" )
	public void plaintext(HttpServerExchange exchange)
	{ 
		response("Hello, World!").textPlain().send(exchange);

	}
}
