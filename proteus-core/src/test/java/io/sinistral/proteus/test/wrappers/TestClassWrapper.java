package io.sinistral.proteus.test.wrappers;

import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;

public class TestClassWrapper implements HandlerWrapper
{


    @Override
    public HttpHandler wrap(HttpHandler handler)
    {
        return exchange -> {

            handler.handleRequest(exchange);
        };
    }
}
