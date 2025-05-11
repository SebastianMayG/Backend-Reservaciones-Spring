package com.example.backendreservacionesspring.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(SpaceSeeder spaceSeeder,
                                   ScheduleSeeder scheduleSeeder,
                                   StudentSeeder studentSeeder,
                                   SignatureSeeder signatureSeeder,
                                   PracticeSeeder practiceSeeder,
                                   UserSeeder userSeeder,
                                   RoleSeeder roleSeeder) {
        return args -> {
            signatureSeeder.seedSignatures();
            studentSeeder.seedStudent();
            roleSeeder.seedRoles();
            userSeeder.seedUsers();
            spaceSeeder.seed();
            scheduleSeeder.seedSchedules();
            practiceSeeder.seed();
        };
    }
}