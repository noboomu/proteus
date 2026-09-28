package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for resolved JAX-RS parameters
 */
public class ResolvedParameter {
    /** the parameter map. */
    public List<Parameter> parameters = new ArrayList<>();
    /** the value. */
    public List<Parameter> formParameters = new ArrayList<>();
    /** the request body. */
    public Parameter requestBody;

    /** Creates an empty resolved parameter. */
    public ResolvedParameter() {
    }
}
