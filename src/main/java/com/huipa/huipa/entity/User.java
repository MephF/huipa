package com.huipa.huipa.entity;

import com.huipa.huipa.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate; // Import for LocalDate
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @NotNull(message = "El teléfono no puede estar vacío")
    @Size(max = 20)
    @Column(name = "telefono", length = 20)
    private String telefono;

    @NotNull
    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotNull
    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @NotNull
    @Size(max = 150)
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "foto_perfil_url", columnDefinition = "TEXT")
    private String fotoPerfilUrl;

    @NotNull
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @NotNull(message = "La fecha de nacimiento no puede estar vacía")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull(message = "El lugar de residencia no puede estar vacío")
    @Size(max = 255)
    @Column(name = "lugar_residencia", nullable = false, length = 255)
    private String lugarResidencia;

    @NotNull(message = "La dirección no puede estar vacía")
    @Column(name = "direccion", nullable = false, columnDefinition = "TEXT")
    private String direccion;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
