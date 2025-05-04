//package com.example.backendreservacionesspring.security;
//
//import com.example.backendreservacionesspring.model.User;
//import com.example.backendreservacionesspring.repository.UserRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepo;
//
//    public CustomUserDetailsService(UserRepository userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new UserDetailsImpl(user);
//    }
//}
