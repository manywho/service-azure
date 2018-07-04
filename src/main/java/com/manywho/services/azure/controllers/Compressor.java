package com.manywho.services.azure.controllers;

import java.io.*;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Compressor {
    public static String compress(String data) throws IOException {

            // Create an output stream, and a gzip stream to wrap over.
            ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length());
            GZIPOutputStream gzip = new GZIPOutputStream(bos);

            // Compress the input string
            gzip.write(data.getBytes());
            gzip.close();
            byte[] compressed = bos.toByteArray();
            bos.close();
            compressed = Base64.getEncoder().encode(compressed);
            return new String(compressed);
    }



    public static String decompress(String compressedText) throws IOException {
        // get the bytes for the compressed string
        byte[] compressed = compressedText.getBytes("UTF8");
        Base64.Decoder d = Base64.getDecoder();
        compressed = d.decode(compressed);

        // decode.
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1)
        {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }

}
