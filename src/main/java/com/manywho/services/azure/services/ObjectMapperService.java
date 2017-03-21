package com.manywho.services.azure.services;

import com.manywho.sdk.entities.run.elements.type.Object;
import com.manywho.sdk.entities.run.elements.type.Property;
import com.manywho.sdk.entities.run.elements.type.PropertyCollection;
import org.apache.olingo.commons.api.domain.v4.ODataEntity;

public class ObjectMapperService {

    public Object buildGroupObject(ODataEntity groupEntity) {
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

    public Object buildUserObject(ODataEntity userEntity) {
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
}
