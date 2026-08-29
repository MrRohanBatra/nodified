package com.nodified.identity.enums;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    SUPER_ADMIN, //only given to nodified owner, so that he can make tenant and add the admin of tenant
    ADMIN,
    WRITE,
    READ;

    public List<SimpleGrantedAuthority> getAuthorities() {
        return Arrays.stream(values())
            .filter(r -> r.ordinal() >= this.ordinal())
            .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
            .toList();
    }
}
