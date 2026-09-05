package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.entity.User;
import com.example.ecommerce_backend.repository.UserRepository;
import com.example.ecommerce_backend.security.JwtFilter;
import com.example.ecommerce_backend.security.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @PostMapping("/register")
    public String register(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "Register Successfully";
    }


    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body){
        User user = userRepository.findByEmail(body.get("email")).orElseThrow(() -> new RuntimeException("Invalid Credentials"));

        if(!passwordEncoder.matches(body.get("password"), user.getPassword())){
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return Map.of("token", token);
    }


}
