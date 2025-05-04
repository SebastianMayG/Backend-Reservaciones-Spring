package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.dto.ApiResponse;
import com.example.backendreservacionesspring.model.Schedule;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.service.ScheduleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ApiResponse<List<Schedule>> getAllSchedules(){
        return ApiResponse.success(scheduleService.findAll());
    }

    @PostMapping
    public ApiResponse<Schedule> createSchedule(@RequestBody @Valid Schedule schedule){
        return ApiResponse.success(scheduleService.save(schedule));
    }
    //get un horario por su id
    @GetMapping("/{id}")
    public ApiResponse<Schedule> getScheduleById(@PathVariable Long id){
        Schedule schedule = scheduleService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule no encontrado con id: " + id));
        return ApiResponse.success(schedule);
    }
    @GetMapping("/space/{id_space}")
    public ApiResponse<List<Schedule>> getScheduleBySpace(@PathVariable Long id_space){
        return ApiResponse.success(scheduleService.findBySpaceId(id_space));
    }

    @PutMapping("/{id}")
    public ApiResponse<Schedule> updateSchedule(@PathVariable Long id, @RequestBody @Valid Schedule schedule){
        Schedule Updatedschedule = scheduleService.update(id,schedule);
        return ApiResponse.success(Updatedschedule);
    }

    @PatchMapping("/{id}")
    public ApiResponse<Schedule> updatePartialSpace(@PathVariable Long id, @RequestBody Map<String, Object> campos) {
        Schedule updatedSchedule = scheduleService.updatePartial(id, campos);
        return ApiResponse.success(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Schedule> deleteSchedule(@PathVariable Long id){
        return ApiResponse.success(scheduleService.deleteSchedule(id));
    }


}
