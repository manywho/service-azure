package com.manywho.services.azure.facades;

import java.util.ArrayList;
import java.util.List;

import com.manywho.sdk.entities.run.elements.type.MObject;
import com.manywho.sdk.entities.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.entities.run.elements.type.Property;

public class ObjectDataRequestExtractor {

    public static List<String> getObjectProperties(ObjectDataRequest objectDataRequest, String name, String propertyName){

        List<String> ids = new ArrayList<String>();

        if (objectDataRequest.getObjectData() != null && objectDataRequest.getObjectData().size() > 0) {
            for (MObject requestedObjects : objectDataRequest.getObjectData()) {
                if (requestedObjects.getDeveloperName().equals(name)) {

                    String id = requestedObjects.getProperties().stream()
                            .filter(property -> property.getDeveloperName().equals(propertyName))
                            .findFirst()
                            .orElse(new Property(propertyName, ""))
                            .getContentValue();

                    ids.add(id);
                }
            }
        }

        return ids;
    }

}