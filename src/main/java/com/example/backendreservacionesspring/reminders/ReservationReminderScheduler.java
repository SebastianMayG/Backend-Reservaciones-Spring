package com.example.backendreservacionesspring.reminders;

import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.model.User;
import com.example.backendreservacionesspring.repository.ReservationRepository;
import com.example.backendreservacionesspring.service.EmailService;
import com.example.backendreservacionesspring.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ReservationReminderScheduler {

    private final ReservationRepository reservationRepository;
    private final EmailService emailService;
    private final UserService userService;

    public ReservationReminderScheduler(ReservationRepository reservationRepository, EmailService emailService, UserService userService) {
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
        this.userService = userService;
    }

    @Scheduled(fixedRate = 60000) // Ejecuta cada minuto (ajusta según necesidades)
    public void checkReservationsForReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime currentTime = LocalTime.now();

        // Busca reservaciones que ocurran exactamente en 24 horas
        List<Reservation> reservations = reservationRepository
                .findByDateAndStartTimeBetween(tomorrow,
                        currentTime.minusMinutes(1),
                        currentTime.plusMinutes(1));

                //user.findById(reservation.getUser().getId()).orElseThrow(EntityNotFoundException::new);
        reservations.forEach(reservation -> {
            if (!reservation.isReminderSent()) {
                emailService.sendReservationReminder(
                        reservation.getUser().getEmail(),
                        reservation,
                        userService.findById(reservation.getUser().getId()).orElseThrow(EntityNotFoundException::new)
                );
                // Marcar como enviado (necesitarás agregar este campo a tu entidad)
                reservation.setReminderSent(true);
                reservationRepository.save(reservation);
            }
        });
    }
}
