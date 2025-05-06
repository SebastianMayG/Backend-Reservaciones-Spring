package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Practice;
import com.example.backendreservacionesspring.model.Signature;
import com.example.backendreservacionesspring.service.PracticeService;
import com.example.backendreservacionesspring.service.SignatureService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PracticeSeeder {

    private final PracticeService practiceService;
    private final SignatureService signatureService;

    public PracticeSeeder(PracticeService practiceService, SignatureService signatureService) {
        this.practiceService = practiceService;
        this.signatureService = signatureService;
    }

    public void seedPractices() {
        // Verificar si ya existen prácticas (asumiendo que tienes este método)
        if (practiceService.findAll().isEmpty()) {
            // Obtener todas las asignaturas existentes
            List<Signature> signatures = signatureService.findAll();

            if (!signatures.isEmpty()) {
                // Crear prácticas para la primera asignatura
                Signature math = signatures.stream()
                        .filter(s -> s.getName().equalsIgnoreCase("Matemáticas"))
                        .findFirst()
                        .orElse(signatures.get(0));

                createPractice("Aplicacion Java Web con Spring", "https://drive.google.com/file/d/1VjEvzCrGE-WUCJ6uqxItjJTGhgFrWsSs/view?usp=sharing", math);
                createPractice("Aplicación Java Web usando estructura básica", "https://drive.google.com/file/d/1VjEvzCrGE-WUCJ6uqxItjJTGhgFrWsSs/view?usp=sharing", math);

                // Crear prácticas para la segunda asignatura (si existe)
                if (signatures.size() > 1) {
                    Signature language = signatures.stream()
                            .filter(s -> s.getName().equalsIgnoreCase("Lengua y Literatura"))
                            .findFirst()
                            .orElse(signatures.get(1));

                    createPractice("Análisis de Texto", "https://lengua.com/analisis", language);
                    createPractice("Gramática Avanzada", "https://lengua.com/gramatica", language);
                }

                System.out.println("Seeder ejecutado: " + practiceService.findAll().size() + " prácticas creadas.");
            }
        }
    }

    public Practice createPractice(String name, String url, Signature signature) {
        Practice practice = new Practice();
        practice.setName(name);
        practice.setUrl(url);
        practice.setSignature(signature);

        return practiceService.save(practice);
    }
}