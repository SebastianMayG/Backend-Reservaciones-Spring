package com.example.backendreservacionesspring.Seeders;

import com.example.backendreservacionesspring.model.Role;
import com.example.backendreservacionesspring.model.User;
import com.example.backendreservacionesspring.repository.RoleRepository;
import com.example.backendreservacionesspring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class UserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Rol admin no encontrado"));

            User admin = new User();
            admin.setName("Admin1");
            admin.setLastName("Admin1");
            admin.setEmail("admin1@example.com");
            admin.setPassword(passwordEncoder.encode("root"));
            admin.setRole(adminRole);

            userRepository.save(admin);

            System.out.println("✅ Usuario admin1@example.com creado correctamente");
        } else {
            System.out.println("⚠️ Ya existen usuarios, no se crea admin por defecto");
        }
    }
}
