package com.example.backendreservacionesspring.Seeders;

import com.example.backendreservacionesspring.model.Space;
import com.example.backendreservacionesspring.repository.SpaceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(SpaceSeeder spaceSeeder, ScheduleSeeder scheduleSeeder, StudentSeeder studentSeeder) {
        return args -> {
            studentSeeder.seedStudent();
            spaceSeeder.seed();
            scheduleSeeder.seedSchedules();
        };
    }
}
