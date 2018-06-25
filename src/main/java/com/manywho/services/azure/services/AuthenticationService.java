package com.manywho.services.azure.services;

import com.auth0.jwt.JWT;
import com.google.common.base.Strings;
import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.entities.security.AuthenticationCredentials;
import com.manywho.sdk.enums.AuthenticationStatus;
import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import com.manywho.services.azure.configuration.SecurityConfiguration;
import com.manywho.services.azure.entities.AzureUser;
import com.manywho.services.azure.facades.AzureFacade;
import com.manywho.services.azure.oauth.AuthResponse;
import com.manywho.services.azure.oauth.AzureHttpClient;
import com.manywho.services.azure.oauth.AzureProvider;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

public class AuthenticationService {
    public final static String RESOURCE_ID = "00000003-0000-0000-c000-000000000000";
    private SecurityConfiguration securityConfiguration;
    private AzureHttpClient azureHttpClient;
    private AzureFacade azureFacade;

    @Inject
    public AuthenticationService( SecurityConfiguration securityConfiguration, AzureHttpClient azureHttpClient, AzureFacade azureFacade) {
        this.securityConfiguration = securityConfiguration;
        this.azureHttpClient = azureHttpClient;
        this.azureFacade = azureFacade;
    }

    public AuthenticatedWhoResult getAuthenticatedWhoResult(AbstractOauth2Provider provider, AuthenticationCredentials credentials) {
        AuthResponse authResponse = azureHttpClient.getAccessTokenByAuthCode(
                credentials.getCode(),
                AzureProvider.REDIRECT_URI,
                securityConfiguration.getOauth2ClientId(),
                securityConfiguration.getOauth2ClientSecret(),
                RESOURCE_ID);

        JWT jwt = JWT.decode(authResponse.getAccess_token());
        AzureUser azureUser = azureFacade.fetchCurrentUser(jwt.getToken());
        AuthenticatedWhoResult authenticatedWhoResult = new AuthenticatedWhoResult();
        authenticatedWhoResult.setDirectoryId( provider.getClientId());
        authenticatedWhoResult.setDirectoryName( provider.getName());

        // the engine needs a user with email populated, if the user doesn't have email we use the unique name that is
        // always mandatory in Azure

        if (Strings.isNullOrEmpty(azureUser.getEmail())) {
            authenticatedWhoResult.setEmail(azureUser.getUniqueName());
        } else {
            authenticatedWhoResult.setEmail(azureUser.getEmail());
        }

        authenticatedWhoResult.setFirstName(azureUser.getGivenName());
        authenticatedWhoResult.setIdentityProvider(provider.getName());
        authenticatedWhoResult.setLastName(azureUser.getFamilyName());
        authenticatedWhoResult.setStatus(AuthenticationStatus.Authenticated);
        authenticatedWhoResult.setTenantName(provider.getClientId());
        authenticatedWhoResult.setToken( jwt.getToken());
        authenticatedWhoResult.setUserId( azureUser.getUserId());
        authenticatedWhoResult.setUsername(azureUser.getUniqueName());

        return authenticatedWhoResult;
    }


}
