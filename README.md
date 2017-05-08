# PROTEUS

An extremely lightweight, flexible, and fast [Swagger](http://swagger.io/) first REST API framework atop [Undertow](http://undertow.io). 
A great deal of inspiration came from working with the following excellent projects: [Play](http://playframework.com), [Jooby](http://jooby.org), and [light-4j](https://github.com/networknt/light-4j).

### Motivation
  - Several years of working with the [Play](http://playframework.com) framework convinced us there had to be a better way.
  - We faced a Goldilocks Crisis with the existing alternatives: [Jooby](http://jooby.org) did too much, [light-4j](https://github.com/networknt/light-4j) didn't do quite enough.
  - We needed a framework that enabled us to write clean MVC REST controllers that created Swagger docs we could plug directly into the existing [codegen](https://github.com/swagger-api/swagger-codegen) solutions.
  - We needed a framework with minimal overhead and performance at or near that of raw [Undertow](http://undertow.io).
 
### Built With
 - [Undertow](http://undertow.io) (server)
 - [Guice](https://github.com/google/guice) (di)
 - [Java Runtime Compiler](https://github.com/OpenHFT/Java-Runtime-Compiler) (runtime generated class compilation)
 - [javapoet](https://github.com/square/javapoet) (runtime class generation)
 - [Jackson](https://github.com/FasterXML/jackson-dataformat-xml) (xml)
 - [jsoniter](http://jsoniter.com/) (json)
 - [Logback](https://logback.qos.ch/) (logging)
 - [Typesafe Config](https://github.com/typesafehub/config) (config)
 - [Swagger](http://swagger.io/) (annotations and swagger spec)
 - [jax-rs](http://docs.oracle.com/javaee/6/api/javax/ws/rs/package-summary.html) (annotations only)

### Dependencies
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3](http://maven.apache.org/)

### Setup
  - We are very impressed by what Jooby has done with server configuration  
  - Parameters are all configured in the ```conf/application.conf``` file 
  - Proteus applications generally have a main method that creates an instance of ```io.sinistral.proteus.Application``` 
  - The user adds ```Service``` and ```Module``` classes to the application instance via ```addService``` and ```addModule``` methods prior to calling ```start``` 

### Getting Started
 - COMING SOON
 
### Running

    mvn exec:exec

  
