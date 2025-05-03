package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.Practice;
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
    public ResponseEntity<List<Practice>> getAllPractices() {
        List<Practice> practices = practiceService.findAll();
        return ResponseEntity.ok(practices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Practice> getPracticeById(@PathVariable Long id) {
        return practiceService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-signature/{signatureId}")
    public ResponseEntity<List<Practice>> getPracticesBySignature(@PathVariable Long signatureId) {
        List<Practice> practices = practiceService.findBySignatureId(signatureId);
        return ResponseEntity.ok(practices);
    }

    @PostMapping
    public ResponseEntity<Practice> createPractice(@RequestBody Practice practice) {
        Practice savedPractice = practiceService.savePractice(practice);
        return ResponseEntity.created(URI.create("/api/practices/" + savedPractice.getId()))
                .body(savedPractice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Practice> updatePractice(
            @PathVariable Long id,
            @RequestBody Practice practiceDetails) {
        Practice updatedPractice = practiceService.update(id, practiceDetails);
        return ResponseEntity.ok(updatedPractice);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Practice> partialUpdatePractice(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Practice updatedPractice = practiceService.updatePartial(id, updates);
        return ResponseEntity.ok(updatedPractice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractice(@PathVariable Long id) {
        practiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}