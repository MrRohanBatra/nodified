package com.nodified.identity.seeder;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "app.bootstrap")
@Data
public class BootStrapProperties {
    private boolean enabled;
    private String tenantKey;
    private String ownerEmail;
    private String ownerPassword;
}
