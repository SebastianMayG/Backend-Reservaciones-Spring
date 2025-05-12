package com.example.backendreservacionesspring.service;

import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SpaceService {
    private final SpaceRepository spaceRepository;

    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    @Transactional(readOnly = true)
    public List<Space> findAll() {
        return spaceRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Space> findSpaceById(Long id) {
        // Limpiar el caché de Hibernate antes de buscar
        spaceRepository.flush();
        return spaceRepository.findById(id);
    }

    @Transactional
    public Space saveSpace(Space space) {
        return spaceRepository.save(space);
    }

    @Transactional
    public Space update(Long id, Space spaceUpdated) {
        return spaceRepository.findById(id).map(space -> {
            space.setName(spaceUpdated.getName());
            space.setDescription(spaceUpdated.getDescription());
            space.setCapacity(spaceUpdated.getCapacity());
            return spaceRepository.save(space);
        }).orElseThrow(() -> new EntityNotFoundException("Space no encontrado con id: " + id));
    }

    @Transactional
    public Space updateParcial(Long id, Map<String, Object> campos) {
        return spaceRepository.findById(id).map(space -> {
            campos.forEach((key, value) -> {
                switch (key) {
                    case "name":
                        space.setName((String) value);
                        break;
                    case "description":
                        space.setDescription((String) value);
                        break;
                    case "capacity":
                        space.setCapacity((Integer) value);
                        break;
                }
            });
            return spaceRepository.save(space);
        }).orElseThrow(() -> new EntityNotFoundException("Space no encontrado con id: " + id));
    }

    @Transactional
    public void deleteSpace(Long id) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Space no encontrado con id: " + id));
        spaceRepository.delete(space);
        // Forzar sincronización con la base de datos
        spaceRepository.flush();
    }
}