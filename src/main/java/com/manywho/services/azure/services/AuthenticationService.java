package com.manywho.services.azure.services;

import com.auth0.jwt.JWT;
import com.manywho.sdk.entities.security.AuthenticatedWhoResult;
import com.manywho.sdk.entities.security.AuthenticationCredentials;
import com.manywho.sdk.enums.AuthenticationStatus;
import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import com.manywho.services.azure.configuration.SecurityConfiguration;
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

    public AuthenticatedWhoResult getAuthenticatedWhoResult(AbstractOauth2Provider provider, AuthenticationCredentials credentials) throws Exception {
        AuthResponse authResponse = azureHttpClient.getAccessTokenByAuthCode(
                credentials.getCode(),
                AzureProvider.REDIRECT_URI,
                securityConfiguration.getOauth2ClientId(),
                securityConfiguration.getOauth2ClientSecret(),
                RESOURCE_ID);

        JWT jwt = JWT.decode(authResponse.getAccess_token());
        AuthenticatedWhoResult authenticatedWhoResult = new AuthenticatedWhoResult();
        authenticatedWhoResult.setDirectoryId( provider.getClientId());
        authenticatedWhoResult.setDirectoryName( provider.getName());
        authenticatedWhoResult.setEmail(azureFacade.fetchCurrentUserEmail(jwt.getToken()));
        authenticatedWhoResult.setFirstName(valueOrEmpty(jwt,"given_name"));
        authenticatedWhoResult.setIdentityProvider(provider.getName());
        authenticatedWhoResult.setLastName(valueOrEmpty(jwt,"family_name"));
        authenticatedWhoResult.setStatus(AuthenticationStatus.Authenticated);
        authenticatedWhoResult.setTenantName(provider.getClientId());
        authenticatedWhoResult.setToken( jwt.getToken());
        authenticatedWhoResult.setUserId( jwt.getClaim("oid").asString());
        authenticatedWhoResult.setUsername(jwt.getClaim("unique_name").asString());

        return authenticatedWhoResult;
    }

    private String valueOrEmpty(JWT jwt, String value) {
        if(StringUtils.isEmpty(jwt.getClaim(value).asString())) {
            return "empty";
        } else {
            return jwt.getClaim(value).asString();
        }
    }
}
