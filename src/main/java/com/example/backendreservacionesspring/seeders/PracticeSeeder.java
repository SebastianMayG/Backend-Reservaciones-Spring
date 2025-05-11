package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Practice;
import com.example.backendreservacionesspring.model.Signature;
import com.example.backendreservacionesspring.repository.PracticeRepository;
import com.example.backendreservacionesspring.service.SignatureService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PracticeSeeder {

    private final PracticeRepository practiceRepository;
    private final SignatureService signatureService;

    public PracticeSeeder(PracticeRepository practiceRepository, SignatureService signatureService) {
        this.practiceRepository = practiceRepository;
        this.signatureService = signatureService;
    }

    public void seed() {
        if (practiceRepository.count() == 0) {
            List<Signature> signatures = signatureService.findAll();
            List<Practice> practices = new ArrayList<>();

            if (!signatures.isEmpty()) {
                // Crear prácticas para la primera asignatura
                Signature math = signatures.stream()
                        .filter(s -> s.getName().equalsIgnoreCase("Matemáticas"))
                        .findFirst()
                        .orElse(signatures.get(0));

                practices.add(createPractice("Aplicacion Java Web con Spring", 
                    "https://drive.google.com/file/d/1VjEvzCrGE-WUCJ6uqxItjJTGhgFrWsSs/view?usp=sharing", 
                    math));
                practices.add(createPractice("Aplicación Java Web usando estructura básica", 
                    "https://drive.google.com/file/d/1VjEvzCrGE-WUCJ6uqxItjJTGhgFrWsSs/view?usp=sharing", 
                    math));

                // Crear prácticas para la segunda asignatura (si existe)
                if (signatures.size() > 1) {
                    Signature language = signatures.stream()
                            .filter(s -> s.getName().equalsIgnoreCase("Lengua y Literatura"))
                            .findFirst()
                            .orElse(signatures.get(1));

                    practices.add(createPractice("Análisis de Texto", 
                        "https://lengua.com/analisis", 
                        language));
                    practices.add(createPractice("Gramática Avanzada", 
                        "https://lengua.com/gramatica", 
                        language));
                }

                practiceRepository.saveAll(practices);
                System.out.println("✅ Prácticas insertadas: " + practices.size());
            }
        }
    }

    private Practice createPractice(String name, String url, Signature signature) {
        Practice practice = new Practice();
        practice.setName(name);
        practice.setUrl(url);
        practice.setSignature(signature);
        return practice;
    }
}