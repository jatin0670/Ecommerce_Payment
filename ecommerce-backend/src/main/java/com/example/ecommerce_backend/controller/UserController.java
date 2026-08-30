package com.example.ecommerce_backend.controller;

import com.example.ecommerce_backend.entity.User;
import com.example.ecommerce_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PostMapping
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }
}
