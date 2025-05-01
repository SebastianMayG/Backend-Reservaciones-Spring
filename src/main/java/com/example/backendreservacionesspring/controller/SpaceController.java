package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.dto.ApiResponse;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.service.SpaceService;
import jakarta.persistence.EntityNotFoundException;
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
    public ApiResponse<List<Space>> getSpaces() {
        List<Space> spaces = spaceService.findAll();
        return ApiResponse.success(spaces);
    }

    @PostMapping
    public ApiResponse<Space> createSpace(@RequestBody Space space) {
        Space createdSpace = spaceService.saveSpace(space);
        return ApiResponse.created(createdSpace);
    }

    @GetMapping("/{id}")
    public ApiResponse<Space> getSpaceById(@PathVariable Long id) {
        Space space = spaceService.findSpaceById(id)
                .orElseThrow(() -> new EntityNotFoundException("Space no encontrado con id: " + id));
        return ApiResponse.success(space);
    }

    @PutMapping("/{id}")
    public ApiResponse<Space> updateSpace(@PathVariable Long id, @RequestBody Space space) {
        Space updatedSpace = spaceService.update(id, space);
        return ApiResponse.success(updatedSpace);
    }

    @PatchMapping("/{id}")
    public ApiResponse<Space> updateSpaceById(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Space updatedSpace = spaceService.updateParcial(id, campos);
        return ApiResponse.success(updatedSpace);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSpaceById(@PathVariable Long id) {
        spaceService.deleteSpace(id);
        return ApiResponse.success(null);
    }
}