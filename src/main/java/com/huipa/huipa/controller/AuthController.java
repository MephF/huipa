package com.huipa.huipa.controller;

import com.huipa.huipa.dtos.UserLoginDto; // Import UserLoginDto
import com.huipa.huipa.dtos.UserRegistrationDto;
import com.huipa.huipa.entity.User;
import com.huipa.huipa.service.UserService;
import jakarta.validation.Valid; // Keep Valid for DTOs if needed elsewhere, or remove if not used
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            User registeredUser = userService.registerUser(registrationDto);
            // In a real application, you might return a DTO that doesn't expose the password hash
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al registrar el usuario: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto loginDto) { // Added @Valid for loginDto
        try {
            User loggedInUser = userService.loginUser(loginDto);
            // In a real application, you might return a DTO that doesn't expose the password hash
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED); // Use UNAUTHORIZED for login failures
        } catch (Exception e) {
            return new ResponseEntity<>("Error al iniciar sesión: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
