package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.Assistance;
import com.example.backendreservacionesspring.model.Reservation;
import com.example.backendreservacionesspring.service.AssistanceService;
import com.example.backendreservacionesspring.service.ExcelService;
import com.example.backendreservacionesspring.service.PDFService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.itextpdf.text.DocumentException;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/assistances")
public class AssistanceController {

    private final AssistanceService assistanceService;
    private final PDFService pdfService;
    private final ExcelService excelService;

    public AssistanceController(AssistanceService assistanceService, PDFService pdfService, ExcelService excelService) {
        this.assistanceService = assistanceService;
        this.pdfService = pdfService;
        this.excelService = excelService;
    }

    @GetMapping
    public List<Assistance> findAllAssistance() {
        return assistanceService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Assistance> findAssistanceById(@PathVariable Long id) {
        return assistanceService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> createAssistance(@Valid @RequestBody Assistance assistance) {
        try {
            Assistance savedAssistance = assistanceService.save(assistance);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAssistance);
        } catch (RuntimeException e) {
            // Crear un objeto JSON con el mensaje de error
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error al crear la asistencia");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }
    }

    // Nuevo endpoint para PDF (agregado al final)
    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> generateAssistancePdf(@PathVariable Long id) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode errorResponse = objectMapper.createObjectNode();

        try {
            // 1. Buscar la asistencia
            Assistance assistance = assistanceService.findById(id)
                    .orElseThrow(() -> {
                        errorResponse.put("error", "Asistencia no encontrada");
                        errorResponse.put("id", id);
                        return new RuntimeException(errorResponse.toString());
                    });

            // 2. Validar que dateAssistance no sea nulo
            if (assistance.getDateAssistance() == null) {
                errorResponse.put("error", "La fecha de asistencia es requerida");
                errorResponse.put("id", id);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }

            // 3. Validar que exista la reservación y su fecha
            if (assistance.getReservation() == null || assistance.getReservation().getDate() == null) {
                errorResponse.put("error", "La reservación no tiene fecha válida");
                errorResponse.put("id", id);
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorResponse);
            }

            // 4. Generar PDF (el PDFService ya recibe toda la asistencia con su reservación)
            byte[] pdfBytes = pdfService.generatePdf(assistance);

            // 5. Configurar respuesta exitosa
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("asistencia_" + id + ".pdf")
                            .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (RuntimeException e) {
            // Error 404 - No encontrado
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());

        } catch (DocumentException e) {
            // Error 500 - Problema al generar PDF
            errorResponse.put("error", "Error al generar el PDF");
            errorResponse.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);

        } catch (Exception e) {
            // Error 500 - Otros errores
            errorResponse.put("error", "Error inesperado");
            errorResponse.put("detalle", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(errorResponse);
        }
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadAssistance(
            @RequestParam("file") MultipartFile file,
            @RequestParam("reservationId") Long reservationId,
            @RequestParam("dateAssistance") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateAssistance) {

        try {
            List<Long> studentIds = excelService.extractStudentIdsFromCsv(file);
            Reservation reservation = new Reservation();
            reservation.setId(reservationId);

            // Línea clave: Asegúrate de que registerAssistance retorne el ID.
            Long assistanceId = excelService.registerAssistance(dateAssistance, studentIds, reservation);

            // Respuesta JSON manual (como antes, pero ahora incluyendo assistanceId)
            String jsonResponse = String.format(
                    "{\"success\": true, \"message\": \"Asistencia registrada para %d estudiantes.\", \"students\": %s, \"assistanceId\": %d}",
                    studentIds.size(),
                    new ObjectMapper().writeValueAsString(studentIds),
                    assistanceId
            );

            return ResponseEntity.ok().body(jsonResponse);

        } catch (IOException e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"Error al procesar el archivo.\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }
}