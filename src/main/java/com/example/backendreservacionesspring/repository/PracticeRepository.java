package com.example.backendreservacionesspring.repository;

import com.example.backendreservacionesspring.model.Practices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practices, Long> {

    // Método para buscar prácticas por ID de asignatura
    List<Practices> findBySignatureId(Long signatureId);

    // Método para verificar si existe una práctica con nombre específico en una asignatura
    boolean existsByNameAndSignatureId(String name, Long signatureId);

    // Método para buscar prácticas por nombre (insensible a mayúsculas/minúsculas)
    List<Practices> findByNameContainingIgnoreCase(String name);
}