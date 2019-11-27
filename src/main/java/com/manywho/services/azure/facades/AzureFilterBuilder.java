package com.manywho.services.azure.facades;

import java.util.List;

public class AzureFilterBuilder {

	public static String buildIdInFilter(List<String> ids) {
        String filter = "";
        
        for(String id : ids){
            if(filter != ""){
                filter += " or ";
            }

            filter += "id eq '" + id + "'";
        }

        return filter;
	}
}