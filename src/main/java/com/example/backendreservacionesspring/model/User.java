package com.example.backendreservacionesspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "El apellido es obligatorio")
    private String last_name;

    @Column(nullable = false)
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "El password es obligatorio")
    private String password;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();;

//    public Integer getRole() {
//        return role != null ? role.getId() : null;
//    }

    @JsonProperty("role")
    public Integer getRoleId() {
        return role != null ? role.getId() : null;
    }

}
