package io.sinistral.proteus.test.wrappers;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
