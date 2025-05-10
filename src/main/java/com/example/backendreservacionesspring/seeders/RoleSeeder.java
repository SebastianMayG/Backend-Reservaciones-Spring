
package com.example.backendreservacionesspring.seeders;

import com.example.backendreservacionesspring.model.Role;
import com.example.backendreservacionesspring.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void seedRoles() {
        if (roleRepository.count() == 0) {
            List<Role> roles = Arrays.asList(
                    createRole("admin"),
                    createRole("user")
            );

            roleRepository.saveAll(roles);
            System.out.println("✅ Roles insertados: " + roles.size());
        }
    }

    private Role createRole(String description) {
        Role role = new Role();
        role.setDescription(description);
        return role;
    }
}