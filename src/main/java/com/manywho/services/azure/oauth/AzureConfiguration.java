package com.manywho.services.azure.oauth;

import com.manywho.sdk.services.utils.Environment;
import com.manywho.services.azure.ServiceConfiguration;

import javax.inject.Inject;

public class AzureConfiguration {
    public static final String REDIRECT_URI = "https://flow.manywho.com/api/run/1/oauth2";
    public static final String GRAPH_RESOURCE = "00000003-0000-0000-c000-000000000000";
    public static final String DIRECTORY_NAME = "azure-manywho";

    private final ServiceConfiguration serviceConfiguration;

    @Inject
    AzureConfiguration(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    /**
     * If tenant is not empty returns the oauth2 entry point v1:
     *      https://login.microsoftonline.com/common
     *
     * but if the tenant is not empty then returns the v2
     *      https://login.microsoftonline.com/{tenant}
     *
     * @param tenant tenant id or org domain
     * @return the entry point for initialize the oauth2 code flow
     */
    public String getAuthorizationUrl(String tenant) {
        String authorityUri = "https://login.microsoftonline.com/common";

        if (tenant != null && !tenant.equals("")) {
            authorityUri = String.format("https://login.microsoftonline.com/%s", tenant);
        }
        return String.format("%s/oauth2/authorize?client_id=%s&scope=%s&response_type=%s",
                authorityUri, serviceConfiguration.getClientId(), "User.Read" , "code");
    }

    /**
     * If tenant is not empty returns the oauth2 entry point v1:
     *      https://login.microsoftonline.com/common/oauth2/token
     *
     * but if the tenant is not empty then returns the v2
     *      https://login.microsoftonline.com/{tenant}/oauth2/token
     *
     * @param tenant tenant id or org domain
     * @return the entry point that need to be called to get an access token
     */
    public static String getTokenUrl(String tenant) {
        if (tenant == null || tenant.equals("")) {
            return "https://login.microsoftonline.com/common/oauth2/token";
        }

        return String.format("https://login.microsoftonline.com/%s/oauth2/token", tenant);
    }
}
