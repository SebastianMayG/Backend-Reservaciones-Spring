package com.example.backendreservacionesspring.repository;

import com.example.backendreservacionesspring.model.Reservation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // Buscar reservaciones por fecha y espacio
    @Query("SELECT r FROM Reservation r WHERE r.date = :date AND r.space.id = :spaceId")
    List<Reservation> findByDateAndSpaceId(@Param("date") LocalDate date, @Param("spaceId") Long spaceId);

    // Buscar reservaciones de un usuario
    List<Reservation> findByUserId(Long userId);

    // Método para verificar disponibilidad
    @Query("SELECT r FROM Reservation r WHERE r.space.id = :spaceId AND r.date = :date " +
            "AND ((r.startTime < :endTime AND r.endTime > :startTime))")
    List<Reservation> findConflictingReservations(
            @Param("spaceId") Long spaceId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
  
  @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN FETCH r.space " +      // Carga los datos de la sala
            "LEFT JOIN FETCH r.user " +       // Carga los datos del profesor
            "WHERE r.id = :id")
    Optional<Reservation> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r WHERE r.date = :date " +
            "AND r.startTime BETWEEN :startTime AND :endTime " +
            "AND r.reminderSent = false")
    List<Reservation> findByDateAndStartTimeBetween(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime timeEnd);

}

