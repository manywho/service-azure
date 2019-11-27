package com.manywho.services.azure.facades;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.communication.request.ODataRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntityRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetRequest;
import org.apache.olingo.client.api.communication.request.retrieve.v4.RetrieveRequestFactory;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.v4.ODataClient;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.domain.v4.ODataEntity;
import org.apache.olingo.commons.api.domain.v4.ODataEntitySet;

public class AzureEntityFetcher {
    private final static String GRAPH_ENDPOINT = "https://graph.microsoft.com/v1.0";
    private final ODataClient client;
    private final RetrieveRequestFactory retrieveRequestFactory;

    public AzureEntityFetcher() {
        client = ODataClientFactory.getV4();
        retrieveRequestFactory = client.getRetrieveRequestFactory();
    }

    public ODataEntity fetchEntity(String token, String entryPoint){
        
        ODataEntityRequest<ODataEntity> entitySetRequest = retrieveRequestFactory.getEntityRequest(buildUriForEntryPoint(entryPoint));
        addAuthHeader(entitySetRequest, token);
        
        return entitySetRequest.execute().getBody();
    }

    public List<ODataEntity> fetchEntities(String token, String entryPoint){
        return fetchEntitiesFollowPaging(token, buildUriForEntryPoint(entryPoint));
    }

    private List<ODataEntity> fetchEntitiesFollowPaging(String token, URI uri){   
        List<ODataEntity> entities = new ArrayList<ODataEntity>();
        
        ODataEntitySetRequest<ODataEntitySet> entitySetRequest = retrieveRequestFactory.getEntitySetRequest(uri);
        addAuthHeader(entitySetRequest, token);
        ODataRetrieveResponse<ODataEntitySet> sitesEntitySetResponse = entitySetRequest.execute();
        
        ODataEntitySet body = sitesEntitySetResponse.getBody();
        entities.addAll(body.getEntities());
        
        if(body.getNext() != null){
            List<ODataEntity> moreEntities = fetchEntitiesFollowPaging(token, body.getNext());
            entities.addAll(moreEntities);
        }

        return entities;
    }

    private void addAuthHeader(ODataRequest request, String token){
        request.addCustomHeader("Authorization", String.format("Bearer %s", token));
    }

    private URI buildUriForEntryPoint(String entryPoint){
        return client.newURIBuilder(GRAPH_ENDPOINT).appendEntitySetSegment(entryPoint).build();    
    }
}
