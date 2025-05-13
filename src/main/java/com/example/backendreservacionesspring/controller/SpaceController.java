package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.dto.ApiResponse;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.ScheduleRepository;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import com.example.backendreservacionesspring.service.EmailService;
import com.example.backendreservacionesspring.service.SpaceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/spaces")
public class SpaceController {

    private final SpaceService spaceService;
    private final EmailService emailService; // Inyecta EmailService
    private final ScheduleRepository scheduleRepository;
    private final SpaceRepository spaceRepository;
    public SpaceController(SpaceService spaceService, EmailService emailService, ScheduleRepository scheduleRepository, SpaceRepository spaceRepository) {
        this.spaceService = spaceService;
        this.emailService = emailService;
        this.scheduleRepository = scheduleRepository;
        this.spaceRepository = spaceRepository;
    }
    @GetMapping
    public ApiResponse<List<Space>> getSpaces() {
        List<Space> spaces = spaceService.findAll();
        //emailService.sendVerificationEmail("al064488@uacam.mx");
        return ApiResponse.success(spaces);
    }

    @PostMapping
    public ApiResponse<Space> createSpace(@RequestBody @Valid Space space) {
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
    public ApiResponse<Space> updatePartialSpace(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Space updatedSpace = spaceService.updateParcial(id, campos);
        return ApiResponse.success(updatedSpace);
    }
    @Transactional
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSpaceById(@PathVariable Long id) {
        scheduleRepository.deleteBySpaceId(id);
        spaceService.deleteSpace(id);
        return ApiResponse.success(null);
    }

    @GetMapping("/diagnostic/{id}")
    public ApiResponse<String> diagnostic(@PathVariable Long id) {
        boolean exists = spaceRepository.existsById(id);
        return ApiResponse.success("Exists in DB: " + exists);
    }
}