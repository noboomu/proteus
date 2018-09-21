
![Alt logo](https://cdn.rawgit.com/noboomu/proteus/master/src/main/resources/io/sinistral/proteus/proteus-logo.svg)

An extremely __lightweight, flexible, and high performance__ [Undertow](http://undertow.io) based Java framework for developing and running microservices.

__NO MAGIC.__

JAX-RS compliant.

Verifiably [FAST](https://www.techempower.com/benchmarks/): [The latest benchmarks](https://www.techempower.com/benchmarks/) show Proteus outperforming 99% of other web frameworks. 

Inspired by [Play](http://playframework.com), [Jooby](http://jooby.org), and [light-4j](https://github.com/networknt/light-4j).



Getting Started
---------------

- Make sure you have a JDK >= 8 and a current version of Maven installed.
- Copy and paste into your terminal:
```
/bin/bash -e <(curl -fsSL https://raw.githubusercontent.com/noboomu/proteus-example/master/scripts/quickStart.sh)
```

- Open [http://localhost:8090/v1/swagger](http://localhost:8090/v1/swagger) in your browser.
- Open [http://localhost:8090/v1/swagger/redoc](http://localhost:8090/v1/swagger/redoc) for a pretty version of your API.


Controllers
---------------

### Supported Controller Annotations

Controller classes respect standard Swagger / JAX-RS annotations:
```java
@Api(tags="benchmarks")
@Path("/benchmarks")
@Produces((MediaType.APPLICATION_JSON)) 
@Consumes((MediaType.MEDIA_TYPE_WILDCARD))
```

### Supported Method Annotations

Controller class methods respect standard Swagger / JAX-RS annotations:
```java
@GET
@Path("/plaintext")
@Produces((MediaType.TEXT_PLAIN)) 
@ApiOperation(value = "Plaintext endpoint",   httpMethod = "GET" )
public void plaintext(HttpServerExchange exchange)
{ 
	response("Hello, World!").contentType(PLAINTEXT_TYPE).send(exchange);
}
```
In addition, the `io.sinistral.proteus.annotations.Blocking` annotation can be used to explicitly mark a method for blocked request handling. 

## Return Types

##### Performance
For total control and maximum performance the raw `HttpServerExchange` can be passed to the controller method.

Methods that take an `HttpServerExchange` as an argument should __not__ return a value.

In this case the method takes on __full responsibility__ for completing the exchange.
 
##### Convenience
The static method ```io.sinistral.proteus.server.ServerResponse.response``` helps create ```ServerResponse<T>``` instances that are the preferred return type for controller methods.

If the response object's `contentType` is not explicitly set, the `@Produces` annotation is used in combination with the `Accept` headers to determine the `Content-Type`.

For methods that should return a `String` or `ByteBuffer` to the client users can create responses like this:
 ```java
@GET
@Path("/hello-world")
@Produces((MediaType.TEXT_PLAIN)) 
@ApiOperation(value = "Serve a plaintext message using a ServerResponse")
public ServerResponse<ByteBuffer> plaintext(String message)
{ 
	return ServerResponse.response("Hello, World!").contentType(PLAINTEXT_TYPE);
}
```
By default, passing a `String` to the static `ServerResponse.response` helper function will convert it into a `ByteBuffer`.

For other types of responses the following demonstrates the preferred style:
```java
@GET
@Path("/world")
@Produces((MediaType.APPLICATION_JSON)) 
@ApiOperation(value = "Return a world JSON object",   httpMethod = "GET", response=World.class )
public io.sinistral.proteus.server.ServerResponse<World> getWorld(Integer id,  Integer randomNumber )
{ 
	return io.sinistral.proteus.server.ServerResponse.response(new World(id,randomNumber));
}
```
The entity can be set separately as well:
> this disables static type checking!
```java
@GET
@Path("/world")
@Produces((MediaType.APPLICATION_JSON)) 
@ApiOperation(value = "Return a world JSON object",   httpMethod = "GET", response=World.class )
public io.sinistral.proteus.server.ServerResponse getWorld(Integer id,  Integer randomNumber )
{ 
	return io.sinistral.proteus.server.ServerResponse.response().entity(new World(id,randomNumber));
}
```
`CompletableFuture<ServerResponse<T>>` can also be used as a response type:
```java
@GET
@Path("/future/user")
@ApiOperation(value = "Future user endpoint",   httpMethod = "GET" )
public CompletableFuture<ServerResponse<User>> futureUser()
{ 
	return CompletableFuture.completedFuture(response( new User(123L) ).applicationJson() );
}
```
In this case a handler will be generated with the following source code:
```java
public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws java.lang.Exception { 
    CompletableFuture<ServerResponse<User>> response = examplesController.futureUser();
    response.thenAccept( r ->  r.applicationJson().send(this,exchange) )
    	.exceptionally( ex ->  {
      		throw new java.util.concurrent.CompletionException(ex);
  	} );
}
```

Controller Parameters
--------------

A ```io.sinistral.proteus.server.ServerRequest``` can be added as an endpoint parameter if the user wishes to access request properties that are not included in the parameter list.

Proteus is capable of parsing most types of endpoint parameters automatically so long as the type has a ```fromString```, ```valueOf```, or can be deserialized from JSON.

Multipart/Form file uploads can be passed to the endpoint methods as a ```java.io.File```, a ```java.nio.Files.Path```, or a ```java.nio.ByteBuffer```.

Optional parameters are also supported, here is a more complex endpoint demonstrating several parameter types:
```java
@GET
@Path("/response/parameters/complex/{pathLong}")
@ApiOperation(value = "Complex parameters", httpMethod = "GET")
public ServerResponse<Map<String,Object>> complexParameters(
        final ServerRequest serverRequest, 
        @PathParam("pathLong") final Long pathLong, 
        @QueryParam("optionalQueryString") Optional<String> optionalQueryString, 
        @QueryParam("optionalQueryLong") Optional<Long> optionalQueryLong, 
        @QueryParam("optionalQueryDate") Optional<OffsetDateTime> optionalQueryDate, 
        @QueryParam("optionalQueryUUID") Optional<UUID> optionalQueryUUID, 
        @HeaderParam("optionalHeaderUUID") Optional<UUID> optionalHeaderUUID,
        @QueryParam("optionalQueryEnum") Optional<User.UserType> optionalQueryEnum,
        @HeaderParam("optionalHeaderString") Optional<String> optionalHeaderString,
        @QueryParam("queryUUID") UUID queryUUID,  
        @HeaderParam("headerString") String headerString,
        @QueryParam("queryEnum") User.UserType queryEnum, 
	    @QueryParam("queryIntegerList") List<Integer> queryIntegerList, 
	    @QueryParam("queryLong") Long queryLong 
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
```
Services
-------------

Proteus comes with two standard services that extend the ```io.sinistral.proteus.services.BaseService``` class.
## AssetsService

The AssetsService mounts an asset directory at a given path and is configured in your ```application.conf``` file.

The default configuration:
```
assets {
    # the base path assets will be server from
    path = "/public"
    # the directory to load the assets from
    dir = "./assets"
    cache {
    # cache timeout for the assets
        time = 500
    }
}
```
## SwaggerService   

The SwaggerService generates a swagger-spec file from your endpoints and serves a swagger-ui and spec.

The service is configured in your ```application.conf``` file.

The default configuration:
```
swagger {
    # the path that has an index.html template and theme css files
    resourcePrefix="io/sinistral/proteus/swagger"
    # swagger version
    swagger: "2.0"
    info {
        # swagger info title
        title = ${application.name}
        # swagger info version
        version = ${application.version}
    }
    # swagger-ui theme from ostranmes swagger-ui-themes, the following are built-in 
    # [feeling-blue, flattop, material, monokai, muted, newspaper, outline]
    # specifying a different name causes the SwaggerService to search in 
    # {swagger.resourcePrefix}/themes for a file named "theme-{swagger.theme}.css"
    theme="default"
    # where the swagger endpoints will be mounted
    basePath= ${application.path}"/swagger"
    #the name of the spec file
    specFilename="swagger.json"
    consumes = ["application/json"]
    produces = ["application/json"]
    # supported schemes
    schemes = ["http"]
    }
```
 
Under the Hood
---------------

Proteus takes your MVC controller classes and methods decorated with Swagger / JAX-RS annotations and generates native Undertow handler classes at runtime. 


By default, the configuration is loaded into a `com.typesafe.config.Config` from a file at `conf/application.conf`.

`@Named` annotated properties of `Module`, `Service` and controller classes are bound to values found in the configuration.

Proteus applications generally have a main method that creates an instance of `io.sinistral.proteus.ProteusApplication`. 

Prior to calling `start` on the `ProteusApplication` instance:
* Register `Service` classes via `addService`  
* Register `Module` classes via `addModule`  
* Register classes annotated with `io.swagger.annotations.Api` via `addController`  

Out of the box you get a [Swagger UI](https://github.com/swagger-api/swagger-ui) at `/swagger` and [Redoc](https://github.com/Rebilly/ReDoc) at `/swagger/redoc`.

> A `Service` extends `com.google.common.util.concurrent.AbstractIdleService` or `io.sinistral.proteus.services.BaseService`.

> A `Module` implements `com.google.inject.Module`.

Examples
----------
Check out [this example](https://github.com/noboomu/proteus-example).
 

Motivation
----------
* Several years of working with the [Play](http://playframework.com) framework convinced us there had to be a better way.
* We faced a Goldilocks Crisis with the existing alternatives: [Jooby](http://jooby.org) did too much, [light-4j](https://github.com/networknt/light-4j) didn't do quite enough.
* We needed a framework that enabled us to write clean MVC REST controllers that created Swagger docs we could plug directly into the existing [codegen](https://github.com/swagger-api/swagger-codegen) solutions.
* We needed a framework with minimal overhead and performance at or near that of raw [Undertow](http://undertow.io).


Dependencies
----------
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3](http://maven.apache.org/)


Built With
----------
 - [Undertow](http://undertow.io) (server)
 - [Guice](https://github.com/google/guice) (di)
 - [Java Runtime Compiler](https://github.com/OpenHFT/Java-Runtime-Compiler) (runtime generated class compilation)
 - [javapoet](https://github.com/square/javapoet) (runtime class generation)
 - [Jackson](https://github.com/FasterXML/jackson-dataformat-xml) (xml)
 - [jsoniter](http://jsoniter.com/) (json)
 - [Logback](https://logback.qos.ch/) (logging)
 - [Typesafe Config](https://github.com/typesafehub/config) (config)
 - [Swagger](http://swagger.io/) (annotations and swagger spec)
 - [JAX-RS](http://docs.oracle.com/javaee/6/api/javax/ws/rs/package-summary.html) (annotations only)

  
