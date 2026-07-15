package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for resolved JAX-RS parameters
 */
public class ResolvedParameter {
    public List<Parameter> parameters = new ArrayList<>();
    public List<Parameter> formParameters = new ArrayList<>();
    public Parameter requestBody;

    public ResolvedParameter() {
    }
}
