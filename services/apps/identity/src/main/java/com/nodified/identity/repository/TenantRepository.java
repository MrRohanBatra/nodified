package com.nodified.identity.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodified.identity.entity.Tenant;

@Repository
public interface  TenantRepository extends JpaRepository<Tenant,UUID>{
    Optional<Tenant> findByKey(String key);
}
