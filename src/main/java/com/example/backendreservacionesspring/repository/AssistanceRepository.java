package com.example.backendreservacionesspring.repository;

import com.example.backendreservacionesspring.model.Assistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AssistanceRepository extends JpaRepository<Assistance,Long> {
}
