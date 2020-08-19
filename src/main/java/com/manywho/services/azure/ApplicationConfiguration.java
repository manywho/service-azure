package com.manywho.services.azure;

import com.manywho.sdk.api.ContentType;
import com.manywho.sdk.services.configuration.Configuration;

public class ApplicationConfiguration implements Configuration {
    @Configuration.Setting(name ="Username", contentType = ContentType.String, required = false)
    private String username;

    @Configuration.Setting(name ="Password", contentType = ContentType.Password, required = false)
    private String password;

    @Configuration.Setting(name="Tenant", contentType = ContentType.String, required = false)
    private String tenant;

    public String getUsername() {return username;}

    public String getPassword() {
        return password;
    }

    public String getTenant() {
        return tenant;
    }
}
