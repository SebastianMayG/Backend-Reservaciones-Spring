package com.example.backendreservacionesspring.Seeders;

import com.example.backendreservacionesspring.model.Schedule;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.ScheduleRepository;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class ScheduleSeeder {

    private final ScheduleRepository scheduleRepository;
    private final SpaceRepository spaceRepository;

    public ScheduleSeeder(ScheduleRepository scheduleRepository, SpaceRepository spaceRepository) {
        this.scheduleRepository = scheduleRepository;
        this.spaceRepository = spaceRepository;
    }

    public void seedSchedules() {
        if (scheduleRepository.count() == 0) {
            // Verifica que los espacios existan
            List<Space> spaces = spaceRepository.findAll();
            if (spaces.isEmpty()) {
                throw new IllegalStateException("No hay espacios disponibles. Ejecuta SpaceSeeder primero.");
            }

            List<Schedule> schedules = Arrays.asList(
                    createSchedule(1, LocalTime.of(7, 0), LocalTime.of(17, 0)),
                    createSchedule(2, LocalTime.of(7, 0), LocalTime.of(17, 0)),
                    createSchedule(3, LocalTime.of(7, 0), LocalTime.of(17, 0)),
                    createSchedule(4, LocalTime.of(7, 0), LocalTime.of(17, 0)),
                    createSchedule(5, LocalTime.of(7, 0), LocalTime.of(17, 0))
            );

            scheduleRepository.saveAll(schedules);
            System.out.println("Seeders de horarios ejecutados correctamente");
        }
    }

    private Schedule createSchedule(long spaceId, LocalTime startTime, LocalTime endTime) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado con ID: " + spaceId));

        Schedule schedule = new Schedule();
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setSpace(space);

        return schedule;
    }
}