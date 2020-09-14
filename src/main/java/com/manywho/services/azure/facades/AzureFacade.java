package com.manywho.services.azure.facades;

import com.manywho.sdk.services.types.system.AuthorizationGroup;
import com.manywho.sdk.services.types.system.AuthorizationUser;
import com.manywho.services.azure.entities.AzureUser;
import com.manywho.services.azure.services.ObjectMapperService;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntityRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetRequest;
import org.apache.olingo.client.api.communication.request.retrieve.RetrieveRequestFactory;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.uri.URIBuilder;
import org.apache.olingo.client.core.ODataClientFactory;
import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class AzureFacade {
    private final static String GRAPH_ENDPOINT = "https://graph.microsoft.com/v1.0";
    private final ObjectMapperService objectMapperService;
    private final ODataClient client;
    private final RetrieveRequestFactory retrieveRequestFactory;

    @Inject
    public AzureFacade(ObjectMapperService objectMapperService) {
        this.objectMapperService = objectMapperService;
        client = ODataClientFactory.getClient();
        retrieveRequestFactory = client.getRetrieveRequestFactory();
    }

    public List<AuthorizationGroup> fetchGroups(String token, String filter) {
        ODataRetrieveResponse<ClientEntitySet> sitesEntitySetResponse = getEntitiesSetResponse(token, "groups", filter);

        return responseGroups(sitesEntitySetResponse.getBody().getEntities());
    }
    public List<AuthorizationGroup> fetchMemberOfGroups(String token) {
        ODataRetrieveResponse<ClientEntitySet> sitesEntitySetResponse = getEntitiesSetResponse(token, "me/memberOf", "");

        return responseGroups(sitesEntitySetResponse.getBody().getEntities());
    }

    public List<AuthorizationUser> fetchUsers(String token, String filter) {
        ODataRetrieveResponse<ClientEntitySet> sitesEntitySetResponse = getEntitiesSetResponse(token, "users", filter);

        return responseUsers(sitesEntitySetResponse.getBody().getEntities());
    }

    public String fetchCurrentUserId(String token) {
        ClientEntity sitesEntitySetResponse = getEntitySetResponse(token, "me").getBody();

        return sitesEntitySetResponse.getProperty("id").getValue().toString();
    }

    public AzureUser fetchCurrentUser(String token) {
        ClientEntity sitesEntitySetResponse = getEntitySetResponse(token, "me").getBody();

        return new AzureUser(sitesEntitySetResponse.getProperty("mail").getValue().toString(),
                sitesEntitySetResponse.getProperty("givenName").getValue().toString(),
                sitesEntitySetResponse.getProperty("surname").getValue().toString(),
                sitesEntitySetResponse.getProperty("id").getValue().toString(),
                sitesEntitySetResponse.getProperty("userPrincipalName").getValue().toString()
        );
    }

    private List<AuthorizationUser> responseUsers(List<ClientEntity> entities) {
        List<AuthorizationUser> users = new ArrayList<>();

        for (ClientEntity siteEntity : entities) {
            users.add(this.objectMapperService.buildUserObject(siteEntity));
        }

        return users;
    }

    private ODataRetrieveResponse<ClientEntitySet> getEntitiesSetResponse(String token, String urlEntity, String filter) {
        URI entitySetURI = buildUri(urlEntity, filter);
        ODataEntitySetRequest<ClientEntitySet> entitySetRequest = retrieveRequestFactory.getEntitySetRequest(entitySetURI);
        entitySetRequest.addCustomHeader("Authorization", String.format("Bearer %s", token));

        return entitySetRequest.execute();
    }

    private ODataRetrieveResponse<ClientEntity> getEntitySetResponse(String token, String entryPoint) {
        URI entityUri = client.newURIBuilder(GRAPH_ENDPOINT).appendEntitySetSegment(entryPoint).build();
        ODataEntityRequest<ClientEntity> entitySetRequest = retrieveRequestFactory.getEntityRequest(entityUri);
        entitySetRequest.addCustomHeader("Authorization", String.format("Bearer %s", token));
        return entitySetRequest.execute();
    }

    private List<AuthorizationGroup> responseGroups(List<ClientEntity> groups) {
        List<AuthorizationGroup> groupsArray = new ArrayList<>();

        for (ClientEntity groupEntity : groups) {
            groupsArray.add(this.objectMapperService.buildGroupObject(groupEntity));
        }

        return groupsArray;
    }

    private URI buildUri(String entryPoint, String filter){

        URIBuilder builder = client.newURIBuilder(GRAPH_ENDPOINT);

        builder.appendEntitySetSegment(entryPoint);

        if(filter != null && !filter.isEmpty()){
            builder.addQueryOption("filter", filter, false);
        }

        // Request the first 999 records which is the maximum allowed for Users and Groups, defaults to 100
        builder.top(999);

        return builder.build();
    }
}
