package com.nodified.identity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodified.identity.dto.request.RegisterTenant;
import com.nodified.identity.dto.response.TenantCreated;
import com.nodified.identity.service.TenantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class authController {
  private final TenantService tenantService;

  @PostMapping
  public ResponseEntity<TenantCreated> registerTenant(
      @RequestBody @Valid RegisterTenant registerTenant) {
    return ResponseEntity.ok().body(tenantService.registerTenant(registerTenant));
  }
}
