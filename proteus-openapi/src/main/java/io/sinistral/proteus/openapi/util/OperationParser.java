package io.sinistral.proteus.openapi.util;

import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.parameters.RequestBody;
import io.sinistral.proteus.openapi.models.responses.ApiResponses;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Optional;

/**
 * Parser for Operation-related annotations
 */
public class OperationParser {

    /**
     * Get RequestBody from annotation
     */
    public static Optional<RequestBody> getRequestBody(
        io.swagger.v3.oas.annotations.parameters.RequestBody annotation,
        jakarta.ws.rs.Consumes classConsumes,
        jakarta.ws.rs.Consumes methodConsumes,
        Components components,
        JsonView jsonViewAnnotation
    ) {
        if (annotation == null) {
            return Optional.empty();
        }

        RequestBody requestBody = new RequestBody();
        requestBody.setDescription(annotation.description());
        requestBody.setRequired(annotation.required());

        // Content would be processed from annotation.content()
        // For now, return basic structure
        return Optional.of(requestBody);
    }

    /**
     * Get ApiResponses from annotations
     */
    public static Optional<ApiResponses> getApiResponses(
        io.swagger.v3.oas.annotations.responses.ApiResponse[] annotations,
        jakarta.ws.rs.Produces classProduces,
        jakarta.ws.rs.Produces methodProduces,
        Components components,
        JsonView jsonViewAnnotation
    ) {
        if (annotations == null || annotations.length == 0) {
            return Optional.empty();
        }

        ApiResponses responses = new ApiResponses();

        for (io.swagger.v3.oas.annotations.responses.ApiResponse annotation : annotations) {
            io.sinistral.proteus.openapi.models.responses.ApiResponse response =
                new io.sinistral.proteus.openapi.models.responses.ApiResponse();

            response.setDescription(annotation.description());

            String responseCode = annotation.responseCode();
            if (responseCode == null || responseCode.isEmpty()) {
                responseCode = "default";
            }

            responses.put(responseCode, response);
        }

        return Optional.of(responses);
    }
}
