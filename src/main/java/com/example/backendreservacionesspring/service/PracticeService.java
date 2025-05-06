package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Practice;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    public List<Practice> findAll() {
        return practiceRepository.findAll();
    }

    public Optional<Practice> findById(Long id) {
        return practiceRepository.findById(id);
    }

    public Practice savePractice(Practice practice) {
        return practiceRepository.save(practice);
    }

    public Practice update(Long id, Practice practiceUpdated) {
        return practiceRepository.findById(id).map(practice -> {
            practice.setName(practiceUpdated.getName());
            practice.setUrl(practiceUpdated.getUrl());
            practice.setSignature(practiceUpdated.getSignature());

            return practiceRepository.save(practice);
        }).orElseThrow(() -> new RuntimeException("Práctica no encontrada"));
    }

    public Practice updatePartial(Long id, Map<String, Object> campos) {
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
    public List<Practice> findBySignatureId(Long signatureId) {
        return practiceRepository.findBySignatureId(signatureId);
    }

    @Transactional
    public Practice save(Practice practice) {
        // Validaciones básicas
        if (practice.getName() == null || practice.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la práctica no puede estar vacío");
        }

        if (practice.getUrl() == null || practice.getUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("La URL de la práctica no puede estar vacía");
        }

        if (practice.getSignature() == null || practice.getSignature().getId() == null) {
            throw new IllegalArgumentException("La práctica debe estar asociada a una asignatura válida");
        }

        return practiceRepository.save(practice);
    }
}