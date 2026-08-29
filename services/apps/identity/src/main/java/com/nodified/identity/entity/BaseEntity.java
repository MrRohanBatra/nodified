package com.nodified.identity.entity;

import java.util.UUID;

import com.nodified.identity.utils.PublicIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private UUID id;

    @Column(name="public_id")
    private String publicId;

    @PrePersist
    public void generatePublicId(){
        publicId=PublicIdGenerator.generate(this.getClass());
    }
}
