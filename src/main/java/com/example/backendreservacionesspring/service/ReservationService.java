package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final EmailService emailService;

    public ReservationService(ReservationRepository reservationRepository, EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
    }

    // Obtener todas las reservaciones
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // Obtener reservaciones paginadas
    public Page<Reservation> findAllPaginated() {
        return reservationRepository.findAll(Pageable.ofSize(10));
    }

    // Buscar reservaciones por fecha y espacio
    public List<Reservation> findByDateAndSpace(LocalDate date, Long spaceId) {
        return reservationRepository.findByDateAndSpaceId(date, spaceId);
    }

    // Crear una nueva reservación (con transacción)
    @Transactional
    public Reservation create(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        // Enviar email de confirmación
        //emailService.sendVerificationEmail(savedReservation.getUser().getEmail());
        return savedReservation;
    }

    // Obtener una reservación por ID
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    // Obtener reservaciones de un usuario
    public List<Reservation> findByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    // Eliminar una reservación
    public void delete(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new EntityNotFoundException("Reservación no encontrada con id: " + id);
        }
        reservationRepository.deleteById(id);
    }

    // Actualizar una reservación completamente
    public Reservation update(Long id, Reservation reservationDetails) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setDate(reservationDetails.getDate());
                    reservation.setStartTime(reservationDetails.getStartTime());
                    reservation.setEndTime(reservationDetails.getEndTime());
                    reservation.setUser(reservationDetails.getUser());
                    reservation.setSpace(reservationDetails.getSpace());
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new EntityNotFoundException("Reservación no encontrada"));
    }

    // Actualización parcial de una reservación
    public Reservation updatePartial(Long id, Map<String, Object> updates) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "date":
                                reservation.setDate((LocalDate) value);
                                break;
                            case "startTime":
                                reservation.setStartTime((LocalTime) value);
                                break;
                            case "endTime":
                                reservation.setEndTime((LocalTime) value);
                                break;
                            case "userId":
                                // Aquí deberías buscar el User correspondiente
                                break;
                            case "spaceId":
                                // Aquí deberías buscar el Space correspondiente
                                break;
                        }
                    });
                    return reservationRepository.save(reservation);
                })
                .orElseThrow(() -> new EntityNotFoundException("Reservación no encontrada"));
    }
}