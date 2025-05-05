package com.example.backendreservacionesspring.controller;

import com.example.backendreservacionesspring.model.User;
import com.example.backendreservacionesspring.repository.UserRepository;
import com.example.backendreservacionesspring.model.Role;
import com.example.backendreservacionesspring.repository.RoleRepository;

import com.example.backendreservacionesspring.security.TokenBlackList;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private static final String SECRET_KEY = "mWQKjKflpJSqyj0nDdSG9ZHE6x4tNaXGb35J6d7G5mo=";
    private final TokenBlackList tokenBlackList;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, TokenBlackList tokenBlackList) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.tokenBlackList = tokenBlackList;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findAll().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "El correo ya esta registrado"));
        }
        Role defaultRole = roleRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol por defecto no encontrado"));

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(defaultRole);

        User savedUser = userRepository.save(user);

        String token = Jwts.builder()
                .subject(savedUser.getEmail())
                .claim("role", savedUser.getRole().getId())
                .claim("id", savedUser.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 horas
                .signWith(generalKey())
                .compact();

        return ResponseEntity.ok(Map.of(
                "data", savedUser, // devolver informacion del usuario
                "token", token,
                "code",201
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> userOpt = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();

        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("mensaje", "Credenciales inválidas"));
        }

        User user = userOpt.get();

        String token = Jwts.builder()
                .subject(user.getEmail())
                .claim("role", user.getRole().getId())
                .claim("id", user.getId())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY)))
                .compact();

        return ResponseEntity.ok(Map.of(
                "data", user, // devolver informacion del usuario
                "token", token,
                "code", 200
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            // Obtener el token del header
            String token = authHeader.startsWith("Bearer ") ?
                    authHeader.substring(7) : authHeader;

            // Agregar el token a la lista negra
            tokenBlackList.addToBlacklist(token);

            return ResponseEntity.ok(Map.of(
                    "mensaje", "Sesión cerrada exitosamente",
                    "code", 200
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "mensaje", "Error al cerrar sesión",
                    "code", 500
            ));
        }
    }


    private SecretKey generalKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}