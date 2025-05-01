package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.Practices;
import com.example.backendreservacionesspring.service.PracticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/practices")
public class PracticeController {

    private final PracticeService practiceService;

    public PracticeController(PracticeService practiceService) {
        this.practiceService = practiceService;
    }

    @GetMapping
    public ResponseEntity<List<Practices>> getAllPractices() {
        List<Practices> practices = practiceService.findAll();
        return ResponseEntity.ok(practices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practices> getPracticeById(@PathVariable Long id) {
        return practiceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-signature/{signatureId}")
    public ResponseEntity<List<Practices>> getPracticesBySignature(@PathVariable Long signatureId) {
        List<Practices> practices = practiceService.findBySignatureId(signatureId);
        return ResponseEntity.ok(practices);
    }

    @PostMapping
    public ResponseEntity<Practices> createPractice(@RequestBody Practices practice) {
        Practices savedPractice = practiceService.savePractice(practice);
        return ResponseEntity.created(URI.create("/api/practices/" + savedPractice.getId()))
                .body(savedPractice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Practices> updatePractice(
            @PathVariable Long id,
            @RequestBody Practices practiceDetails) {
        Practices updatedPractice = practiceService.update(id, practiceDetails);
        return ResponseEntity.ok(updatedPractice);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Practices> partialUpdatePractice(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Practices updatedPractice = practiceService.updatePartial(id, updates);
        return ResponseEntity.ok(updatedPractice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractice(@PathVariable Long id) {
        practiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}