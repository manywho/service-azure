package com.manywho.services.azure.controllers;

import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.controllers.AbstractAuthenticationController;
import com.manywho.services.azure.ApplicationConfiguration;
import com.manywho.services.azure.services.AuthenticationService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationController extends AbstractAuthenticationController {
    private final AuthenticationService authenticationService;
    private final ConfigurationParser configurationParser;

    @Inject
    public AuthenticationController(AuthenticationService authenticationService, ConfigurationParser configurationParser) {
        this.authenticationService = authenticationService;
        this.configurationParser = configurationParser;
    }

    @Path("/authentication")
    @POST
    public AuthenticatedWhoResult authentication(AuthenticationCredentials authenticationCredentials) {
        ApplicationConfiguration applicationConfiguration = configurationParser.from(authenticationCredentials);
        return authenticationService.getAuthenticatedWhoResult(applicationConfiguration, authenticationCredentials);
    }
}
