package com.nodified.identity.dto.request;

import lombok.Data;

@Data
public class RegisterTenant {
    private String name;
    private String key;    
}
