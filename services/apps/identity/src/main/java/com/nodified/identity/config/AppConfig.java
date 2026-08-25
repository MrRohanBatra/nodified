package com.nodified.identity.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nodified.identity.dto.response.TenantCreated;
import com.nodified.identity.entity.Tenants;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
            .createTypeMap(Tenants.class, TenantCreated.class)
            .setConverter(ctx -> {
                Tenants source = ctx.getSource();
                if (source == null) {
                    return null;
                }
                return TenantCreated.builder()
                    .id(source.getPublicId())
                    .name(source.getName())
                    .key(source.getKey())
                    .build();
            });
        return modelMapper;
    }
}