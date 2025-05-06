package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Schedule;
import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.ScheduleRepository;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
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

            // Versión con LocalTime
            List<Schedule> schedules = spaces.stream()
                    .map(space -> createSchedule(
                            space,
                            LocalTime.of(7, 0),  // Hora de inicio
                            LocalTime.of(17, 0)   // Hora de fin
                    ))
                    .collect(Collectors.toList());

            scheduleRepository.saveAll(schedules);
            System.out.println("Seeders de horarios ejecutados correctamente");
        }
    }

    private Schedule createSchedule(Space space, LocalTime startTime, LocalTime endTime) {
        Schedule schedule = new Schedule();
        schedule.setStart_time(startTime.withSecond(0)); // Fuerza segundos a 0
        schedule.setEnd_time(endTime.withSecond(0));     // Fuerza segundos a 0
        schedule.setSpace(space);
        return schedule;
    }
}