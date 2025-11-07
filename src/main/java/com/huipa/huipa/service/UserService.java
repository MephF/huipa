package com.huipa.huipa.service;

import com.huipa.huipa.dtos.UserLoginDto; // Import UserLoginDto
import com.huipa.huipa.dtos.UserRegistrationDto;
import com.huipa.huipa.entity.User;

public interface UserService {
    User registerUser(UserRegistrationDto registrationDto);
    User loginUser(UserLoginDto loginDto); // Added loginUser method
}
