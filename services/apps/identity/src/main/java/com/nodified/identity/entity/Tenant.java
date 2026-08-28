package com.nodified.identity.entity;

import com.nodified.identity.utils.PublicIdPrefix;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tenants")
@PublicIdPrefix("tnt")
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Tenant extends BaseEntity {
    @Column(name="name")
    private String name;

    @Column(name="key")
    private String key;
}
