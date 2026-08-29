package com.nodified.identity.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodified.identity.dto.request.RegisterTenant;
import com.nodified.identity.dto.response.TenantCreated;
import com.nodified.identity.service.TenantService;
import com.nodified.identity.utils.ApiEnvelope;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
  private final TenantService tenantService;

  @PostMapping
  public ResponseEntity<ApiEnvelope<TenantCreated>> registerTenant(
      @RequestBody @Valid RegisterTenant registerTenant) {
    TenantCreated tenantCreated = tenantService.registerTenant(registerTenant);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiEnvelope.success(tenantCreated, "Tenant registered successfully"));
  }

  @GetMapping("/ping")
  public ResponseEntity<ApiEnvelope<Map<String, String>>> ping() {
    return ResponseEntity.ok()
        .body(ApiEnvelope.success(Map.of("message", "hello world")));
  }
}
