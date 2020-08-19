package com.manywho.services.azure.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import com.manywho.sdk.services.utils.Environment;
import com.manywho.services.azure.ApplicationConfiguration;
import com.manywho.services.azure.entities.AzureUser;
import com.manywho.services.azure.facades.AzureFacade;
import com.manywho.services.azure.oauth.AuthResponse;
import com.manywho.services.azure.oauth.AzureHttpClient;
import com.manywho.services.azure.oauth.AzureConfiguration;

import javax.inject.Inject;

public class AuthenticationService {
    private final AzureHttpClient azureHttpClient;
    private final AzureFacade azureFacade;

    @Inject
    public AuthenticationService(AzureHttpClient azureHttpClient, AzureFacade azureFacade) {
        this.azureHttpClient = azureHttpClient;
        this.azureFacade = azureFacade;
    }

    public AuthenticatedWhoResult getAuthenticatedWhoResult(ApplicationConfiguration applicationConfiguration, AuthenticationCredentials credentials) {
        AuthResponse authResponse = azureHttpClient.getAccessTokenByAuthCode(
                applicationConfiguration.getTenant(),
                credentials.getCode(),
                AzureConfiguration.REDIRECT_URI,
                Environment.getRequired("oauth2.clientId"),
                Environment.getRequired("oauth2.clientSecret"));

        DecodedJWT jwt = JWT.decode(authResponse.getAccess_token());
        AzureUser azureUser = azureFacade.fetchCurrentUser(jwt.getToken());
        AuthenticatedWhoResult authenticatedWhoResult = new AuthenticatedWhoResult();

        if (Strings.isNullOrEmpty(azureUser.getEmail())) {
            authenticatedWhoResult = AuthenticatedWhoResult.createDeniedResult();
            authenticatedWhoResult.setStatusMessage("This account doesn't have an email address - please provide an email in your account and try again.");
            return authenticatedWhoResult;
        }

        authenticatedWhoResult.setDirectoryId(Environment.getRequired("oauth2.clientId"));
        authenticatedWhoResult.setDirectoryName(AzureConfiguration.DIRECTORY_NAME);
        authenticatedWhoResult.setEmail(azureUser.getEmail());
        authenticatedWhoResult.setFirstName(azureUser.getGivenName());
        authenticatedWhoResult.setIdentityProvider(AzureConfiguration.DIRECTORY_NAME);
        authenticatedWhoResult.setLastName(azureUser.getFamilyName());
        authenticatedWhoResult.setStatus(AuthenticatedWhoResult.AuthenticationStatus.Authenticated);
        authenticatedWhoResult.setTenantName(Environment.getRequired("oauth2.clientId"));
        authenticatedWhoResult.setToken(jwt.getToken());
        authenticatedWhoResult.setUserId(azureUser.getUserId());
        authenticatedWhoResult.setUsername(azureUser.getUniqueName());

        return authenticatedWhoResult;
    }
}
