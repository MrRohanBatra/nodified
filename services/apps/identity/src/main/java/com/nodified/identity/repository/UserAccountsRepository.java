package com.nodified.identity.repository;

import com.nodified.identity.entity.UserAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAccountsRepository extends JpaRepository<UserAccounts, UUID> {
    Optional<UserAccounts> findByUsername(String username);
    boolean existsByUsername(String username);
}
