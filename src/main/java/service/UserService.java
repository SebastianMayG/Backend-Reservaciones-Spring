package service;

import model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserService userService;

    public UserService(UserService userService) {
        this.userService = userService;
    }

    public List<User> findAll() {
        return userService.findAll();
    }
}
