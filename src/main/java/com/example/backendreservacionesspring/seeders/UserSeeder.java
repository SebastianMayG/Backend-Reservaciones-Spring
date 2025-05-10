
package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Role;
import com.example.backendreservacionesspring.model.User;
import com.example.backendreservacionesspring.repository.RoleRepository;
import com.example.backendreservacionesspring.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserSeeder {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository,
                      RoleRepository roleRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void seedUsers() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Rol admin no encontrado"));
            Role userRole = roleRepository.findById(2)
                    .orElseThrow(() -> new RuntimeException("Rol usuario no encontrado"));

            List<User> users = Arrays.asList(
                    createUser("Admin1", "Admin1", "admin1@example.com", adminRole),
                    createUser("Admin2", "Admin2", "admin2@example.com", adminRole),
                    createUser("Admin3", "Admin23", "admin3@example.com", adminRole),
                    createUser("Santiago", "May", "halo5santiago@gmail.com", userRole)
            );

            userRepository.saveAll(users);
            System.out.println("✅ Usuarios insertados: " + users.size());
        }
    }

    private User createUser(String name, String lastName, String email, Role role) {
        User user = new User();
        user.setName(name);
        user.setLast_name(lastName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("root"));
        user.setRole(role);
        return user;
    }
}