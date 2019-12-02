package com.manywho.services.azure.managers;

import com.google.common.base.Strings;
import com.manywho.sdk.entities.UserObject;
import com.manywho.sdk.entities.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.entities.run.elements.type.ObjectDataResponse;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.entities.security.AuthenticationCredentials;
import com.manywho.sdk.enums.AuthorizationType;
import com.manywho.sdk.services.PropertyCollectionParser;
import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import com.manywho.services.azure.entities.Configuration;
import com.manywho.services.azure.facades.ObjectDataRequestExtractor;
import com.manywho.services.azure.services.AuthenticationService;
import com.manywho.services.azure.services.AuthorizationService;
import org.scribe.oauth.OAuthService;

import java.util.List;

import javax.inject.Inject;

public class AuthManager {

    private AuthenticationService authenticationService;
    private AuthorizationService authorizationService;
    private PropertyCollectionParser propertyParser;

    @Inject
    public AuthManager(AuthenticationService authenticationService, AuthorizationService authorizationService,
                       PropertyCollectionParser propertyParser){
        this.authenticationService = authenticationService;
        this.propertyParser = propertyParser;
        this.authorizationService = authorizationService;
    }

    public AuthenticatedWhoResult authenticateUser(AbstractOauth2Provider provider, AuthenticationCredentials credentials) {

        return authenticationService.getAuthenticatedWhoResult(provider, credentials);
    }

    public ObjectDataResponse authorizeUser(OAuthService oauthService, AbstractOauth2Provider provider, AuthenticatedWho user, ObjectDataRequest objectDataRequest) {
        String authorizationStatus = authorizationService.getUserAuthorizationStatus(objectDataRequest.getAuthorization(), user);

        UserObject userObject = new UserObject(provider.getName(), AuthorizationType.Oauth2,
                oauthService.getAuthorizationUrl(null), authorizationStatus);

        return new ObjectDataResponse(userObject);
    }

    public ObjectDataResponse loadGroups(ObjectDataRequest objectDataRequest) throws Exception {
        Configuration configuration = propertyParser.parse(objectDataRequest.getConfigurationValues(), Configuration.class);

        if (Strings.isNullOrEmpty(configuration.getUsername()) || Strings.isNullOrEmpty(configuration.getPassword())) {
            throw new RuntimeException("Username and Password are required to load groups");
        }

        List<String> groupIds = ObjectDataRequestExtractor.getObjectProperties(objectDataRequest, "GroupAuthorizationGroup", "AuthenticationId");
        String searchTerm = objectDataRequest.getListFilter().getSearch();

        return new ObjectDataResponse(authorizationService.loadGroups(configuration, groupIds, searchTerm));
    }

    public ObjectDataResponse loadGroupAttributes() {
        return new ObjectDataResponse(authorizationService.loadGroupAttributes());
    }

    public ObjectDataResponse loadUsers(ObjectDataRequest objectDataRequest) throws Exception {
        Configuration configuration = propertyParser.parse(objectDataRequest.getConfigurationValues(), Configuration.class);

        if (Strings.isNullOrEmpty(configuration.getUsername()) || Strings.isNullOrEmpty(configuration.getPassword())) {
            throw new RuntimeException("Username and Password are required to load users");
        }

        List<String> userIds = ObjectDataRequestExtractor.getObjectProperties(objectDataRequest, "GroupAuthorizationUser", "AuthenticationId");
        String searchTerm = objectDataRequest.getListFilter().getSearch();

        return new ObjectDataResponse(authorizationService.loadUsers(configuration, userIds, searchTerm));
    }

    public ObjectDataResponse loadUsersAttributes() {
        return new ObjectDataResponse(authorizationService.loadUsersAttributes());
    }
}
