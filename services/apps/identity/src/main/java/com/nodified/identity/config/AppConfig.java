package com.nodified.identity.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nodified.identity.dto.response.TenantCreated;
import com.nodified.identity.entity.Tenant;
import com.nodified.identity.utils.MetaObject;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
            .createTypeMap(Tenant.class, TenantCreated.class)
            .setConverter(ctx -> {
                Tenant source = ctx.getSource();
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

    @Bean
    public MetaObject metaObject(){
        return new MetaObject();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}