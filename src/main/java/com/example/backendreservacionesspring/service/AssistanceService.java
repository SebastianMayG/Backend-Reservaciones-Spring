package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Assistance;
import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.model.Student;
import com.example.backendreservacionesspring.repository.AssistanceRepository;
import com.example.backendreservacionesspring.repository.ReservationRepository;
import com.example.backendreservacionesspring.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AssistanceService {
    private final AssistanceRepository assistanceRepository;
    private final StudentRepository studentRepository;
    private final ReservationRepository reservationRepository;

    public AssistanceService(AssistanceRepository assistanceRepository, StudentRepository studentRepository, ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
        this.studentRepository = studentRepository;
        this.assistanceRepository = assistanceRepository;
    }

    public List<Assistance> findAll() {
        return assistanceRepository.findAll();
    }

    public Optional<Assistance> findById(Long id) {
        return assistanceRepository.findById(id);
    }

    @Transactional
    public Assistance save(Assistance assistance) {
        // Validar que tenga reservación
        if (assistance.getReservation() == null || assistance.getReservation().getId() == null) {
            throw new IllegalArgumentException("La reservación es requerida");
        }

        // Obtener la reservación completa de la base de datos
        Reservation reservation = reservationRepository.findById(assistance.getReservation().getId())
                .orElseThrow(() -> new IllegalArgumentException("Reservación no encontrada"));

        // Asignar automáticamente la fecha de la reservación a la asistencia
        LocalDateTime reservationDateTime = LocalDateTime.of(
                reservation.getDate(),
                reservation.getStartTime()
        );
        assistance.setDateAssistance(reservationDateTime);

        // Manejo de estudiantes (manteniendo tu lógica actual)
        if (assistance.getStudents() != null && !assistance.getStudents().isEmpty()) {
            Set<Student> managedStudents = new HashSet<>();
            for (Student student : assistance.getStudents()) {
                Student managedStudent = studentRepository.findById(Long.valueOf(student.getId()))
                        .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + student.getId()));
                managedStudents.add(managedStudent);
            }
            assistance.setStudents(managedStudents);
        }

        return assistanceRepository.save(assistance);
    }
}
