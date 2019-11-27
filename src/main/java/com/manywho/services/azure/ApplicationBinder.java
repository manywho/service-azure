package com.manywho.services.azure;

import com.manywho.sdk.services.oauth.AbstractOauth2Provider;
import com.manywho.services.azure.configuration.SecurityConfiguration;
import com.manywho.services.azure.facades.AzureEntityFetcher;
import com.manywho.services.azure.facades.AzureFacade;
import com.manywho.services.azure.managers.*;
import com.manywho.services.azure.oauth.AuthResponseHandler;
import com.manywho.services.azure.oauth.AzureHttpClient;
import com.manywho.services.azure.oauth.AzureProvider;
import com.manywho.services.azure.services.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class ApplicationBinder extends AbstractBinder {
    @Override
    protected void configure() {
        bind(AzureProvider.class).to(AbstractOauth2Provider.class);
        bind(AzureHttpClient.class).to(AzureHttpClient.class);
        bind(AuthResponseHandler.class).to(AuthResponseHandler.class);
        bind(AuthManager.class).to(AuthManager.class);
        bind(AuthenticationService.class).to(AuthenticationService.class);
        bind(AuthorizationService.class).to(AuthorizationService.class);
        bind(SecurityConfiguration.class).to(SecurityConfiguration.class);
        bind(ObjectMapperService.class).to(ObjectMapperService.class);
        bind(AzureFacade.class).to(AzureFacade.class);
        bind(AzureEntityFetcher.class).to(AzureEntityFetcher.class);
    }
}
