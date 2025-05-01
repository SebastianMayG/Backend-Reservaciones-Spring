package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Practices;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.example.backendreservacionesspring.repository.PracticeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PracticeService {
    private final PracticeRepository practiceRepository;

    public PracticeService(PracticeRepository practiceRepository) {
        this.practiceRepository = practiceRepository;
    }

    public List<Practices> findAll() {
        return practiceRepository.findAll();
    }

    public Optional<Practices> findById(Long id) {
        return practiceRepository.findById(id);
    }

    public Practices savePractice(Practices practice) {
        return practiceRepository.save(practice);
    }

    public Practices update(Long id, Practices practiceUpdated) {
        return practiceRepository.findById(id).map(practice -> {
            practice.setName(practiceUpdated.getName());
            practice.setUrl(practiceUpdated.getUrl());
            practice.setSignature(practiceUpdated.getSignature());

            return practiceRepository.save(practice);
        }).orElseThrow(() -> new RuntimeException("Práctica no encontrada"));
    }

    public Practices updatePartial(Long id, Map<String, Object> campos) {
        return practiceRepository.findById(id).map(practice -> {
            if(campos.containsKey("name")) {
                practice.setName((String) campos.get("name"));
            }

            if(campos.containsKey("url")) {
                practice.setUrl((String) campos.get("url"));
            }

            if(campos.containsKey("id_signature")) {
                // Aquí deberías buscar la Signature correspondiente y asignarla
                // practice.setSignature(signatureService.findById((Long) campos.get("id_signature")));
            }

            return practiceRepository.save(practice);
        }).orElseThrow(() -> new RuntimeException("Práctica no encontrada"));
    }

    public void deleteById(Long id) {
        if (!practiceRepository.existsById(id)) {
            throw new EntityNotFoundException("Práctica no encontrada con id: " + id);
        }
        practiceRepository.deleteById(id);
    }

    // Método adicional para buscar prácticas por asignatura
    public List<Practices> findBySignatureId(Long signatureId) {
        return practiceRepository.findBySignatureId(signatureId);
    }
}