/**
 *
 */
package io.sinistral.proteus.server.predicates;

import io.undertow.predicate.Predicate;
import io.undertow.predicate.PredicateBuilder;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author jbauer
 *
 */
public class MaxRequestContentLengthPredicate implements Predicate
{
    private final long maxSize;

    MaxRequestContentLengthPredicate(final long maxSize)
    {
        this.maxSize = maxSize;
    }

    @Override
    public boolean resolve(final HttpServerExchange value)
    {
        final String length = value.getRequestHeaders().getFirst(Headers.CONTENT_LENGTH);

        if (length == null) {
            return false;
        }

        return Long.parseLong(length) > maxSize;
    }

    public static class Builder implements PredicateBuilder
    {
        @Override
        public Predicate build(final Map<String, Object> config)
        {
            Long max = (Long) config.get("value");

            return new MaxRequestContentLengthPredicate(max);
        }

        @Override
        public String defaultParameter()
        {
            return "value";
        }

        @Override
        public String name()
        {
            return "max-content-size";
        }

        @Override
        public Map<String, Class<?>> parameters()
        {
            return Collections.<String, Class<?>>singletonMap("value", Long.class);
        }

        @Override
        public Set<String> requiredParameters()
        {
            return Collections.singleton("value");
        }
    }
}



