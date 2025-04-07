package com.example.capstone_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.capstone_backend.repository.UserRepository;
import com.example.capstone_backend.DTO.UserDto;
import com.example.capstone_backend.exception.InvalidPasswordException;
import com.example.capstone_backend.exception.ResourceNotFoundException;
import com.example.capstone_backend.model.user.Login;

import static com.example.capstone_backend.application.ApplicationProperties.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/auth")
@CrossOrigin
public class AuthenticationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @PostMapping("/login")
    public long login(@RequestBody Login login) {
        UserDto userDto = userRepository.findByEmail(login.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));
        String encodedPassword = userDto.getPassword();
        boolean isPwdRight = passwordEncoder.matches(login.getPassword(), encodedPassword);

        if (!isPwdRight) {
            throw new InvalidPasswordException("Password does not match");
        }

        return userDto.getId();
    }

}