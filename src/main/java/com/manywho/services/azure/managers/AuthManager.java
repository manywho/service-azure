package com.manywho.services.azure.managers;

import com.google.common.base.Strings;
import com.manywho.sdk.api.AuthorizationType;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.configuration.ConfigurationParser;
import com.manywho.sdk.services.types.system.$User;
import com.manywho.sdk.services.types.system.AuthorizationGroup;
import com.manywho.sdk.services.types.system.AuthorizationUser;
import com.manywho.services.azure.ApplicationConfiguration;
import com.manywho.services.azure.oauth.AzureConfiguration;
import com.manywho.services.azure.services.AuthorizationService;

import javax.inject.Inject;
import java.util.List;

public class AuthManager {
    private final AuthorizationService authorizationService;
    private final ConfigurationParser configurationParser;

    @Inject
    public AuthManager(AuthorizationService authorizationService,
                       ConfigurationParser configurationParser){
        this.authorizationService = authorizationService;
        this.configurationParser = configurationParser;
    }

    public $User authorizeUser(AuthenticatedWho user, ObjectDataRequest objectDataRequest) throws Exception {
        String authorizationStatus = authorizationService.getUserAuthorizationStatus(objectDataRequest.getAuthorization(), user);
        ApplicationConfiguration configuration = configurationParser.from(objectDataRequest);

        $User userObject = new $User();
        userObject.setUserId(user.getUserId());
        userObject.setDirectoryName(AzureConfiguration.DIRECTORY_NAME);
        userObject.setDirectoryId(AzureConfiguration.DIRECTORY_NAME);
        userObject.setAuthenticationType(AuthorizationType.Oauth2);
        userObject.setLoginUrl(AzureConfiguration.getAuthorizationUrl(configuration.getTenant()));
        userObject.setStatus(authorizationStatus);

        return userObject;
    }

    public List<AuthorizationGroup> loadGroups(ObjectDataRequest objectDataRequest) throws Exception {
        ApplicationConfiguration configuration = configurationParser.from(objectDataRequest);
        if (Strings.isNullOrEmpty(configuration.getUsername()) || Strings.isNullOrEmpty(configuration.getPassword())) {
            throw new RuntimeException("Username and Password are required to load groups");
        }

        return authorizationService.loadGroups(configuration, objectDataRequest);
    }

    public List<AuthorizationUser> loadUsers(ObjectDataRequest objectDataRequest) throws Exception {
        ApplicationConfiguration configuration = configurationParser.from(objectDataRequest);

        if (Strings.isNullOrEmpty(configuration.getUsername()) || Strings.isNullOrEmpty(configuration.getPassword())) {
            throw new RuntimeException("Username and Password are required to load users");
        }

        return authorizationService.loadUsers(configuration, objectDataRequest);
    }
}
