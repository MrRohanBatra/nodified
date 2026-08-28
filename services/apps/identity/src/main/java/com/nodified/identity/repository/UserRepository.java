package com.nodified.identity.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nodified.identity.entity.User;

public interface  UserRepository extends JpaRepository<User,UUID> {
    boolean existsByEmailAndTenant_Id(String email,UUID tenantId);    
}
