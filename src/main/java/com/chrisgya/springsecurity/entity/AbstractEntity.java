package com.chrisgya.springsecurity.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Data
@MappedSuperclass
public class AbstractEntity {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant created;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Version
    private long version;
}
