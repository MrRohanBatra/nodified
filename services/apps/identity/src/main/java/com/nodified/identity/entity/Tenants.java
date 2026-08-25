package com.nodified.identity.entity;

import com.nodified.identity.utils.PublicIdPrefix;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@PublicIdPrefix("tnt")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenants extends BaseEntity {
    @Column(name="name")
    private String name;

    @Column(name="key")
    private String key;
}
