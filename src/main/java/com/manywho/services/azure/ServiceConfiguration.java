package com.manywho.services.azure;

import com.manywho.services.azure.configuration.ServiceConfigurationDefault;
import com.manywho.services.azure.configuration.ServiceConfigurationEnvironmentVariables;
import com.manywho.services.azure.configuration.ServiceConfigurationProperties;

import javax.inject.Inject;

public class ServiceConfiguration extends ServiceConfigurationDefault {

    @Inject
    public ServiceConfiguration(ServiceConfigurationEnvironmentVariables environment, ServiceConfigurationProperties properties) {
        super(environment, properties);
    }

    public String getClientId() {
        return this.get("oauth2.clientId");
    }

    public String getClientSecret() {
        return this.get("oauth2.clientSecret");
    }
}
