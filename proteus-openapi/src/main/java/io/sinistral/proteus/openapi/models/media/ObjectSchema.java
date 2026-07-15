package io.sinistral.proteus.openapi.models.media;

/**
 * Schema representing an object type
 */
public class ObjectSchema extends Schema<Object> {

    public ObjectSchema() {
        super();
        super.setType("object");
    }

    @Override
    public void setType(String type) {
        // Object schema always has type "object"
        super.setType("object");
    }
}
