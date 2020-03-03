
package com.manywho.services.azure.facades;

public class AzureFilterBuilder {

	public static String buildFilterExpression(String searchTerm) {
        String filter = "";

        if(filter == "" && searchTerm != null){
            filter = "startswith(displayName,'"+searchTerm+"')";
        }

        return filter;
	}
}