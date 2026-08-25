package com.nodified.identity.service;

import com.nodified.identity.dto.request.RegisterTenant;
import com.nodified.identity.dto.response.TenantCreated;

public interface  TenantService {
    public TenantCreated registerTenant(RegisterTenant registerTenant);
}
