package com.manywho.services.azure.facades;

import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.Property;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AzureFilterBuilder {

    public static String buildLoadUsersFilterExpression(ObjectDataRequest objectDataRequest) {
        return buildFilterExpression(
                extractSearchTerm(objectDataRequest),
                extractsIdsToFilter(objectDataRequest, false)
        );
    }

    public static String buildLoadGroupsFilterExpression(ObjectDataRequest objectDataRequest) {
        return buildFilterExpression(
                extractSearchTerm(objectDataRequest),
                extractsIdsToFilter(objectDataRequest, true)
        );
    }

    private static String buildFilterExpression(String searchTerm, List<String> elementIds) {
        String filter = "";

        if(searchTerm != null && searchTerm.isEmpty() == false){
            filter = "startswith(displayName,'" + searchTerm + "')";
        }

        List<String> ids = elementIds.stream()
                .map(value -> String.format("'%s'", value))
                .collect(Collectors.toList());

        if (!filter.equals("") && !ids.isEmpty()) {
            filter += " and id in ("+ String.join(",", ids) + ")";
        } else if (!ids.isEmpty()){
            filter = "id in (" + String.join(",", ids) + ")";
        }

        return filter;
    }

    private static String extractSearchTerm(ObjectDataRequest objectDataRequest) {
        if (objectDataRequest.getListFilter() == null || objectDataRequest.getListFilter().getSearch() == null) {
            return null;
        }

        return objectDataRequest.getListFilter().getSearch();
    }

    private static List<String> extractsIdsToFilter(ObjectDataRequest objectDataRequest, boolean isGroup) {
        if (objectDataRequest == null || objectDataRequest.getObjectData() == null) {
            return new ArrayList<>();
        }
        final String objectDataDeveloperName = (isGroup)? "GroupAuthorizationGroup" : "GroupAuthorizationUser";

        return objectDataRequest.getObjectData().stream()
                .filter(data -> data.getDeveloperName().equals(objectDataDeveloperName))
                .map(MObject::getProperties)
                .flatMap(Collection::stream)
                .filter(property -> property.getDeveloperName().equals("AuthenticationId"))
                .map(Property::getContentValue)
                .collect(Collectors.toList());
    }
}
