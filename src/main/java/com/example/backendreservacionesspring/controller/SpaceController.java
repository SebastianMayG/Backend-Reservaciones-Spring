package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.service.SpaceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

    private final SpaceService spaceService;

    public SpaceController(SpaceService spaceService) {
        this.spaceService = spaceService;
    }

    @GetMapping
    public List<Space> getSpaces() {
        return spaceService.findAll();
    }

    @PostMapping
    public ResponseEntity<Space> createSpace(@RequestBody Space space) {
        Space createdSpace = spaceService.saveSpace(space);
        return new ResponseEntity<>(createdSpace, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Space> getSpaceById(@PathVariable Long id) {
        Space space = spaceService.findSpaceById(id)
                .orElseThrow(() -> new EntityNotFoundException("Space no encontrado con id: " + id));
        return new ResponseEntity<>(space, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Space> updateSpace(@PathVariable Long id, @RequestBody Space space) {
        Space updateSpace = spaceService.update(id, space);
        return new ResponseEntity<>(updateSpace, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Space> updateSpaceById(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Space updateSpace = spaceService.updateParcial(id, campos);
        return new ResponseEntity<>(updateSpace, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Space> deleteSpaceById(@PathVariable Long id) {
        Space deletedSpace = spaceService.deleteSpace(id);
        return new ResponseEntity<>(deletedSpace, HttpStatus.OK);
    }
}

