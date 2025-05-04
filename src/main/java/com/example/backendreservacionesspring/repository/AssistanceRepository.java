package com.example.backendreservacionesspring.repository;

import com.example.backendreservacionesspring.model.Assistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AssistanceRepository extends JpaRepository<Assistance,Long> {

    @Query("SELECT a FROM Assistance a " +
            "LEFT JOIN FETCH a.students " +
            "LEFT JOIN FETCH a.reservation r " +
            "LEFT JOIN FETCH r.space " +
            "LEFT JOIN FETCH r.user " +
            "WHERE a.id = :id")
    Optional<Assistance> findByIdWithDetails(@Param("id") Long id);
}
