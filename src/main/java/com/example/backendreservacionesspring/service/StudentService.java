package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Student;
import com.example.backendreservacionesspring.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    //Encontrar todos los estudiantes
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
    //Encontrar por ID
    //Creo puede ser opcional
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }
}
