package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Signature;
import com.example.backendreservacionesspring.repository.SignatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SignatureService {
    private final SignatureRepository signatureRepository;

    public SignatureService(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    public List<Signature> findAll() {
        return signatureRepository.findAll();
    }

    public Optional<Signature> findById(Long id) {
        return signatureRepository.findById(id);
    }

    public Optional<Signature> findByName(String name) {
        return signatureRepository.findByName(name);
    }

    public Signature save(Signature signature) {
        return signatureRepository.save(signature);
    }

    public Signature deleteById(Long id) {
        Signature signature = signatureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignatura no encontrada con id: " + id));
        signatureRepository.deleteById(id);
        return signature;
    }

}
