package com.example.backendreservacionesspring.service;

import jakarta.persistence.EntityNotFoundException;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public class SpaceService {
    private SpaceRepository spaceRepository;

    public SpaceService(SpaceRepository spaceRepository) {
        this.spaceRepository = spaceRepository;
    }

    public List<Space> findAll(){
        return spaceRepository.findAll();
    }

    public Optional<Space> findById(Long id){
        return spaceRepository.findById(id);
    }

    public Space saveSpace (Space space){
        return spaceRepository.save(space);
    }

    public Space update (Long id, Space spaceUpdated){
        return spaceRepository.findById(id).map(space->{
            space.setName(spaceUpdated.getName());
            space.setDescription(spaceUpdated.getDescription());
            space.setCapacity(spaceUpdated.getCapacity());

            return spaceRepository.save(space);
        }).orElseThrow(()-> new RuntimeException("Space no encontrado"));
    }

    public Space updateParcial(Long id, Map<String, Object> campos){
        return spaceRepository.findById(id).map(space -> {
            if(campos.containsKey("name")){
                space.setName((String) campos.get("name"));
            }
            if(campos.containsKey("description")){
                space.setDescription((String) campos.get("description"));
            }
            if(campos.containsKey("capacity")){
                space.setCapacity((Integer) campos.get("capacity"));
            }
            return spaceRepository.save(space);
        }).orElseThrow(()-> new RuntimeException("Space no encontrado"));
    }

    public void deleteSpace(Long id){
        if(!spaceRepository.existsById(id)) {
            throw new EntityNotFoundException("Space no encontrado con id: " + id);
        }
        spaceRepository.deleteById(id);
    }
}
