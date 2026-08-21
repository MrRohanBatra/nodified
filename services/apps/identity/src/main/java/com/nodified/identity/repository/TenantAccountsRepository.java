package com.nodified.identity.repository;

import com.nodified.identity.entity.TenantAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantAccountsRepository extends JpaRepository<TenantAccounts, UUID> {
    Optional<TenantAccounts> findByKey(String key);
    boolean existsByKey(String key);
}
