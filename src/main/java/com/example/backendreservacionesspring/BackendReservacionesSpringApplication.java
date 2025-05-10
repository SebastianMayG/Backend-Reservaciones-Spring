package com.example.backendreservacionesspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableScheduling
@SpringBootApplication
public class BackendReservacionesSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendReservacionesSpringApplication.class, args);

    }
}
