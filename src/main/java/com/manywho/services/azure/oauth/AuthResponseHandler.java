package com.manywho.services.azure.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class AuthResponseHandler implements ResponseHandler {
    private ObjectMapper objectMapper;

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

        throw new RuntimeException(httpResponse.getStatusLine().toString());
    }
}
