package com.manywho.services.azure.entities;

import com.manywho.sdk.services.annotations.Property;

public class Configuration {
    @Property("Username")
    private String username;

    @Property("Password")
    private String password;

    @Property("Tenant")
    private String tenant;

    public String getUsername() {return username;}

    public String getPassword() {
        return password;
    }

    public String getTenant() {
        return tenant;
    }
}
