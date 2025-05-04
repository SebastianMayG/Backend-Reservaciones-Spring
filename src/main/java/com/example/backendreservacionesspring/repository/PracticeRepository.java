package com.example.backendreservacionesspring.repository;

import com.example.backendreservacionesspring.model.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {

    List<Practice> findBySignatureId(Long signatureId);

    boolean existsByNameAndSignatureId(String name, Long signatureId);

    List<Practice> findByNameContainingIgnoreCase(String name);
}