package com.manywho.services.azure.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import sun.net.www.protocol.http.AuthenticationHeader;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.manywho.services.azure.oauth.AzureProvider.AUTHORITY_URI_V2;
import static com.manywho.services.azure.oauth.AzureProvider.GRAPH_RESOURCE;

public class AzureHttpClient {
    private CloseableHttpClient httpclient;
    private ObjectMapper objectMapper;
    private AuthResponseHandler authResponseHandler;
    @Inject
    public AzureHttpClient(ObjectMapper objectMapper, AuthResponseHandler authResponseHandler){
        this.httpclient = HttpClients.createDefault();
        this.objectMapper = objectMapper;
        this.authResponseHandler = authResponseHandler;
    }

    public AuthResponse getAccessTokenByUsernamePassword(String tenant, String userName, String password, String clientId,
                                                         String clientSecret) {
        try {
            // for api version 2.0
            BasicNameValuePair paramPermissions = new BasicNameValuePair("scope", "https://graph.microsoft.com/Group.Read.All https://graph.microsoft.com/User.Read.All");
            String tokenUri = String.format("%s/%s/%s", AUTHORITY_URI_V2, tenant, "oauth2/v2.0/token");

            if (Strings.isNullOrEmpty(tenant)) {
                // for api version 1.0
                paramPermissions = new BasicNameValuePair("resource", "00000003-0000-0000-c000-000000000000");
                tokenUri = String.format("%s/%s", AzureProvider.AUTHORITY_URI_V1, "oauth2/token");
            }

            HttpPost httpPost = new HttpPost(tokenUri);

            List<NameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("grant_type", "password"));
            formParams.add(paramPermissions);
            formParams.add(new BasicNameValuePair("client_id", clientId));
            formParams.add(new BasicNameValuePair("username", userName));
            formParams.add(new BasicNameValuePair("password", password));
            formParams.add(new BasicNameValuePair("client_secret", clientSecret));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

            httpPost.setEntity(entity);

            return (AuthResponse) httpclient.execute(httpPost, authResponseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AuthResponse getAccessTokenByAuthCode(String authCode, String redirectUri, String clientId,
                                                 String clientSecret) {
        try {
            // we use version 1
            HttpPost httpPost = new HttpPost(String.format("%s/%s", AzureProvider.AUTHORITY_URI_V1, "oauth2/token"));

            List<NameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
            formParams.add(new BasicNameValuePair("code", authCode));
            formParams.add(new BasicNameValuePair("resource", GRAPH_RESOURCE));
            formParams.add(new BasicNameValuePair("client_id", clientId));
            formParams.add(new BasicNameValuePair("redirect_uri", redirectUri));
            formParams.add(new BasicNameValuePair("client_secret", clientSecret));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
            httpPost.setEntity(entity);

            return (AuthResponse) httpclient.execute(httpPost, authResponseHandler);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
