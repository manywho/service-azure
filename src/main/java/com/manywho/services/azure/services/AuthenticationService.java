package com.manywho.services.azure.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Strings;
import com.manywho.sdk.api.security.AuthenticatedWhoResult;
import com.manywho.sdk.api.security.AuthenticationCredentials;
import com.manywho.sdk.services.utils.Environment;
import com.manywho.services.azure.ApplicationConfiguration;
import com.manywho.services.azure.ServiceConfiguration;
import com.manywho.services.azure.entities.AzureUser;
import com.manywho.services.azure.facades.AzureFacade;
import com.manywho.services.azure.oauth.AuthResponse;
import com.manywho.services.azure.oauth.AzureHttpClient;
import com.manywho.services.azure.oauth.AzureConfiguration;

import javax.inject.Inject;

public class AuthenticationService {
    private final AzureHttpClient azureHttpClient;
    private final AzureFacade azureFacade;
    private final ServiceConfiguration serviceConfiguration;

    @Inject
    public AuthenticationService(AzureHttpClient azureHttpClient, AzureFacade azureFacade, ServiceConfiguration serviceConfiguration) {
        this.azureHttpClient = azureHttpClient;
        this.azureFacade = azureFacade;
        this.serviceConfiguration = serviceConfiguration;
    }

    public AuthenticatedWhoResult getAuthenticatedWhoResult(ApplicationConfiguration applicationConfiguration, AuthenticationCredentials credentials) {
        AuthResponse authResponse = azureHttpClient.getAccessTokenByAuthCode(
                applicationConfiguration.getTenant(),
                credentials.getCode(),
                AzureConfiguration.REDIRECT_URI,
                serviceConfiguration.getClientId(),
                serviceConfiguration.getClientSecret());

        DecodedJWT jwt = JWT.decode(authResponse.getAccess_token());
        AzureUser azureUser = azureFacade.fetchCurrentUser(jwt.getToken());
        AuthenticatedWhoResult authenticatedWhoResult = new AuthenticatedWhoResult();

        if (Strings.isNullOrEmpty(azureUser.getEmail())) {
            authenticatedWhoResult = AuthenticatedWhoResult.createDeniedResult();
            authenticatedWhoResult.setStatusMessage("This account doesn't have an email address - please provide an email in your account and try again.");
            return authenticatedWhoResult;
        }

        authenticatedWhoResult.setDirectoryId(serviceConfiguration.getClientId());
        authenticatedWhoResult.setDirectoryName(AzureConfiguration.DIRECTORY_NAME);
        authenticatedWhoResult.setEmail(azureUser.getEmail());
        authenticatedWhoResult.setFirstName(azureUser.getGivenName());
        authenticatedWhoResult.setIdentityProvider(AzureConfiguration.DIRECTORY_NAME);
        authenticatedWhoResult.setLastName(azureUser.getFamilyName());
        authenticatedWhoResult.setStatus(AuthenticatedWhoResult.AuthenticationStatus.Authenticated);
        authenticatedWhoResult.setTenantName(serviceConfiguration.getClientId());
        authenticatedWhoResult.setToken(jwt.getToken());
        authenticatedWhoResult.setUserId(azureUser.getUserId());
        authenticatedWhoResult.setUsername(azureUser.getUniqueName());

        return authenticatedWhoResult;
    }
}
