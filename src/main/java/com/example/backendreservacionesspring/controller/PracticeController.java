package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.Practice;
import com.example.backendreservacionesspring.model.Signature;
import com.example.backendreservacionesspring.repository.SignatureRepository;
import com.example.backendreservacionesspring.service.PracticeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/practices")
public class PracticeController {

    private final PracticeService practiceService;
    private final SignatureRepository signatureRepository;

    // Inyección de dependencias mejorada
    public PracticeController(PracticeService practiceService,
                              SignatureRepository signatureRepository) {
        this.practiceService = practiceService;
        this.signatureRepository = signatureRepository;
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
    public ResponseEntity<Practice> createPractice(@Valid @RequestBody Practice practice) {
        // Validación y carga de la signature
        if (practice.getSignature() == null || practice.getSignature().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        Long signatureId = practice.getSignature().getId();
        Optional<Signature> signature = signatureRepository.findById(signatureId);

        if (signature.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Asignamos la signature gestionada por JPA
        practice.setSignature(signature.get());

        Practice savedPractice = practiceService.savePractice(practice);

        // URI de creación más robusta
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPractice.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedPractice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Practice> updatePractice(
            @PathVariable Long id,
            @Valid @RequestBody Practice practiceDetails) {

        // Verificar existencia de la práctica
        if (!practiceService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Manejo de la relación signature
        if (practiceDetails.getSignature() != null && practiceDetails.getSignature().getId() != null) {
            Optional<Signature> signature = signatureRepository.findById(practiceDetails.getSignature().getId());
            if (signature.isPresent()) {
                practiceDetails.setSignature(signature.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }

        Practice updatedPractice = practiceService.update(id, practiceDetails);
        return ResponseEntity.ok(updatedPractice);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Practice> partialUpdatePractice(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        // Verificar existencia de la práctica
        if (!practiceService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Manejo especial para actualización de signature
        if (updates.containsKey("signature")) {
            @SuppressWarnings("unchecked")
            Map<String, Object> signatureMap = (Map<String, Object>) updates.get("signature");
            if (signatureMap != null && signatureMap.containsKey("id")) {
                Long signatureId = Long.valueOf(signatureMap.get("id").toString());
                Optional<Signature> signature = signatureRepository.findById(signatureId);
                if (signature.isPresent()) {
                    updates.put("signature", signature.get());
                } else {
                    return ResponseEntity.badRequest().build();
                }
            }
        }

        Practice updatedPractice = practiceService.updatePartial(id, updates);
        return ResponseEntity.ok(updatedPractice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractice(@PathVariable Long id) {
        if (!practiceService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        practiceService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}