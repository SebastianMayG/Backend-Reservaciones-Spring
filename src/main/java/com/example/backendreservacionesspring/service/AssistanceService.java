package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Assistance;
import com.example.backendreservacionesspring.model.Student;
import com.example.backendreservacionesspring.repository.AssistanceRepository;
import com.example.backendreservacionesspring.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AssistanceService {
    private final AssistanceRepository assistanceRepository;
    private final StudentRepository studentRepository;

    public AssistanceService(AssistanceRepository assistanceRepository, StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.assistanceRepository = assistanceRepository;
    }

    public List<Assistance> findAll() {
        return assistanceRepository.findAll();
    }

    public Optional<Assistance> findById(Long id) {
        return assistanceRepository.findById(id);
    }

    public Assistance save(Assistance assistance) {
        System.out.println("Fecha recibida: " + assistance.getDateAssistance());
        // Verifica y asigna estudiantes existentes
        if (assistance.getStudents() != null) {
            Set<Student> managedStudents = new HashSet<>();
            for (Student student : assistance.getStudents()) {
                // Busca cada estudiante en la BD para evitar duplicados
                Student managedStudent = studentRepository.findById(Long.valueOf(student.getId()))
                        .orElseThrow(() -> new RuntimeException("Estudiante no encontrado con ID: " + student.getId()));
                managedStudents.add(managedStudent);
            }
            assistance.setStudents(managedStudents);
        }

        return assistanceRepository.save(assistance);
    }
}
