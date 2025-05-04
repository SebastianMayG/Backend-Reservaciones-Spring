package com.example.backendreservacionesspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter @Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // Usamos Integer para coincidir con el esquema

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @ManyToMany(mappedBy = "students") // Mapeado por el campo en List
    @JsonIgnore // ← Esto evita que se serialice esta propiedad
    private Set<Assistance> assistances = new HashSet<>();
}
