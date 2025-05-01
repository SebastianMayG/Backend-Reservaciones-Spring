package com.example.backendreservacionesspring.Seeders;

import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(SpaceRepository spaceRepository) {
        return args -> {
            // Verifica si ya hay datos para no duplicar
            if (spaceRepository.count() == 0) {
                Space space1 = new Space();
                space1.setName("Sala de Conferencias");
                space1.setDescription("Sala equipada con proyector");
                space1.setCapacity(20);

                Space space2 = new Space();
                space2.setName("Sala de Reuniones");
                space2.setDescription("Sala pequeña para equipos");
                space2.setCapacity(8);

                spaceRepository.saveAll(List.of(space1, space2));
                System.out.println("Seeders ejecutados correctamente");
            }
        };
    }
}
