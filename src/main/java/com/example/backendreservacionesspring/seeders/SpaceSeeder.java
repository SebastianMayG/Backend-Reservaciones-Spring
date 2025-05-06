package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpaceSeeder {

    private final SpaceRepository spaceRepository;

    public SpaceSeeder(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    public void seed() {
        if (spaceRepository.count() == 0) {
            List<Space> spaces = List.of(
                    createSpace("CIC Sala 1", "Sala de Computo de ultima tecnologia", 40),
                    createSpace("CIC Sala 2", "Sala de Computo de ultima tecnologia", 40),
                    createSpace("CIC Sala 3", "Sala de Computo de ultima tecnologia", 40),
                    createSpace("CIC Sala 4", "Sala de Computo de ultima tecnologia", 40),
                    createSpace("Sala de Actos", "Amplia sala ideal para conferencias", 120)

            );

            spaceRepository.saveAll(spaces);
            System.out.println("Seeders de espacios ejecutados correctamente");
        }
    }

    private Space createSpace(String name, String description, int capacity) {
        Space space = new Space();
        space.setName(name);
        space.setDescription(description);
        space.setCapacity(capacity);
        return space;
    }
}