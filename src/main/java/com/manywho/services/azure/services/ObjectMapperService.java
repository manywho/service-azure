package com.manywho.services.azure.services;

import com.manywho.sdk.services.types.system.AuthorizationGroup;
import com.manywho.sdk.services.types.system.AuthorizationUser;
import org.apache.olingo.client.api.domain.ClientEntity;

public class ObjectMapperService {

    public AuthorizationGroup buildGroupObject(ClientEntity groupEntity) {

        return new AuthorizationGroup(groupEntity.getProperty("id").getValue().toString(),
                groupEntity.getProperty("displayName").getValue().toString(),
                groupEntity.getProperty("displayName").getValue().toString());
    }

    public AuthorizationUser buildUserObject(ClientEntity userEntity) {

        return new AuthorizationUser(userEntity.getProperty("id").getValue().toString(),
                userEntity.getProperty("displayName").getValue().toString(),
                userEntity.getProperty("displayName").getValue().toString());
    }
}
