package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Role;
import com.example.backendreservacionesspring.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleSeeder {

    private final RoleRepository roleRepo;

    @PostConstruct
    public void init() {
        if (roleRepo.count() == 0) {
            Role adminRole = new Role();
            adminRole.setDescription("admin");
            roleRepo.save(adminRole);

            Role userRole = new Role();
            userRole.setDescription("user");
            roleRepo.save(userRole);
        }
    }
}