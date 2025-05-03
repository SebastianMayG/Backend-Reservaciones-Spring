package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.Assistance;
import com.example.backendreservacionesspring.service.AssistanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("assistances")
public class AssistanceController {

    private final AssistanceService assistanceService;

    public AssistanceController(AssistanceService assistanceService) {
        this.assistanceService = assistanceService;
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
}
