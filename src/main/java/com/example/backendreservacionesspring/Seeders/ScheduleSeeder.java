package com.example.backendreservacionesspring.Seeders;

import com.example.backendreservacionesspring.model.Schedule;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.ScheduleRepository;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
            List<Space> spaces = spaceRepository.findAll();
            if (spaces.isEmpty()) {
                throw new IllegalStateException("No hay espacios disponibles. Ejecuta SpaceSeeder primero.");
            }

            // Crea horarios para cada espacio
            List<Schedule> schedules = spaces.stream()
                    .map(space -> createSchedule(space, LocalTime.of(7, 0), LocalTime.of(17, 0)))
                    .collect(Collectors.toList());

            scheduleRepository.saveAll(schedules);
            System.out.println("Seeders de horarios ejecutados correctamente");
        }
    }

    private Schedule createSchedule(Space space, LocalTime startTime, LocalTime endTime) {
        Schedule schedule = new Schedule();
        schedule.setStart_time(startTime);
        schedule.setEnd_time(endTime);
        schedule.setSpace(space);  // Asigna el espacio directamente
        return schedule;
    }
}