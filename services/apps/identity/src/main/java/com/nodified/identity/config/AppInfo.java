package com.nodified.identity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AppInfo {

    @Autowired(required = false)
    private BuildProperties buildProperties;

    @Value("${app.version:}")
    private String configuredVersion;

    private static String version = "0.1.0-SNAPSHOT";

    @PostConstruct
    public void init() {
        if (configuredVersion != null && !configuredVersion.isBlank()) {
            version = configuredVersion;
        } else if (buildProperties != null && buildProperties.getVersion() != null) {
            version = buildProperties.getVersion();
        }
    }

    public static String getVersion() {
        return version;
    }
}
