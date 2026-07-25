package com.krushimitra.app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private ERole name;

    public Role() {}
    public Role(Long id, ERole name) { this.id = id; this.name = name; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ERole getName() { return name; }
    public void setName(ERole name) { this.name = name; }

    public enum ERole {
        ROLE_FARMER, ROLE_ADMIN, ROLE_SUPER_ADMIN
    }
}
