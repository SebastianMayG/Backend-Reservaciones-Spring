package service;

import jakarta.persistence.EntityNotFoundException;
import model.User;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User userUpdated){
        return  userRepository.findById(id).map(user -> {
            user.setName(userUpdated.getName());
            user.setLastName(userUpdated.getLastName());
            user.setEmail(userUpdated.getEmail());

            return userRepository.save(user);
        }).orElseThrow(()-> new RuntimeException("User no encontrada"));
    }

    public User updateParcial(Long id, Map<String, Object> campos) {
        return userRepository.findById(id).map(user -> {
            if(campos.containsKey("name")){
                user.setName((String) campos.get("name"));
            }

            if(campos.containsKey("last_name")){
                user.setLastName((String) campos.get("last_name"));
            }

            if(campos.containsKey("email")){
                user.setEmail((String) campos.get("email"));
            }

            if(campos.containsKey("password")) {
                user.setPassword(passwordEncoder.encode((String) campos.get("password")));
            }
            return userRepository.save(user);
        }).orElseThrow(()-> new RuntimeException("User no encontrada"));
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }
}
