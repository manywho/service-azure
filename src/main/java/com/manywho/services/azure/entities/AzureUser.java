package com.manywho.services.azure.entities;

import com.google.common.base.Strings;

public class AzureUser {
    public String email;
    public String givenName;
    public String familyName;
    public String userId;
    public String uniqueName;

    public AzureUser(String email, String givenName, String familyName, String userId, String userPrincipalName) {
        this.email = email;
        this.givenName = givenName;
        this.familyName = familyName;
        this.userId = userId;
        this.uniqueName = userPrincipalName;

        if (Strings.isNullOrEmpty(this.familyName)) {
            this.familyName = "empty";
        }

        if (Strings.isNullOrEmpty(this.givenName)) {
            this.givenName = "empty";
        }

        if (Strings.isNullOrEmpty(this.uniqueName)) {
            this.uniqueName = userId;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
}
