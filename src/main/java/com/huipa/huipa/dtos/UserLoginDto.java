package com.huipa.huipa.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size; // Added for phone number validation
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Size(max = 20, message = "El número de teléfono no puede exceder los 20 caracteres") // Assuming max size from User entity
    private String telefono;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}
