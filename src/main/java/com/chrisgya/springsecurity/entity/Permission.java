package com.chrisgya.springsecurity.entity;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@ApiResponse
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "permissions", schema = "bp")
@BatchSize(size = 50)
public class Permission extends AbstractEntity implements Serializable {
    @Column(name = "name",unique = true, nullable = false, length = 50)
    private String name;
    @Column(name = "description", length = 100)
    private String description;
}
