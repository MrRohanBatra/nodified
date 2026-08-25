package com.nodified.identity.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodified.identity.entity.Tenants;

@Repository
public interface  TenantRepository extends JpaRepository<Tenants,UUID>{
    
}
