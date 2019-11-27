package com.manywho.services.azure.facades;

import java.util.List;

import javax.inject.Inject;

import com.manywho.sdk.entities.run.elements.type.Object;
import com.manywho.services.azure.entities.AzureUser;
import com.manywho.services.azure.services.ObjectMapperService;

import org.apache.olingo.commons.api.domain.v4.ODataEntity;

public class AzureFacade {
    private ObjectMapperService objectMapperService;
    private AzureEntityFetcher azureEntityStore;

    @Inject
    public AzureFacade(ObjectMapperService objectMapperService, AzureEntityFetcher azureEntityStore) {
        this.objectMapperService = objectMapperService;
        this.azureEntityStore = azureEntityStore;
    }

    public List<Object> fetchGroups(String token) {
        List<ODataEntity> entities = this.azureEntityStore.fetchEntities(token, "groups");
        
        return this.objectMapperService.buildGroupsObjects(entities);
    }

    public List<Object> fetchMemberOfGroups(String token) {
        List<ODataEntity> entities = this.azureEntityStore.fetchEntities(token, "me/memberOf");

        return this.objectMapperService.buildGroupsObjects(entities);
    }

    public List<Object> fetchUsers(String token) {
        List<ODataEntity> entities = this.azureEntityStore.fetchEntities(token, "users");

        return this.objectMapperService.buildUserObjects(entities);
    }

    public AzureUser fetchCurrentUser(String token) {
        ODataEntity sitesEntitySetResponse = this.azureEntityStore.fetchEntity(token, "me");

        return this.objectMapperService.buildAzureUser(sitesEntitySetResponse);
    }
}
