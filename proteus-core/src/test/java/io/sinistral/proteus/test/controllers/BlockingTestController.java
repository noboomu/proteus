package io.sinistral.proteus.test.controllers;

import com.google.inject.Singleton;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.server.ServerResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Path("/blocking")
@Produces(MediaType.APPLICATION_JSON)
@Blocking
@Singleton
public class BlockingTestController {

    @GET
    @Path("/virtual")
    public ServerResponse<Map<String, Boolean>> virtualThread() {
        return ServerResponse.response(Map.of(
            "virtual",
            Thread.currentThread().isVirtual()
        )).applicationJson();
    }

    @GET
    @Path("/io")
    @Blocking(false)
    public ServerResponse<Map<String, Boolean>> ioThread() {
        return ServerResponse.response(Map.of(
            "virtual",
            Thread.currentThread().isVirtual()
        )).applicationJson();
    }
}
