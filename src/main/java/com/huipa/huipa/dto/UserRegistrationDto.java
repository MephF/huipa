package com.huipa.huipa.dto;

import com.huipa.huipa.enums.UserRole;
import com.huipa.huipa.validation.ArtesanoRegistration;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Size(max = 255, message = "El email no puede exceder los 255 caracteres")
    private String email;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 150, message = "El nombre no puede exceder los 150 caracteres")
    private String nombre;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @NotNull(message = "El rol de usuario no puede ser nulo")
    private UserRole role;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(max = 20, message = "El teléfono no puede exceder los 20 caracteres")
    private String telefono;

    private String fotoPerfilUrl; // Opcional para todos

    // Campos específicos para Artesano
    @NotBlank(message = "La historia de vida no puede estar vacía para un artesano", groups = ArtesanoRegistration.class)
    private String historiaVida;

    @NotBlank(message = "El nombre del negocio no puede estar vacío para un artesano", groups = ArtesanoRegistration.class)
    @Size(max = 255, message = "El nombre del negocio no puede exceder los 255 caracteres", groups = ArtesanoRegistration.class)
    private String nombreNegocio;

    @NotBlank(message = "La descripción del taller no puede estar vacía para un artesano", groups = ArtesanoRegistration.class)
    private String descripcionTaller;

    @NotBlank(message = "El WhatsApp del negocio no puede estar vacío para un artesano", groups = ArtesanoRegistration.class)
    @Size(max = 20, message = "El WhatsApp del negocio no puede exceder los 20 caracteres", groups = ArtesanoRegistration.class)
    private String whatsappNegocio;

    private String emailPublico;
    private String metodosPagoInfo;
    private String instagramUrl;
    private String facebookUrl;
}
