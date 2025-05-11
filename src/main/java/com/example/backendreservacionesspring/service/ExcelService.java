package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Assistance;
import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.model.Student;
import com.example.backendreservacionesspring.repository.AssistanceRepository;
import com.example.backendreservacionesspring.repository.StudentRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExcelService {

    private final StudentRepository studentRepository;
    private final AssistanceRepository assistanceRepository;

    public ExcelService(AssistanceRepository assistanceRepository, StudentRepository studentRepository) {
        this.assistanceRepository = assistanceRepository;
        this.studentRepository = studentRepository;
    }

    public List<Long> extractStudentIdsFromCsv(MultipartFile file) throws IOException {
        List<Long> studentIds = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] linea;
            while ((linea = reader.readNext()) != null) {
                if (linea.length > 0 && !linea[0].isEmpty()) {
                    try {
                        studentIds.add(Long.parseLong(linea[0].trim())); // Asume que el ID está en la primera columna
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Formato de ID inválido: " + linea[0]);
                    }
                }
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return studentIds;
    }

    // Método para registrar asistencia
    public Long registerAssistance(LocalDateTime dateAssistance, List<Long> studentIds, Reservation reservation) {
        Assistance assistance = new Assistance();
        assistance.setDateAssistance(dateAssistance);
        assistance.setReservation(reservation);

        Set<Student> students = studentIds.stream()
                .map(id -> studentRepository.findById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        assistance.setStudents(students);
        Assistance savedAssistance = assistanceRepository.save(assistance); // Guarda y obtén la entidad
        return savedAssistance.getId(); // Retorna el ID real (¡nunca null!)
    }
}
