package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.Assistance;
import com.example.backendreservacionesspring.service.AssistanceService;
import com.example.backendreservacionesspring.service.PDFService;
import com.itextpdf.text.DocumentException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("assistances")
public class AssistanceController {

    private final AssistanceService assistanceService;
    private final PDFService pdfService;

    public AssistanceController(AssistanceService assistanceService, PDFService pdfService) {
        this.assistanceService = assistanceService;
        this.pdfService = pdfService;
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
    public ResponseEntity<Assistance> createAssistance(@RequestBody Assistance assistance) {
        try {
            Assistance savedAssistance = assistanceService.save(assistance);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAssistance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Nuevo endpoint para PDF (agregado al final)
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generateAssistancePdf(@PathVariable Long id) {
        try {
            Assistance assistance = assistanceService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));

            byte[] pdfBytes = pdfService.generatePdf(assistance);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                    ContentDisposition.attachment()
                            .filename("asistencia_" + id + ".pdf")
                            .build()
            );

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (DocumentException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}