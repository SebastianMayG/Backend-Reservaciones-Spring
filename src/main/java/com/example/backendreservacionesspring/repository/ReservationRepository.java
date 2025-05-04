package com.example.backendreservacionesspring.repository;

import com.example.backendreservacionesspring.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository {

    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN FETCH r.space " +      // Carga los datos de la sala
            "LEFT JOIN FETCH r.user " +       // Carga los datos del profesor
            "WHERE r.id = :id")
    Optional<Reservation> findByIdWithRelations(@Param("id") Long id);
}
