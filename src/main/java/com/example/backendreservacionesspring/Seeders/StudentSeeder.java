package com.example.backendreservacionesspring.Seeders;

import com.example.backendreservacionesspring.model.Student;
import com.example.backendreservacionesspring.repository.StudentRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class StudentSeeder {

    private final StudentRepository studentRepository;

    public StudentSeeder(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void seedStudent() {
        if (studentRepository.count() == 0) {
            List<Student> students = Arrays.asList(
                    createStudent("Maria"),
                    createStudent("Jose"),
                    createStudent("Luis")
            );
            studentRepository.saveAll(students);
            System.out.println("Estudiantes insertados: " + students.size());
        }
    }

    private Student createStudent(String name) {
        Student student = new Student();
        student.setName(name);
        student.setLists(new HashSet<>());
        return student;
    }
}
