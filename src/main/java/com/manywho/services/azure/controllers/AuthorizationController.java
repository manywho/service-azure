package com.manywho.services.azure.controllers;

import com.google.inject.Provider;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.controllers.AbstractAuthorizationController;
import com.manywho.sdk.services.types.TypeBuilder;
import com.manywho.sdk.services.types.system.AuthorizationAttribute;
import com.manywho.services.azure.managers.AuthManager;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthorizationController extends AbstractAuthorizationController {
    private final AuthManager authManager;
    private final TypeBuilder typeBuilder;
    private final Provider<AuthenticatedWho> authenticatedWhoProvider;

    @Inject
    public AuthorizationController(AuthManager authManager, TypeBuilder typeBuilder,
                                   Provider<AuthenticatedWho> authenticatedWhoProvider) {
        this.authManager = authManager;
        this.authenticatedWhoProvider = authenticatedWhoProvider;
        this.typeBuilder = typeBuilder;
    }

    @Path("/authorization")
    @POST
    public ObjectDataResponse authorization(ObjectDataRequest objectDataRequest) throws Exception {
        AuthenticatedWho authenticatedWho = authenticatedWhoProvider.get();
        return new ObjectDataResponse(typeBuilder.from(authManager.authorizeUser(authenticatedWho, objectDataRequest)));
    }

    @Path("/authorization/group")
    @POST
    public ObjectDataResponse groups(ObjectDataRequest objectDataRequest) throws Exception {
        return new ObjectDataResponse(typeBuilder.from(authManager.loadGroups(objectDataRequest)));
    }

    @Path("/authorization/group/attribute")
    @POST
    public ObjectDataResponse groupAttributes(ObjectDataRequest objectDataRequest) {
        return new ObjectDataResponse(
                typeBuilder.from(new AuthorizationAttribute("users", "Users"))
        );
    }

    @Path("/authorization/user")
    @POST
    public ObjectDataResponse users(ObjectDataRequest objectDataRequest) throws Exception {
        return new ObjectDataResponse(typeBuilder.from(authManager.loadUsers(objectDataRequest)));
    }

    @Path("/authorization/user/attribute")
    @POST
    public ObjectDataResponse userAttributes(ObjectDataRequest objectDataRequest) {
        return new ObjectDataResponse(
                typeBuilder.from(new AuthorizationAttribute("accountId", "Account ID"))
        );
    }
}
