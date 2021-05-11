import static io.sinistral.proteus.server.Extractors.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.sinistral.proteus.test.controllers.Tests;
import io.sinistral.proteus.test.models.User;
import io.sinistral.proteus.test.wrappers.TestClassWrapper;
import io.sinistral.proteus.test.wrappers.TestWrapper;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import java.lang.String;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;
import java.util.function.Supplier;

public class TestsRouteSupplier implements Supplier<RoutingHandler> {
  protected final Tests testsController;

  protected final Map<String, HandlerWrapper> registeredHandlerWrappers;

  protected final TestWrapper ioSinistralProteusTestWrappersTestWrapper_1;

  protected final TestClassWrapper ioSinistralProteusTestWrappersTestClassWrapper;

  @Inject
  public TestsRouteSupplier(Tests testsController,
      @Named("registeredHandlerWrappers") Map<String, HandlerWrapper> registeredHandlerWrappers,
      TestWrapper ioSinistralProteusTestWrappersTestWrapper_1,
      TestClassWrapper ioSinistralProteusTestWrappersTestClassWrapper) {
    this.testsController = testsController;
    this.registeredHandlerWrappers = registeredHandlerWrappers;
    this.ioSinistralProteusTestWrappersTestWrapper_1 = ioSinistralProteusTestWrappersTestWrapper_1;
    this.ioSinistralProteusTestWrappersTestClassWrapper = ioSinistralProteusTestWrappersTestClassWrapper;
  }

  public RoutingHandler get() {
    final RoutingHandler router = new RoutingHandler();
    final TypeReference<java.util.Map<java.lang.String, java.lang.Long>> langLongMapTypeReference = new TypeReference<java.util.Map<java.lang.String, java.lang.Long>>(){};
    final TypeReference<java.util.List<java.lang.Long>> langLongListTypeReference = new TypeReference<java.util.List<java.lang.Long>>(){};
    final TypeReference<java.util.Set<java.lang.Long>> langLongSetTypeReference = new TypeReference<java.util.Set<java.lang.Long>>(){};
    final TypeReference<Timestamp> sqlTimestampTypeReference = new TypeReference<Timestamp>(){};
    final TypeReference<JsonNode> databindJsonNodeTypeReference = new TypeReference<JsonNode>(){};
    final TypeReference<Instant> timeInstantTypeReference = new TypeReference<Instant>(){};
    final TypeReference<User> modelsUserTypeReference = new TypeReference<User>(){};
    final TypeReference<BigDecimal> mathBigDecimalTypeReference = new TypeReference<BigDecimal>(){};
    HttpHandler currentHandler = null;

    final io.undertow.server.HttpHandler testsExchangeUserJsonHandler_1 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        testsController.exchangeUserJson(exchange);
      }
    };

    currentHandler = testsExchangeUserJsonHandler_1;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/exchange/user/json",currentHandler);

    final io.undertow.server.HttpHandler testsGenericSetHandler_2 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
        java.util.Set<java.lang.Long> ids = exchange.getQueryParameters().get("ids").stream().map(java.lang.Long::valueOf).collect(java.util.stream.Collectors.toSet());

        io.sinistral.proteus.server.ServerResponse<java.util.Set<java.lang.Long>> response = testsController.genericSet(request,ids);
        response.send(exchange);
      }
    };

    currentHandler = testsGenericSetHandler_2;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/generic/set",currentHandler);

    final io.undertow.server.HttpHandler testsGenericBeanSetHandler_3 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.Set<java.lang.Long> ids = io.sinistral.proteus.server.Extractors.model(exchange,langLongSetTypeReference);

          io.sinistral.proteus.server.ServerResponse<java.util.Set<java.lang.Long>> response = testsController.genericBeanSet(request,ids);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsGenericBeanSetHandler_3);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/generic/set/bean",currentHandler);

    final io.undertow.server.HttpHandler testsGenericBeanListHandler_4 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.List<java.lang.Long> ids = io.sinistral.proteus.server.Extractors.model(exchange,langLongListTypeReference);

          io.sinistral.proteus.server.ServerResponse<java.util.List<java.lang.Long>> response = testsController.genericBeanList(request,ids);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsGenericBeanListHandler_4);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/generic/list/bean",currentHandler);

    final io.undertow.server.HttpHandler testsGenericBeanMapHandler_5 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.Map<java.lang.String, java.lang.Long> ids = io.sinistral.proteus.server.Extractors.model(exchange,langLongMapTypeReference);

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Long>> response = testsController.genericBeanMap(request,ids);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsGenericBeanMapHandler_5);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/generic/map/bean",currentHandler);

    final io.undertow.server.HttpHandler testsExchangeUserXmlHandler_6 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        testsController.exchangeUserXml(exchange);
      }
    };

    currentHandler = testsExchangeUserXmlHandler_6;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/exchange/user/xml",currentHandler);

    final io.undertow.server.HttpHandler testsResponseUserJsonHandler_7 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        io.sinistral.proteus.server.ServerResponse<io.sinistral.proteus.test.models.User> response = testsController.responseUserJson(request);
        response.send(exchange);
      }
    };

    currentHandler = testsResponseUserJsonHandler_7;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/user/json",currentHandler);

    final io.undertow.server.HttpHandler testsResponseUserXmlHandler_8 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        io.sinistral.proteus.server.ServerResponse<io.sinistral.proteus.test.models.User> response = testsController.responseUserXml(request);
        response.send(exchange);
      }
    };

    currentHandler = testsResponseUserXmlHandler_8;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/user/xml",currentHandler);

    final io.undertow.server.HttpHandler testsBadRequestHandler_9 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.badRequest(request);
        response.send(exchange);
      }
    };

    currentHandler = testsBadRequestHandler_9;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/badrequest",currentHandler);

    final io.undertow.server.HttpHandler testsBadRequestBlockingHandler_10 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.badRequestBlocking(request);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsBadRequestBlockingHandler_10);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/badrequest/blocking",currentHandler);

    final io.undertow.server.HttpHandler testsExchangePlaintextHandler_11 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        testsController.exchangePlaintext(exchange);
      }
    };

    currentHandler = testsExchangePlaintextHandler_11;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/exchange/plaintext",currentHandler);

    final io.undertow.server.HttpHandler testsExchangePlaintext2Handler_12 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        testsController.exchangePlaintext2(exchange);
      }
    };

    currentHandler = testsExchangePlaintext2Handler_12;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/exchange/plaintext2",currentHandler);

    final io.undertow.server.HttpHandler testsResponsePlaintextHandler_13 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.responsePlaintext(request);
        response.send(exchange);
      }
    };

    currentHandler = testsResponsePlaintextHandler_13;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/plaintext",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureUserHandler_14 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>>> response = testsController.responseFutureUser(request);
        exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
          response.whenComplete( (r,ex) ->  {
            if(ex != null) {
                        exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                exchange.endExchange();
                } else {
                        r.send(exchange);}
          } );
        } );
      }
    };

    currentHandler = testsResponseFutureUserHandler_14;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/future/worker",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureUserHandler_15 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        java.util.concurrent.CompletableFuture<io.sinistral.proteus.test.models.User> response = testsController.responseFutureUser();
        exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
          response.whenComplete( (r,ex) ->  {
            if(ex != null) {
                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);
                exchange.setResponseCode(500);
                exchange.endExchange();
            } else {
                        io.sinistral.proteus.server.ServerResponse.response(r).applicationJson().send(exchange);}
          } );
        } );
      }
    };

    currentHandler = testsResponseFutureUserHandler_15;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/future/user",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureMapHandler_16 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        java.util.concurrent.CompletableFuture<java.util.Map<java.lang.String, java.lang.String>> response = testsController.responseFutureMap(request);
        exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
          response.whenComplete( (r,ex) ->  {
            if(ex != null) {
                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);
                exchange.setResponseCode(500);
                exchange.endExchange();
            } else {
                        io.sinistral.proteus.server.ServerResponse.response(r).applicationJson().send(exchange);}
          } );
        } );
      }
    };

    currentHandler = testsResponseFutureMapHandler_16;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/future/map",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureResponseMapHandler_17 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>>> response = testsController.responseFutureResponseMap(request);
        exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
          response.whenComplete( (r,ex) ->  {
            if(ex != null) {
                        exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                exchange.endExchange();
                } else {
                        r.send(exchange);}
          } );
        } );
      }
    };

    currentHandler = testsResponseFutureResponseMapHandler_17;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/future/response",currentHandler);

    final io.undertow.server.HttpHandler testsTestRedirectHandler_18 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        io.sinistral.proteus.server.ServerResponse<java.lang.Void> response = testsController.testRedirect();
        response.send(exchange);
      }
    };

    currentHandler = testsTestRedirectHandler_18;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/redirect",currentHandler);

    final io.undertow.server.HttpHandler testsMaxValueHandler_19 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
        Integer param = io.sinistral.proteus.server.Extractors.integerValue(exchange,"param");
        if( param > 100 ) {
          throw new io.sinistral.proteus.server.exceptions.ServerException("must be less than or equal to 100",javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }

        io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.maxValue(request,param);
        response.send(exchange);
      }
    };

    currentHandler = testsMaxValueHandler_19;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/max",currentHandler);

    final io.undertow.server.HttpHandler testsMinValueHandler_20 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
        Integer param = io.sinistral.proteus.server.Extractors.integerValue(exchange,"param");
        if( param < 10 ) {
          throw new io.sinistral.proteus.server.exceptions.ServerException("must be greater than or equal to 10",javax.ws.rs.core.Response.Status.BAD_REQUEST);
        }

        io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.minValue(request,param);
        response.send(exchange);
      }
    };

    currentHandler = testsMinValueHandler_20;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/min",currentHandler);

    final io.undertow.server.HttpHandler testsUploadMultipleFileListHandler_21 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.List<java.io.File> files = io.sinistral.proteus.server.Extractors.fileList(exchange,"files");
          java.util.List<java.lang.String> names = exchange.getQueryParameters().get("names").stream().map(java.lang.String::valueOf).collect(java.util.stream.Collectors.toList());

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.uploadMultipleFileList(request,files,names);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsUploadMultipleFileListHandler_21);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/list/file",currentHandler);

    final io.undertow.server.HttpHandler testsUploadMultiplePathListHandler_22 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.List<java.nio.file.Path> files = io.sinistral.proteus.server.Extractors.pathList(exchange,"files");
          java.util.List<java.lang.String> names = exchange.getQueryParameters().get("names").stream().map(java.lang.String::valueOf).collect(java.util.stream.Collectors.toList());

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.uploadMultiplePathList(request,files,names);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsUploadMultiplePathListHandler_22);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/list/path",currentHandler);

    final io.undertow.server.HttpHandler testsUploadMultipleFileMapHandler_23 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.Map<java.lang.String, java.io.File> files = io.sinistral.proteus.server.Extractors.fileMap(exchange,"files");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.uploadMultipleFileMap(request,files);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsUploadMultipleFileMapHandler_23);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/map/file",currentHandler);

    final io.undertow.server.HttpHandler testsUploadMultiplePathMapHandler_24 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.Map<java.lang.String, java.nio.file.Path> files = io.sinistral.proteus.server.Extractors.pathMap(exchange,"files");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.uploadMultiplePathMap(request,files);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsUploadMultiplePathMapHandler_24);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/map/path",currentHandler);

    final io.undertow.server.HttpHandler testsExchangeJsonSerializeHandler_25 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        testsController.exchangeJsonSerialize(exchange);
      }
    };

    currentHandler = testsExchangeJsonSerializeHandler_25;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/exchange/json/serialize",currentHandler);

    final io.undertow.server.HttpHandler testsExchangeJsonSerializeToBytesHandler_26 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        testsController.exchangeJsonSerializeToBytes(exchange);
      }
    };

    currentHandler = testsExchangeJsonSerializeToBytesHandler_26;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/exchange/json/serializeToBytes",currentHandler);

    final io.undertow.server.HttpHandler testsFutureMapHandler_27 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.futureMap(request);
        response.send(exchange);
      }
    };

    currentHandler = testsFutureMapHandler_27;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/map",currentHandler);

    final io.undertow.server.HttpHandler testsResponseUploadFilePathHandler_28 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.file.Path file = io.sinistral.proteus.server.Extractors.filePath(exchange,"file");

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.responseUploadFilePath(request,file);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseUploadFilePathHandler_28);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/response/file/path",currentHandler);

    final io.undertow.server.HttpHandler testsResponseUploadOptionalFilePathHandler_29 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.Optional<java.nio.file.Path> file = io.sinistral.proteus.server.Extractors.Optional.filePath(exchange,"file");

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.responseUploadOptionalFilePath(request,file);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseUploadOptionalFilePathHandler_29);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/response/file/path/optional",currentHandler);

    final io.undertow.server.HttpHandler testsResponseEchoJsonHandler_30 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");

          io.sinistral.proteus.server.ServerResponse<io.sinistral.proteus.test.models.User> response = testsController.responseEchoJson(request,user);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseEchoJsonHandler_30);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/response/json/echo",currentHandler);

    final io.undertow.server.HttpHandler testsResponseInnerClassTestHandler_31 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.model(exchange,io.sinistral.proteus.test.models.User.class);

          io.sinistral.proteus.server.ServerResponse<io.sinistral.proteus.test.models.User> response = testsController.responseInnerClassTest(request,user);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseInnerClassTestHandler_31);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/response/json/beanparam",currentHandler);

    final io.undertow.server.HttpHandler testsGenericOptionalSetHandler_32 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
        java.util.Optional<java.util.Set<java.lang.Long>> ids = java.util.Optional.ofNullable(exchange.getQueryParameters().get("ids")).map(java.util.Deque::stream).map( p -> p.map(java.lang.Long::valueOf).collect(java.util.stream.Collectors.toSet()));

        io.sinistral.proteus.server.ServerResponse<java.util.Set<java.lang.Long>> response = testsController.genericOptionalSet(request,ids);
        response.send(exchange);
      }
    };

    currentHandler = testsGenericOptionalSetHandler_32;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/optional/set",currentHandler);

    final io.undertow.server.HttpHandler testsTestPermanentRedirectHandler_33 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        io.sinistral.proteus.server.ServerResponse<java.lang.Void> response = testsController.testPermanentRedirect();
        response.send(exchange);
      }
    };

    currentHandler = testsTestPermanentRedirectHandler_33;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/redirect/permanent",currentHandler);

    final io.undertow.server.HttpHandler testsListConversionHandler_34 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.util.List<java.lang.Long> ids = io.sinistral.proteus.server.Extractors.model(exchange,langLongListTypeReference);

          io.sinistral.proteus.server.ServerResponse<java.util.List<java.lang.Long>> response = testsController.listConversion(request,ids);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsListConversionHandler_34);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/response/parse/ids",currentHandler);

    final io.undertow.server.HttpHandler testsTimestampConversionHandler_35 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(io.sinistral.proteus.server.Extractors.string(exchange,"timestamp"));

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.timestampConversion(request,timestamp);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsTimestampConversionHandler_35);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/parse/timestamp",currentHandler);

    final io.undertow.server.HttpHandler testsDoubleConversionHandler_36 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          Double value = io.sinistral.proteus.server.Extractors.doubleValue(exchange,"value");

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.doubleConversion(request,value);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsDoubleConversionHandler_36);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/parse/double",currentHandler);

    final io.undertow.server.HttpHandler testsBigDecimalConversionHandler_37 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          BigDecimal value = io.sinistral.proteus.server.Extractors.bigDecimalValue(exchange,"value");

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.bigDecimalConversion(request,value);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsBigDecimalConversionHandler_37);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/parse/big-decimal",currentHandler);

    final io.undertow.server.HttpHandler testsInstantConversionHandler_38 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.time.Instant instant = io.sinistral.proteus.server.Extractors.instant(exchange,"instant");

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.instantConversion(request,instant);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsInstantConversionHandler_38);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/parse/instant",currentHandler);

    final io.undertow.server.HttpHandler testsResponseUploadByteBufferHandler_39 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.ByteBuffer file =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"file");

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.responseUploadByteBuffer(request,file);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseUploadByteBufferHandler_39);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/response/bytebuffer",currentHandler);

    final io.undertow.server.HttpHandler testsResponseUploadFileHandler_40 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.io.File file =  io.sinistral.proteus.server.Extractors.file(exchange,"file");

          io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.responseUploadFile(request,file);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseUploadFileHandler_40);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/response/file",currentHandler);

    final io.undertow.server.HttpHandler testsPathParamEndpointHandler_41 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
        String param =  io.sinistral.proteus.server.Extractors.string(exchange,"param");

        io.sinistral.proteus.server.ServerResponse<java.nio.ByteBuffer> response = testsController.pathParamEndpoint(request,param);
        response.send(exchange);
      }
    };

    currentHandler = testsPathParamEndpointHandler_41;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/params/path/{param}",currentHandler);

    final io.undertow.server.HttpHandler testsDebugEndpointHandler_42 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.debugEndpoint(request);
        response.send(exchange);
      }
    };

    currentHandler = testsDebugEndpointHandler_42;
    currentHandler = ioSinistralProteusTestWrappersTestWrapper_1.wrap(currentHandler);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/debug",currentHandler);

    final io.undertow.server.HttpHandler testsNotFoundErrorHandler_43 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
        java.util.Optional<String> param = io.sinistral.proteus.server.Extractors.Optional.string(exchange,"test");

        io.sinistral.proteus.server.ServerResponse<java.lang.Void> response = testsController.notFoundError(request,param);
        response.send(exchange);
      }
    };

    currentHandler = testsNotFoundErrorHandler_43;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/error/404",currentHandler);

    final io.undertow.server.HttpHandler testsUnauthorizedErrorHandler_44 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
        java.util.Optional<String> param = io.sinistral.proteus.server.Extractors.Optional.string(exchange,"test");

        io.sinistral.proteus.server.ServerResponse<java.lang.Void> response = testsController.unauthorizedError(request,param);
        response.send(exchange);
      }
    };

    currentHandler = testsUnauthorizedErrorHandler_44;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/error/401",currentHandler);

    final io.undertow.server.HttpHandler testsDebugBlockingEndpointHandler_45 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>> response = testsController.debugBlockingEndpoint(request);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsDebugBlockingEndpointHandler_45);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/debug/blocking",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureBadRequestHandler_46 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

        java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>>> response = testsController.responseFutureBadRequest(request);
        exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
          response.whenComplete( (r,ex) ->  {
            if(ex != null) {
                        exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                exchange.endExchange();
                } else {
                        r.send(exchange);}
          } );
        } );
      }
    };

    currentHandler = testsResponseFutureBadRequestHandler_46;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/future/badrequest",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureBadRequestBlockingHandler_47 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>>> response = testsController.responseFutureBadRequestBlocking(request);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseFutureBadRequestBlockingHandler_47);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/future/badrequest/blocking",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureNotFoundBlockingHandler_48 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>>> response = testsController.responseFutureNotFoundBlocking(request);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseFutureNotFoundBlockingHandler_48);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/future/notfound/blocking",currentHandler);

    final io.undertow.server.HttpHandler testsResponseFutureUserBlockingHandler_49 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.String>>> response = testsController.responseFutureUserBlocking(request);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsResponseFutureUserBlockingHandler_49);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/future/worker/blocking",currentHandler);

    final io.undertow.server.HttpHandler testsComplexParametersHandler_50 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        io.sinistral.proteus.server.ServerRequest serverRequest = new io.sinistral.proteus.server.ServerRequest(exchange);
        Long pathLong = io.sinistral.proteus.server.Extractors.longValue(exchange,"pathLong");
        java.util.Optional<String> optionalQueryString = io.sinistral.proteus.server.Extractors.Optional.string(exchange,"optionalQueryString");
        java.util.Optional<Long> optionalQueryLong = io.sinistral.proteus.server.Extractors.Optional.longValue(exchange,"optionalQueryLong");
        java.util.Optional<java.time.OffsetDateTime> optionalQueryDate = io.sinistral.proteus.server.Extractors.Optional.offsetDateTime(exchange,"optionalQueryDate");
        java.util.Optional<java.util.UUID> optionalQueryUUID = io.sinistral.proteus.server.Extractors.Optional.string(exchange,"optionalQueryUUID").map(java.util.UUID::fromString);
        java.util.Optional<java.util.UUID> optionalHeaderUUID = io.sinistral.proteus.server.Extractors.Header.Optional.string(exchange,"optionalHeaderUUID").map(java.util.UUID::fromString);
        java.util.Optional<io.sinistral.proteus.test.models.User.UserType> optionalQueryEnum = io.sinistral.proteus.server.Extractors.Optional.string(exchange,"optionalQueryEnum").map(io.sinistral.proteus.test.models.User.UserType::valueOf);
        java.util.Optional<java.lang.String> optionalHeaderString = io.sinistral.proteus.server.Extractors.Header.Optional.string(exchange,"optionalHeaderString");
        java.util.UUID queryUUID = java.util.UUID.fromString(io.sinistral.proteus.server.Extractors.string(exchange,"queryUUID"));
        java.lang.String headerString = io.sinistral.proteus.server.Extractors.Header.string(exchange,"headerString");
        io.sinistral.proteus.test.models.User.UserType queryEnum = io.sinistral.proteus.test.models.User.UserType.valueOf(io.sinistral.proteus.server.Extractors.string(exchange,"queryEnum"));
        java.util.List<java.lang.Integer> queryIntegerList = exchange.getQueryParameters().get("queryIntegerList").stream().map(java.lang.Integer::valueOf).collect(java.util.stream.Collectors.toList());
        Long queryLong = io.sinistral.proteus.server.Extractors.longValue(exchange,"queryLong");

        io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.complexParameters(serverRequest,pathLong,optionalQueryString,optionalQueryLong,optionalQueryDate,optionalQueryUUID,optionalHeaderUUID,optionalQueryEnum,optionalHeaderString,queryUUID,headerString,queryEnum,queryIntegerList,queryLong);
        response.send(exchange);
      }
    };

    currentHandler = testsComplexParametersHandler_50;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/response/parameters/complex/{pathLong}",currentHandler);

    final io.undertow.server.HttpHandler testsResponseSecureContextHandler_51 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {

        io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.responseSecureContext();
        response.send(exchange);
      }
    };

    currentHandler = testsResponseSecureContextHandler_51;
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.GET,"/v1/tests/secure/resource",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadByteBufferHandler_52 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.ByteBuffer buffer =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"buffer");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Integer>> response = testsController.multipartUploadByteBuffer(request,buffer);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestDumpingHandler(new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadByteBufferHandler_52));
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/bytebuffer",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartFutureUploadByteBufferHandler_53 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.ByteBuffer buffer =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"buffer");

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Integer>>> response = testsController.multipartFutureUploadByteBuffer(request,buffer);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartFutureUploadByteBufferHandler_53);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/future/bytebuffer",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadMixedHandler_54 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.ByteBuffer buffer =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"buffer");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.multipartUploadMixed(request,buffer,user,userId);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestDumpingHandler(new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadMixedHandler_54));
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/mixed",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadFutureMixedHandler_55 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.ByteBuffer buffer =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"buffer");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>>> response = testsController.multipartUploadFutureMixed(request,buffer,user,userId);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestDumpingHandler(new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadFutureMixedHandler_55));
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/future/mixed",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadJsonHandler_56 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          com.fasterxml.jackson.databind.JsonNode node = io.sinistral.proteus.server.Extractors.namedJsonNode(exchange,"json");

          io.sinistral.proteus.server.ServerResponse<com.fasterxml.jackson.databind.JsonNode> response = testsController.multipartUploadJson(request,node);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadJsonHandler_56);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/json",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadFutureJsonHandler_57 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          com.fasterxml.jackson.databind.JsonNode json = io.sinistral.proteus.server.Extractors.namedJsonNode(exchange,"json");

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<com.fasterxml.jackson.databind.JsonNode>> response = testsController.multipartUploadFutureJson(request,json);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadFutureJsonHandler_57);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/future/json",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadMixedWithPathHandler_58 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.file.Path path = io.sinistral.proteus.server.Extractors.filePath(exchange,"path");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.multipartUploadMixedWithPath(request,path,user,userId);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadMixedWithPathHandler_58);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/path-mixed",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadFutureMixedWithPathHandler_59 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.file.Path path = io.sinistral.proteus.server.Extractors.filePath(exchange,"path");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>>> response = testsController.multipartUploadFutureMixedWithPath(request,path,user,userId);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadFutureMixedWithPathHandler_59);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/future/path-mixed",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadMixedWithFileHandler_60 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.io.File file =  io.sinistral.proteus.server.Extractors.file(exchange,"file");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.multipartUploadMixedWithFile(request,file,user,userId);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadMixedWithFileHandler_60);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/file-mixed",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadFutureMixedWithFileHandler_61 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.io.File file =  io.sinistral.proteus.server.Extractors.file(exchange,"file");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          java.util.concurrent.CompletableFuture<io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>>> response = testsController.multipartUploadFutureMixedWithFile(request,file,user,userId);
          exchange.dispatch( exchange.getConnection().getWorker(), () ->  {
            response.whenComplete( (r,ex) ->  {
              if(ex != null) {
                                exchange.putAttachment(io.undertow.server.handlers.ExceptionHandler.THROWABLE, ex);             exchange.setResponseCode(500);
                                        exchange.endExchange();
                        } else {
                                r.send(exchange);}
            } );
          } );
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadFutureMixedWithFileHandler_61);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/future/file-mixed",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadMultipleBuffersHandler_62 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.ByteBuffer file1 =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"file1");
          java.nio.ByteBuffer file2 =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"file2");
          java.nio.ByteBuffer file3 =  io.sinistral.proteus.server.Extractors.namedByteBuffer(exchange,"file3");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.multipartUploadMultipleBuffers(request,file1,file2,file3,user,userId);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadMultipleBuffersHandler_62);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/multiple-buffers",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadMultipleFilesHandler_63 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.io.File file1 =  io.sinistral.proteus.server.Extractors.file(exchange,"file1");
          java.io.File file2 =  io.sinistral.proteus.server.Extractors.file(exchange,"file2");
          java.io.File file3 =  io.sinistral.proteus.server.Extractors.file(exchange,"file3");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.multipartUploadMultipleFiles(request,file1,file2,file3,user,userId);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadMultipleFilesHandler_63);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/multiple-files",currentHandler);

    final io.undertow.server.HttpHandler testsMultipartUploadMultipleFilesHandler_64 = new io.undertow.server.HttpHandler() {
      @java.lang.Override
      public void handleRequest(final io.undertow.server.HttpServerExchange exchange) throws
          java.lang.Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
          exchange.dispatch(this);
        } else {
          io.sinistral.proteus.server.ServerRequest request = new io.sinistral.proteus.server.ServerRequest(exchange);
          java.nio.file.Path file1 = io.sinistral.proteus.server.Extractors.filePath(exchange,"file1");
          java.nio.file.Path file2 = io.sinistral.proteus.server.Extractors.filePath(exchange,"file2");
          java.nio.file.Path file3 = io.sinistral.proteus.server.Extractors.filePath(exchange,"file3");
          io.sinistral.proteus.test.models.User user = io.sinistral.proteus.server.Extractors.namedModel(exchange,io.sinistral.proteus.test.models.User.class,"user");
          Integer userId = io.sinistral.proteus.server.Extractors.integerValue(exchange,"userId");

          io.sinistral.proteus.server.ServerResponse<java.util.Map<java.lang.String, java.lang.Object>> response = testsController.multipartUploadMultipleFiles(request,file1,file2,file3,user,userId);
          response.send(exchange);
        }
      }
    };

    currentHandler = new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(8).wrap(testsMultipartUploadMultipleFilesHandler_64);
    currentHandler = ioSinistralProteusTestWrappersTestClassWrapper.wrap(currentHandler);
    router.add(io.undertow.util.Methods.POST,"/v1/tests/multipart/multiple-paths",currentHandler);


    return router;
  }
}