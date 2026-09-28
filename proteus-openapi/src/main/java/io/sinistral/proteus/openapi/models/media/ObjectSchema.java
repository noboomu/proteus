package io.sinistral.proteus.openapi.models.media;

/**
 * Schema representing an object type
 */
public class ObjectSchema extends Schema<Object> {

    /** Creates an object-typed schema. */
    public ObjectSchema() {
        super();
        super.setType("object");
    }

    /**
     * Sets the type.
     *
     * @param type the type
     */
    @Override
    public void setType(String type) {
        // Object schema always has type "object"
        super.setType("object");
    }
}
