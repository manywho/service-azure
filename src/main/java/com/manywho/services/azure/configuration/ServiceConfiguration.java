package com.manywho.services.azure.configuration;

public interface ServiceConfiguration {
    String get(String key);
    boolean has(String key);
}
