package com.manywho.services.azure.services;

import java.util.ArrayList;
import java.util.List;

import com.manywho.sdk.entities.run.elements.type.Object;
import com.manywho.sdk.entities.run.elements.type.Property;
import com.manywho.sdk.entities.run.elements.type.PropertyCollection;
import com.manywho.services.azure.entities.AzureUser;

import org.apache.olingo.commons.api.domain.v4.ODataEntity;

public class ObjectMapperService {

    public List<Object> buildGroupsObjects(List<ODataEntity> groups) {
        List<Object> groupsArray = new ArrayList<>();

        for (ODataEntity siteEntity : groups) {
            groupsArray.add(buildGroupObject(siteEntity));
        }

        return groupsArray;
    }

    public List<Object> buildUserObjects(List<ODataEntity> entities) {
        List<Object> groupsArray = new ArrayList<>();

        for (ODataEntity siteEntity : entities) {
            groupsArray.add(buildUserObject(siteEntity));
        }

        return groupsArray;
    }
    
    private Object buildGroupObject(ODataEntity groupEntity) {
        PropertyCollection properties = new PropertyCollection();

        properties.add(new Property("AuthenticationId", groupEntity.getProperty("id").getValue().toString()));
        properties.add(new Property("FriendlyName", groupEntity.getProperty("displayName").getValue().toString()));
        properties.add(new Property("DeveloperSummary", groupEntity.getProperty("displayName").getValue().toString()));

        Object object = new Object();
        object.setDeveloperName("GroupAuthorizationGroup");
        object.setExternalId(groupEntity.getProperty("id").getValue().toString());
        object.setProperties(properties);

        return object;
    }

    private Object buildUserObject(ODataEntity userEntity) {
        PropertyCollection properties = new PropertyCollection();

        properties.add(new Property("AuthenticationId",  userEntity.getProperty("id").getValue().toString()));
        properties.add(new Property("FriendlyName",  userEntity.getProperty("displayName").getValue().toString()));
        properties.add(new Property("DeveloperSummary",  userEntity.getProperty("displayName").getValue().toString()));

        Object object = new Object();
        object.setDeveloperName("GroupAuthorizationUser");
        object.setExternalId( userEntity.getProperty("id").getValue().toString());
        object.setProperties(properties);

        return object;
    }

    public AzureUser buildAzureUser(ODataEntity userEntity) {

        return new AzureUser(userEntity.getProperty("mail").getValue().toString(),
                userEntity.getProperty("givenName").getValue().toString(),
                userEntity.getProperty("surname").getValue().toString(),
                userEntity.getProperty("id").getValue().toString(),
                userEntity.getProperty("userPrincipalName").getValue().toString()
        );
    }
}
