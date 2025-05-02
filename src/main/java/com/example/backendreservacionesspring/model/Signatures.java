package com.example.backendreservacionesspring.model;


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
@Table(name = "signature")
@Getter @Setter

public class Signatures {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "name", nullable = false)
    @NotBlank(message = "Nombre de la asignatura obligatorio")
    private String nombre;

    @Column(name = "create_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "signature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Practices> practices = new ArrayList<>();
}
