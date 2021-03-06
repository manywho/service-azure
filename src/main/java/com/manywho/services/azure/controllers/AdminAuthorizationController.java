package com.manywho.services.azure.controllers;

import com.google.common.base.Strings;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/callback/admin")
public class AdminAuthorizationController {

    @Path("/authorization")
    @Produces(MediaType.TEXT_HTML)
    @GET
    public String adminAuthorizationSuccess(@QueryParam("code") String code, @QueryParam("admin_consent") String adminConsent) {

        String title = "Authorization Success";

        String body = "<h1>Authorization Success</h1>" +
                "<p>The client has been approved to be used in your organization.</p>" +
                "<p>You can now install the Azure Service. For more information please check " +
                "<a href=\"https://docs.manywho.com\">docs.manywho.com</a></p>";
        if (Strings.isNullOrEmpty(code) || Boolean.valueOf(adminConsent) != true) {
            title = "There was a problem ";
            body = "<h1>There was a problem authorizing the client.</h1>" +
                    "<p>This method should be called by a Microsoft client, never directly by a user.</p>" +
                    "<p> Please check the documentation at <a href=\"https://docs.manywho.com\">docs.manywho.com</a>.</p>";
        }

        return populateWeb(title, body);
    }

    private String populateWeb(String title, String body) {
        return String.format("<!DOCTYPE html>\n" +
                "<html lang=\"en\">" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "    <title>%s</title>\n" +
                "</head>\n" +
                "<body>%s</body>\n" +
                "</html>", title, body);
    }
}