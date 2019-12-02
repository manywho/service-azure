package com.manywho.services.azure.facades;

import java.util.List;

public class AzureFilterBuilder {

	public static String buildFilterExpression(List<String> ids, String searchTerm) {
        String filter = "";
        
        for(String id : ids){
            if(filter != ""){
                filter += " or ";
            }

            filter += "id eq '" + id + "'";
        }

        if(filter == "" && searchTerm != null){
            filter = "startswith(displayName,'"+searchTerm+"')";
        }

        return filter;
	}
}