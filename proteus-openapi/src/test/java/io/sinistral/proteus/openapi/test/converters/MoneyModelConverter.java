package io.sinistral.proteus.openapi.test.converters;


import com.fasterxml.jackson.databind.JavaType;
import com.google.common.collect.ImmutableMap;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.javamoney.moneta.Money;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MoneyModelConverter implements ModelConverter
{

    private final Schema<Money> schema = new MoneySchema();

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        JavaType _type = Json.mapper().constructType(type.getType());

         if(_type != null && (_type.getRawClass().equals(Money.class)))
        {
            context.defineModel("Money",schema, type, null);

            return schema;
        }

        if (type.getType() instanceof Class<?>) {

            Class<?> cls = (Class<?>) type.getType();

            if(cls.isAssignableFrom(Money.class))
            {
                context.defineModel("Money",schema, type, null);

                return schema;
            }
        }
        else if(type.getType().getTypeName().equals("[simple type, class org.javamoney.moneta.Money]"))
        {
            context.defineModel("Money",schema, type, null);

            return schema;
        }
        else if (type.isSchemaProperty()) {

            _type = Json.mapper().constructType(type.getType());

            if (_type != null) {
                Class<?> cls = _type.getRawClass();
                if (Money.class.isAssignableFrom(cls)) {

                    context.defineModel("Money",schema, type, null);

                    return schema;
                }
            }
        }

        if (chain.hasNext()) {
            return chain.next().resolve(type, context, chain);
        }

        return null;

    }


    public static class MoneySchema extends Schema<Money>
    {
        private String _type = "object";

        private String _$ref = null;

        private String _description = "A monetary amount";

        private Map<String, Schema> _properties = ImmutableMap.of("amount",new NumberSchema(),"currency",new StringSchema());

        private List<String> _required = Arrays.asList("amount","currency");

        public MoneySchema()
        {
            super();
            super.setName("Money");
            super.setType("object");
            super.set$ref(_$ref);
            super.description(_description);
            super.properties(_properties);
            super.required(_required);
        }

        @Override
        protected Money cast(Object value)
        {
            if (value != null) {
                try {
                    if (value instanceof Money) {
                        return (Money) value;
                    }
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        public boolean equals(java.lang.Object o)
        {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            MoneySchema MoneySchema = (MoneySchema) o;
            return Objects.equals(this._type, MoneySchema._type) &&
                    Objects.equals(this._properties, MoneySchema._properties) &&
                    Objects.equals(this._description, MoneySchema._description) &&
                    super.equals(o);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(_type, _properties, super.hashCode());
        }

        /**
         * Convert the given object to string with each line indented by 4 spaces
         * (except the first line).
         */
        private String toIndentedString(java.lang.Object o)
        {
            if (o == null) {
                return "null";
            }
            return o.toString().replace("\n", "\n    ");
        }

    }
}
