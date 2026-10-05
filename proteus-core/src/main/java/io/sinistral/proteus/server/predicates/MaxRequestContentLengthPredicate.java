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
 * Predicate that matches requests whose content length does not exceed a maximum.
 *
 * @author jbauer
 */
public class MaxRequestContentLengthPredicate implements Predicate
{
    /** The max size. */
    private final long maxSize;

    MaxRequestContentLengthPredicate(final long maxSize)
    {
        this.maxSize = maxSize;
    }

    /**
     * Returns true when the request content length exceeds the configured max size.
     *
     * @param value the server exchange
     * @return true when the body is too large
     */
    @Override
    public boolean resolve(final HttpServerExchange value)
    {
        final String length = value.getRequestHeaders().getFirst(Headers.CONTENT_LENGTH);

        if (length == null) {
            return false;
        }

        return Long.parseLong(length) > maxSize;
    }

    /** Builds the predicate from a {@code value} config entry. */
    public static class Builder implements PredicateBuilder
    {
        /** Creates the builder. */
        public Builder() {}

        /**
         * Builds the predicate from a config map.
         *
         * @param config the predicate config
         * @return the built predicate
         */
        @Override
        public Predicate build(final Map<String, Object> config)
        {
            Long max = (Long) config.get("value");

            return new MaxRequestContentLengthPredicate(max);
        }

        /**
         * Returns the default parameter.
         *
         * @return the default parameter, or null when unset
         */
        @Override
        public String defaultParameter()
        {
            return "value";
        }

        /**
         * Returns the name.
         *
         * @return the name, or null when unset
         */
        @Override
        public String name()
        {
            return "max-content-size";
        }

        /**
         * Returns the parameters.
         *
         * @return the parameters, never null
         */
        @Override
        public Map<String, Class<?>> parameters()
        {
            return Collections.<String, Class<?>>singletonMap("value", Long.class);
        }

        /**
         * Returns the required parameters.
         *
         * @return the required parameters, never null
         */
        @Override
        public Set<String> requiredParameters()
        {
            return Collections.singleton("value");
        }
    }
}



