package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.dto.ApiResponse;
import com.example.backendreservacionesspring.model.Schedule;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.ScheduleWithSpaceId;
import com.example.backendreservacionesspring.service.ScheduleService;
import com.example.backendreservacionesspring.service.SpaceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SpaceService spaceService;
    public ScheduleController(ScheduleService scheduleService, SpaceService spaceService) {
        this.scheduleService = scheduleService;
        this.spaceService = spaceService;
    }

    @GetMapping
    public ApiResponse<List<ScheduleWithSpaceId>> getAllSchedules(){
        return ApiResponse.success(scheduleService.findAllWithSpaceId());
    }

    @PostMapping
    public ApiResponse<Schedule> createSchedule(@RequestBody Map<String, Object> requestData){
        long spaceId;
        spaceId = ((Number) requestData.get("space_id")).longValue();
        Space space = spaceService.findSpaceById(spaceId)
                .orElseThrow(() -> new EntityNotFoundException("Space no encontrado"));

        LocalTime startTime = LocalTime.parse(requestData.get("start_time").toString());
        LocalTime endTime = LocalTime.parse(requestData.get("end_time").toString());


        Schedule schedule = new Schedule();
        schedule.setSpace(space);
        schedule.setStart_time(startTime);
        schedule.setEnd_time(endTime);
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
    public ApiResponse<List<ScheduleWithSpaceId>> getScheduleBySpace(@PathVariable Long id_space){
        return ApiResponse.success(scheduleService.findBySpaceId(id_space));
    }

    @PutMapping("/{id}")
    public ApiResponse<Schedule> updateSchedule(@PathVariable Long id, @RequestBody @Valid Schedule schedule){
        Schedule Updatedschedule = scheduleService.update(id,schedule);
        return ApiResponse.success(Updatedschedule);
    }

    @PatchMapping("/{id}")
    public ApiResponse<Schedule> updatePartialSchedule(
            @PathVariable Long id,
            @RequestBody Map<String, Object> campos) {

        // Convertir nombres de campos y tipos si es necesario
        if (campos.containsKey("start_time")) {
            campos.put("startTime", LocalTime.parse(campos.get("start_time").toString()));
        }
        if (campos.containsKey("end_time")) {
            campos.put("endTime", LocalTime.parse(campos.get("end_time").toString()));
        }
        if (campos.containsKey("space_id")) {
            campos.put("spaceId", ((Number) campos.get("space_id")).longValue());
        }

        Schedule updatedSchedule = scheduleService.updatePartial(id, campos);
        return ApiResponse.success(updatedSchedule);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Schedule> deleteSchedule(@PathVariable Long id){
        return ApiResponse.success(scheduleService.deleteSchedule(id));
    }


}
