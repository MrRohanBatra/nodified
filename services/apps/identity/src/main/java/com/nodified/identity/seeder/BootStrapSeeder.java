package com.nodified.identity.seeder;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.nodified.identity.entity.Tenant;
import com.nodified.identity.repository.TenantRepository;
import com.nodified.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BootStrapSeeder implements ApplicationRunner {
    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final BootStrapProperties bootStrapProperties;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        Tenant tenant=tenantRepository.findByKey(bootStrapProperties.getTenantKey()).orElseGet(() -> tenantRepository.save(
                Tenant.builder()
                    .name(properties.getTenantName())
                    .key(properties.getTenantKey())
                    .build()
            ));
    }
    
}
