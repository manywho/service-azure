package com.manywho.services.azure.services;

import com.manywho.sdk.api.run.elements.config.Authorization;
import com.manywho.sdk.api.run.elements.config.Group;
import com.manywho.sdk.api.run.elements.config.User;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.security.AuthenticatedWho;
import com.manywho.sdk.services.types.system.AuthorizationGroup;
import com.manywho.sdk.services.types.system.AuthorizationUser;
import com.manywho.sdk.services.utils.Environment;
import org.apache.commons.collections4.CollectionUtils;
import com.manywho.services.azure.ApplicationConfiguration;
import com.manywho.services.azure.facades.AzureFacade;
import com.manywho.services.azure.oauth.AuthResponse;
import com.manywho.services.azure.oauth.AzureHttpClient;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

public class AuthorizationService {
    private final AzureHttpClient azureHttpClient;
    private final AzureFacade azureFacade;

    @Inject
    public AuthorizationService(AzureHttpClient azureHttpClient, AzureFacade azureFacade) {
        this.azureHttpClient = azureHttpClient;
        this.azureFacade = azureFacade;
    }

    public String getUserAuthorizationStatus(Authorization authorization, AuthenticatedWho user) throws Exception {
        switch (authorization.getGlobalAuthenticationType()) {
            case Public:
                return "200";
            case AllUsers:
                if (!user.getUserId().equalsIgnoreCase("PUBLIC_USER")) {
                    return "200";
                } else {
                    return "401";
                }
            case Specified:
                if (!user.getUserId().equalsIgnoreCase("PUBLIC_USER")) {
                    String userId = azureFacade.fetchCurrentUserId(user.getToken());;

                    if (CollectionUtils.isNotEmpty(authorization.getUsers())) {
                        for (User allowedUser:authorization.getUsers()) {
                            if (allowedUser.getAttribute().equalsIgnoreCase("accountId")
                                    && Objects.equals(allowedUser.getAuthenticationId(), userId)) {

                                return "200";
                            }
                        }
                    }

                    if (CollectionUtils.isNotEmpty(authorization.getGroups())) {
                        List<AuthorizationGroup> groups = azureFacade.fetchMemberOfGroups(user.getToken());
                        for (Group group : authorization.getGroups()) {
                            if (groups.stream().anyMatch(m -> m.getId().equals(group.getAuthenticationId()))) {
                                return "200";
                            }
                        }
                    }
                }
            default:
                return "401";
        }
    }

    public List<AuthorizationGroup> loadGroups(ApplicationConfiguration configuration, ObjectDataRequest objectDataRequest) {

        AuthResponse accessToken = azureHttpClient.getAccessTokenByUsernamePassword(
                configuration.getTenant(),
                configuration.getUsername(),
                configuration.getPassword(),
                Environment.getRequired("oauth2.clientId"),
                Environment.getRequired("oauth2.clientSecret"));

        String searchTerm = objectDataRequest.getListFilter().getSearch();

        return azureFacade.fetchGroups(accessToken.getAccess_token(), searchTerm);
    }

    public List<AuthorizationUser> loadUsers(ApplicationConfiguration configuration, ObjectDataRequest objectDataRequest) {
        AuthResponse accessToken = azureHttpClient.getAccessTokenByUsernamePassword(
                configuration.getTenant(),
                configuration.getUsername(),
                configuration.getPassword(),
                Environment.getRequired("oauth2.clientId"),
                Environment.getRequired("oauth2.clientSecret"));

        String searchTerm = objectDataRequest.getListFilter().getSearch();

        return azureFacade.fetchUsers(accessToken.getAccess_token(), searchTerm);
    }
}
