package com.huipa.huipa.service.impl;

import com.huipa.huipa.dtos.UserRegistrationDto;
import com.huipa.huipa.entity.User;
import com.huipa.huipa.repository.UserRepository;
import com.huipa.huipa.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }

        User newUser = new User();
        newUser.setEmail(registrationDto.getEmail());
        newUser.setNombre(registrationDto.getNombre());
        newUser.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setRole(registrationDto.getRole());

        return userRepository.save(newUser);
    }
}
