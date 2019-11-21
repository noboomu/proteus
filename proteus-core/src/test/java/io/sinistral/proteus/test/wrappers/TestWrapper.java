package io.sinistral.proteus.test.wrappers;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestWrapper implements HandlerWrapper
{
    private static final Logger log = LoggerFactory.getLogger(TestWrapper.class.getName());

    public static AttachmentKey<String> DEBUG_TEST_KEY = AttachmentKey.create(String.class);

    public TestWrapper()
    {
        super();
    }

    @Inject
    @Named("test.wrapper.value")
    private String wrapperValue;

    @Override
    public HttpHandler wrap(HttpHandler handler)
    {
        return exchange -> {

            log.debug("Test wrapper");

            exchange.putAttachment(DEBUG_TEST_KEY,wrapperValue);

            handler.handleRequest(exchange);
        };
    }
}
