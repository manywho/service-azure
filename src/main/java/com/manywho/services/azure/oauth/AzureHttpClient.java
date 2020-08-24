package com.manywho.services.azure.oauth;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.manywho.services.azure.oauth.AzureConfiguration.GRAPH_RESOURCE;

public class AzureHttpClient {
    private final AuthResponseHandler authResponseHandler;

    @Inject
    public AzureHttpClient(AuthResponseHandler authResponseHandler){
        this.authResponseHandler = authResponseHandler;
    }

    public AuthResponse getAccessTokenByUsernamePassword(String tenantId, String userName, String password, String clientId,
                                                         String clientSecret) {

            HttpPost httpPost = new HttpPost(AzureConfiguration.getTokenUrl(tenantId));

            List<NameValuePair> formParams = new ArrayList<>();
            formParams.add(new BasicNameValuePair("grant_type", "password"));
            formParams.add(new BasicNameValuePair("resource", "00000003-0000-0000-c000-000000000000"));
            formParams.add(new BasicNameValuePair("client_id", clientId));
            formParams.add(new BasicNameValuePair("username", userName));
            formParams.add(new BasicNameValuePair("password", password));
            formParams.add(new BasicNameValuePair("client_secret", clientSecret));
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

            httpPost.setEntity(entity);

            try(CloseableHttpClient httpclient = HttpClients.createDefault()){
                return (AuthResponse) httpclient.execute(httpPost, authResponseHandler);
            } catch (IOException e) {
                throw new RuntimeException("Unable to get the access token by user credentials" + e.getMessage(), e);
            }
    }

    public AuthResponse getAccessTokenByAuthCode(String tenantId, String authCode, String redirectUri, String clientId,
                                                 String clientSecret) {

        HttpPost httpPost = new HttpPost(AzureConfiguration.getTokenUrl(tenantId));

        List<NameValuePair> formParams = new ArrayList<>();
        formParams.add(new BasicNameValuePair("grant_type", "authorization_code"));
        formParams.add(new BasicNameValuePair("code", authCode));
        formParams.add(new BasicNameValuePair("resource", GRAPH_RESOURCE));
        formParams.add(new BasicNameValuePair("client_id", clientId));
        formParams.add(new BasicNameValuePair("redirect_uri", redirectUri));
        formParams.add(new BasicNameValuePair("client_secret", clientSecret));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
        httpPost.setEntity(entity);

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            return (AuthResponse) httpclient.execute(httpPost, authResponseHandler);
        } catch (IOException e) {
            throw new RuntimeException("Unable to get the access token by authorization code flow" + e.getMessage(), e);
        }
    }
}
