package com.example.backendreservacionesspring.repository;

import com.example.backendreservacionesspring.model.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, Long> {
    Optional<Signature> findByName(String nombre);
}
