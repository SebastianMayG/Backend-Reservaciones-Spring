package com.example.backendreservacionesspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "practices")
@Getter @Setter
public class Practice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "El nombre de la practica es obligatorio")
    String name;

    @Column(name = "url", nullable = false)
    @NotBlank(message = "La dirección URL es obligatoria")
    String url;

    @Column(name = "create_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @ManyToOne
    @JoinColumn(name = "id_asignatura", nullable = false)
    @JsonIgnore
    private Signature signature;
}
