package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Signature;
import com.example.backendreservacionesspring.repository.SignatureRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SignatureSeeder {

    private final SignatureRepository signatureRepository;

    public SignatureSeeder(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    public void seedSignatures() {
        if (signatureRepository.count() == 0) {
            List<Signature> signatures = Arrays.asList(
                    createSignature("Programacion Web"),
                    createSignature("Lengua y Literatura"),
                    createSignature("Historia")
            );

            signatureRepository.saveAll(signatures);
            System.out.println("Seeder ejecutado: " + signatures.size() + " asignaturas creadas.");
        }
    }

    private Signature createSignature(String name) {
        Signature signature = new Signature();
        signature.setName(name);
        return signature;
    }
}