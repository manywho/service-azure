package com.manywho.services.azure.services;

import com.manywho.sdk.entities.run.elements.config.Authorization;
import com.manywho.sdk.entities.run.elements.config.Group;
import com.manywho.sdk.entities.run.elements.config.User;
import com.manywho.sdk.entities.run.elements.type.Object;
import com.manywho.sdk.entities.run.elements.type.ObjectCollection;
import com.manywho.sdk.entities.run.elements.type.Property;
import com.manywho.sdk.entities.run.elements.type.PropertyCollection;
import com.manywho.sdk.entities.security.AuthenticatedWho;
import com.manywho.services.azure.configuration.SecurityConfiguration;
import com.manywho.services.azure.controllers.Compressor;
import com.manywho.services.azure.entities.Configuration;
import com.manywho.services.azure.facades.AzureFacade;
import com.manywho.services.azure.oauth.AuthResponse;
import com.manywho.services.azure.oauth.AzureHttpClient;
import org.apache.commons.collections4.CollectionUtils;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class AuthorizationService {

    private SecurityConfiguration securityConfiguration;
    private AzureHttpClient azureHttpClient;
    private AzureFacade azureFacade;

    @Inject
    public AuthorizationService(SecurityConfiguration securityConfiguration, AzureHttpClient azureHttpClient, AzureFacade azureFacade) {
        this.securityConfiguration = securityConfiguration;
        this.azureHttpClient = azureHttpClient;
        this.azureFacade = azureFacade;
    }

    public String getUserAuthorizationStatus(Authorization authorization, AuthenticatedWho user) {
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
                    String userId = azureFacade.fetchCurrentUserId(getDecompressedToken(user));

                    if (CollectionUtils.isNotEmpty(authorization.getUsers())) {
                        for (User allowedUser:authorization.getUsers()) {
                            if (allowedUser.getAttribute().equalsIgnoreCase("accountId")
                                    && Objects.equals(allowedUser.getAuthenticationId(), userId)) {

                                return "200";
                            }
                        }
                    }

                    if (CollectionUtils.isNotEmpty(authorization.getGroups())) {
                        List<Object> groups = azureFacade.fetchMemberOfGroups(getDecompressedToken(user));
                        for (Group group : authorization.getGroups()) {
                            if (groups.stream().anyMatch(m -> m.getExternalId().equals(group.getAuthenticationId()))) {
                                return "200";
                            }
                        }
                    }
                }
            default:
                return "401";
        }
    }

    private String getDecompressedToken(AuthenticatedWho user) {
        try {
            return Compressor.decompress(user.getToken());
        } catch (IOException e) {
            throw new RuntimeException("Not valid token", e);
        }
    }

    public ObjectCollection loadGroups(Configuration configuration) {
        AuthResponse accessToken = azureHttpClient.getAccessTokenByUsernamePassword(
                configuration.getUsername(),
                configuration.getPassword(),
                this.securityConfiguration.getOauth2ClientId(),
                this.securityConfiguration.getOauth2ClientSecret());

        try {
             List<Object> groups = azureFacade.fetchGroups(accessToken.getAccess_token());
            ObjectCollection objectCollection = new ObjectCollection();

            for (Object o: groups) {
                objectCollection.add(o);
            }

            return objectCollection;
        } catch (ExecutionException | InterruptedException e ) {
            throw new RuntimeException(e);
        }
    }

    public ObjectCollection loadUsers(Configuration configuration) {
        AuthResponse accessToken = azureHttpClient.getAccessTokenByUsernamePassword(
                configuration.getUsername(),
                configuration.getPassword(),
                this.securityConfiguration.getOauth2ClientId(),
                this.securityConfiguration.getOauth2ClientSecret());

        List<Object> users = azureFacade.fetchUsers(accessToken.getAccess_token());
        ObjectCollection objectCollection = new ObjectCollection();

        for (Object o: users) {
            objectCollection.add(o);
        }

        return objectCollection;
    }

    public Object loadGroupAttributes() {
        PropertyCollection properties = new PropertyCollection();
        properties.add(new Property("Label", "Users"));
        properties.add(new Property("Value", "users"));

        Object object = new Object();
        object.setDeveloperName("AuthenticationAttribute");
        object.setExternalId("users");
        object.setProperties(properties);

        return object;
    }

    public Object loadUsersAttributes() {
        PropertyCollection properties = new PropertyCollection();
        properties.add(new Property("Label", "Account ID"));
        properties.add(new Property("Value", "accountId"));

        Object object = new Object();
        object.setDeveloperName("AuthenticationAttribute");
        object.setExternalId("accountID");
        object.setProperties(properties);

        return object;
    }
}
