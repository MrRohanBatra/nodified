package com.nodified.identity.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.nodified.identity.dto.request.RegisterTenant;
import com.nodified.identity.dto.response.TenantCreated;
import com.nodified.identity.entity.Tenant;
import com.nodified.identity.repository.TenantRepository;
import com.nodified.identity.service.TenantService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    private final ModelMapper modelMapper;

    @Override
    public TenantCreated registerTenant(RegisterTenant registerTenant) {
        log.info("Tenant Create Request Recieved");
        Tenant tenant = tenantRepository.save(modelMapper.map(registerTenant, Tenant.class));
        log.info("Tenant Saved with id:{} publicId:{} name:{}", tenant.getId(), tenant.getPublicId(), tenant.getName());
        return modelMapper.map(tenant, TenantCreated.class);
    }
}
