package com.manywho.services.azure;
import com.manywho.services.azure.controllers.Compressor;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CompressorTest {

    @Test
    public void testCompress() throws IOException {
        String token = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IkFRQUJBQUFBQUFEWDhHQ2k2SnM2U0s4MlRzRDJQYjdyVHY3Q0xPTGFFMDJ4YmVX" +
                "Qnd1b1VyZWlNUUpOMXdLVkRGci1VNXJQa29CZ1hfUGpUMkVxZzhXVDV4RW9YZ1Y0Z3VtYkQtRUR0SFV1NUtnaHg0STQyOVNBQSI" +
                "sImFsZyI6IlJTMjU2IiwieDV0IjoiVGlvR3l3d2xodmRGYlhaODEzV3BQYXk5QWxVIiwia2lkIjoiVGlvR3l3d2xodmRGYlhaOD" +
                "EzV3BQYXk5QWxVIn0.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL" +
                "3N0cy53aW5kb3dzLm5ldC84ZDJjMDMyYy00ZDEzLTQ5ZmQtODUxZS03ODg2MzIxOTUwN2IvIiwiaWF0IjoxNTMwNzExMzI2LCJu" +
                "YmYiOjE1MzA3MTEzMjYsImV4cCI6MTUzMDcxNTIyNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhIQUFBQVB0UFl6clh4SVRWRHd3" +
                "QjA5ZURsY1Z4bVkvN1ExdUhlVmF2UkpTRWNmTFE9IiwiYW1yIjpbInB3ZCJdLCJhcHBfZGlzcGxheW5hbWUiOiJCb29taSBGbG93" +
                "IC0gQXp1cmUiLCJhcHBpZCI6IjA5NWQ3ZjQyLWI0Y2ItNGJhZS1hNWY2LTg0N2Q3NjQ5MzQ1ZiIsImFwcGlkYWNyIjoiMSIsImZh" +
                "bWlseV9uYW1lIjoiQ29sbGF6emkiLCJnaXZlbl9uYW1lIjoiSm9zZSIsImlwYWRkciI6IjE4NS40MS4yNy40IiwibmFtZSI6Impv" +
                "c2UuY29sbGF6emkiLCJvaWQiOiIwNDUwOTI5ZC1iZDAxLTQzZmEtYTY4Ny03NjZiOWNjMTNlYTMiLCJwbGF0ZiI6IjE0IiwicHVp" +
                "ZCI6IjEwMDMzRkZGOTQ2MzM5RkQiLCJzY3AiOiJEaXJlY3RvcnkuQWNjZXNzQXNVc2VyLkFsbCBEaXJlY3RvcnkuUmVhZC5BbGwgR" +
                "3JvdXAuUmVhZC5BbGwgVXNlci5SZWFkIFVzZXIuUmVhZC5BbGwiLCJzaWduaW5fc3RhdGUiOlsia21zaSJdLCJzdWIiOiJvY2RfTU" +
                "tlNW5iTW9lOGViZEY5V2F0ZF8wN2NOTkYzaUxMZklrRjhLZ19VIiwidGlkIjoiOGQyYzAzMmMtNGQxMy00OWZkLTg1MWUtNzg4NjM" +
                "yMTk1MDdiIiwidW5pcXVlX25hbWUiOiJqb3NlLmNvbGxhenppQG1hbnl3aG8uY29tIiwidXBuIjoiam9zZS5jb2xsYXp6aUBtYW55" +
                "d2hvLmNvbSIsInV0aSI6ImM3Nl9HMGU5aGtPNDlLdHdiZjhFQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtN" +
                "DIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.BoOPqAMhfFab5LhFJvlOsrphftVbO7TOgaWG_MGyQrIhjZkr6V0c38meEoK1vGd-J4o" +
                "WYKhKtAAsmbYj500oRuZISCQiRq932u58fsb3yjMBAO3WvTrAhKLSUJuTL9X87QdlC1CmE-jBBNxXFu8mmQ_tTWbox0-s6TooGRbd" +
                "UUUKYLG2TbTuc92mVjOjw-ZQ8sEBYE9FWtryaAmXeSJywTn6ItgIYBs23KVJWMWDgGSvss03GQQMGUf3MG0J8PJsleNcmcO_A62o4" +
                "Ut3QVaigyXVua7FcFUmy3-fnSLwmeP4uGHRz2-ugOJQaGA5nzaQSfQqpAMGoKeRvubFJlMsEg";

        String compressedToken = Compressor.compress(token);
        String decompressedToken = Compressor.decompress(compressedToken);

        Assert.assertTrue("the compressions is very bad", compressedToken.length() < token.length());

        Assert.assertEquals(token, decompressedToken);
    }
}
