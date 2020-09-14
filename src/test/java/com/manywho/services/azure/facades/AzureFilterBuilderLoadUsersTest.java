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

public class AzureFilterBuilderLoadUsersTest {
    ObjectDataRequest objectDataRequest;

    @Before
    public void setUp() {
        objectDataRequest =  new ObjectDataRequest();
    }

    @Test
    public void testUserLoadWithEmptySearchAndNotObjectData() {
        ListFilter listFilter = null;

        objectDataRequest.setListFilter(listFilter);
        Assert.assertEquals("", AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));

        listFilter = new ListFilter();
        objectDataRequest.setListFilter(listFilter);

        Assert.assertEquals("", AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));

        listFilter = new ListFilter();
        listFilter.setSearch("");
        objectDataRequest.setListFilter(listFilter);

        Assert.assertEquals("", AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));

        listFilter = new ListFilter();
        listFilter.setSearch("");
        objectDataRequest.setListFilter(listFilter);
        objectDataRequest.setObjectData(new ArrayList<>());

        Assert.assertEquals("", AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));
    }

    @Test
    public void testUserLoadWithSearchAndNotObjectData() {
        ListFilter listFilter = new ListFilter();
        listFilter.setSearch("test");
        objectDataRequest.setListFilter(listFilter);
        objectDataRequest.setObjectData(new ArrayList<>());

        Assert.assertEquals("startswith(displayName,'test')",
                AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));
    }

    @Test
    public void testUserLoadWithSearchAndObjectData() {
        ListFilter listFilter = new ListFilter();
        listFilter.setSearch("test");

        objectDataRequest.setListFilter(listFilter);

        MObject objectData = new MObject("GroupAuthorizationUser");
        objectData.setProperties(
                Collections.singletonList(new Property("AuthenticationId", "1", ContentType.String)));

        objectDataRequest.setObjectData(Collections.singletonList(objectData));

        Assert.assertEquals("startswith(displayName,'test') and id in ('1')",
                AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));

        MObject objectData2 = new MObject("GroupAuthorizationUser");
        objectData2.setProperties(
                Collections.singletonList(new Property("AuthenticationId", "2", ContentType.String)));

        objectDataRequest.setObjectData(Arrays.asList(objectData, objectData2));

        Assert.assertEquals("startswith(displayName,'test') and id in ('1','2')",
                AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));
    }

    @Test
    public void testUserLoadWithEmptySearchAndObjectData() {
        ListFilter listFilter = new ListFilter();
        listFilter.setSearch(null);

        objectDataRequest.setListFilter(listFilter);

        MObject objectData = new MObject("GroupAuthorizationUser");
        objectData.setProperties(
                Collections.singletonList(new Property("AuthenticationId", "1", ContentType.String)));

        objectDataRequest.setObjectData(Collections.singletonList(objectData));

        Assert.assertEquals("id in ('1')",
                AzureFilterBuilder.buildLoadUsersFilterExpression(objectDataRequest));
    }
}
