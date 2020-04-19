package com.manywho.services.azure.controllers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/health")
public class HealthController {
    @GET
    public Response healthCheck() {
        return Response.ok().build();
    }
}
