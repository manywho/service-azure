package com.manywho.services.azure.facades;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.api.run.elements.type.ListFilter;
import com.manywho.sdk.api.run.elements.type.MObject;
import com.manywho.sdk.api.run.elements.type.ObjectDataRequest;
import com.manywho.sdk.api.run.elements.type.Property;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class AzureFilterBuilderLoadGroupsTest {
    ObjectDataRequest objectDataRequest;

    @Before
    public void setUp() {
        objectDataRequest =  new ObjectDataRequest();
    }

    @Test
    public void testGroupLoadWithEmptySearchAndNotObjectData() {
        ListFilter listFilter = null;

        objectDataRequest.setListFilter(listFilter);
        Assert.assertEquals("", AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));

        listFilter = new ListFilter();
        objectDataRequest.setListFilter(listFilter);

        Assert.assertEquals("", AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));

        listFilter = new ListFilter();
        listFilter.setSearch("");
        objectDataRequest.setListFilter(listFilter);

        Assert.assertEquals("", AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));

        listFilter = new ListFilter();
        listFilter.setSearch("");
        objectDataRequest.setListFilter(listFilter);
        objectDataRequest.setObjectData(new ArrayList<>());

        Assert.assertEquals("", AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));
    }

    @Test
    public void testGroupsLoadWithSearchAndNotObjectData() {
        ListFilter listFilter = new ListFilter();
        listFilter.setSearch("test");
        objectDataRequest.setListFilter(listFilter);
        objectDataRequest.setObjectData(new ArrayList<>());

        Assert.assertEquals("startswith(displayName,'test')",
                AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));
    }

    @Test
    public void testGroupLoadWithSearchAndObjectData() {
        ListFilter listFilter = new ListFilter();
        listFilter.setSearch("test");

        objectDataRequest.setListFilter(listFilter);

        MObject objectData = new MObject("GroupAuthorizationGroup");
        objectData.setProperties(
                Collections.singletonList(new Property("AuthenticationId", "1", ContentType.String)));

        objectDataRequest.setObjectData(Collections.singletonList(objectData));

        Assert.assertEquals("startswith(displayName,'test') and id in ('1')",
                AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));

        MObject objectData2 = new MObject("GroupAuthorizationGroup");
        objectData2.setProperties(
                Collections.singletonList(new Property("AuthenticationId", "2", ContentType.String)));

        objectDataRequest.setObjectData(Arrays.asList(objectData, objectData2));

        Assert.assertEquals("startswith(displayName,'test') and id in ('1','2')",
                AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));
    }

    @Test
    public void testGroupLoadWithEmptySearchAndObjectData() {
        ListFilter listFilter = new ListFilter();
        listFilter.setSearch(null);

        objectDataRequest.setListFilter(listFilter);

        MObject objectData = new MObject("GroupAuthorizationGroup");
        objectData.setProperties(
                Collections.singletonList(new Property("AuthenticationId", "1", ContentType.String)));

        objectDataRequest.setObjectData(Collections.singletonList(objectData));

        Assert.assertEquals("id in ('1')",
                AzureFilterBuilder.buildLoadGroupsFilterExpression(objectDataRequest));
    }
}
