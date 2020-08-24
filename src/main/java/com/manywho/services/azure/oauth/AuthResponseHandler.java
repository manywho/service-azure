package com.manywho.services.azure.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;

public class AuthResponseHandler implements ResponseHandler {
    private final ObjectMapper objectMapper;

    @Inject
    public AuthResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public AuthResponse handleResponse(HttpResponse httpResponse) throws IOException {
        int status = httpResponse.getStatusLine().getStatusCode();

        if (status >= 200 && status < 300) {
            HttpEntity entity2 = httpResponse.getEntity();
            if (entity2 == null) {
                throw new RuntimeException("Error when auth request");
            } else{
                return objectMapper.readValue(EntityUtils.toString(entity2), AuthResponse.class);
            }
        }

        if (status == 400) {
            String errorString = EntityUtils.toString(httpResponse.getEntity());
            HashMap error = objectMapper.readValue(errorString, HashMap.class);

            if (error.get("error_description") != null) {
                throw new RuntimeException(error.get("error_description").toString());
            }
        }

        throw new RuntimeException(httpResponse.getStatusLine().toString());
    }
}
