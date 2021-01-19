package com.chrisgya.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "refresh_token", schema = "bp")
public class RefreshToken extends SlimAbstractEntity implements Serializable {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "token", columnDefinition = "Text")
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;
}
