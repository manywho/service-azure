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

import javax.inject.Inject;

public class AuthenticationService {
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
        JWT jwt = JWT.decode(credentials.getToken());
        AzureUser azureUser = azureFacade.fetchCurrentUser(jwt.getToken());
        AuthenticatedWhoResult authenticatedWhoResult = new AuthenticatedWhoResult();

        if (Strings.isNullOrEmpty(azureUser.getEmail()) == true) {
            authenticatedWhoResult = AuthenticatedWhoResult.createDeniedResult();
            authenticatedWhoResult.setStatusMessage("This account doesn't have an email address - please provide an email in your account and try again.");
            return authenticatedWhoResult;
        }

        // securityConfiguration.getOauth2ClientId() == ((JWTDecoder) jwt).payload.getAudience()

        authenticatedWhoResult.setDirectoryId( provider.getClientId());
        authenticatedWhoResult.setDirectoryName( provider.getName());
        authenticatedWhoResult.setEmail(jwt.getClaims().get("email").asString());
        authenticatedWhoResult.setFirstName(jwt.getClaims().get("given_name").asString());
        authenticatedWhoResult.setIdentityProvider(provider.getName());
        authenticatedWhoResult.setLastName(jwt.getClaims().get("family_name").asString());
        authenticatedWhoResult.setStatus(AuthenticationStatus.Authenticated);
        authenticatedWhoResult.setTenantName(provider.getClientId());
        authenticatedWhoResult.setToken(jwt.getToken());
        authenticatedWhoResult.setUserId(jwt.getClaims().get("email").asString());
        authenticatedWhoResult.setUsername(jwt.getClaims().get("unique_name").asString());

        return authenticatedWhoResult;
    }


}
