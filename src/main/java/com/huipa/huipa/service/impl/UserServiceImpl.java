package com.huipa.huipa.service.impl;

import com.huipa.huipa.dtos.UserLoginDto;
import com.huipa.huipa.dtos.UserRegistrationDto;
import com.huipa.huipa.enums.UserRole;
import com.huipa.huipa.entity.ArtesanoDetails;
import com.huipa.huipa.entity.Negocio;
import com.huipa.huipa.entity.User;
import com.huipa.huipa.repository.ArtesanoDetailsRepository;
import com.huipa.huipa.repository.NegocioRepository;
import com.huipa.huipa.repository.UserRepository;
import com.huipa.huipa.service.UserService;
import com.huipa.huipa.validation.ArtesanoRegistration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ArtesanoDetailsRepository artesanoDetailsRepository;
    private final NegocioRepository negocioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Validator validator;

    public UserServiceImpl(UserRepository userRepository,
                           ArtesanoDetailsRepository artesanoDetailsRepository,
                           NegocioRepository negocioRepository,
                           BCryptPasswordEncoder passwordEncoder,
                           Validator validator) {
        this.userRepository = userRepository;
        this.artesanoDetailsRepository = artesanoDetailsRepository;
        this.negocioRepository = negocioRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado.");
        }
        // Also check if phone number is already registered
        if (userRepository.findByTelefono(registrationDto.getTelefono()).isPresent()) {
            throw new IllegalArgumentException("El número de teléfono ya está registrado.");
        }

        User newUser = new User();
        newUser.setEmail(registrationDto.getEmail());
        newUser.setNombre(registrationDto.getNombre());
        newUser.setTelefono(registrationDto.getTelefono());
        newUser.setPasswordHash(passwordEncoder.encode(registrationDto.getPassword()));
        newUser.setRole(registrationDto.getRole());
        newUser.setFotoPerfilUrl(registrationDto.getFotoPerfilUrl());

        if (newUser.getRole() == UserRole.ARTESANO) {
            Set<ConstraintViolation<UserRegistrationDto>> violations = validator.validate(registrationDto, ArtesanoRegistration.class);
            if (!violations.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder();
                for (ConstraintViolation<UserRegistrationDto> violation : violations) {
                    errorMessage.append(violation.getMessage()).append(". ");
                }
                throw new IllegalArgumentException("Error de validación para Artesano: " + errorMessage.toString());
            }
        }

        User savedUser = userRepository.save(newUser);

        if (savedUser.getRole() == UserRole.ARTESANO) {
            ArtesanoDetails artesanoDetails = new ArtesanoDetails();
            artesanoDetails.setUser(savedUser);
            artesanoDetails.setHistoriaVida(registrationDto.getHistoriaVida());
            artesanoDetailsRepository.save(artesanoDetails);

            Negocio negocio = new Negocio();
            negocio.setArtesanoUser(savedUser);
            negocio.setNombreNegocio(registrationDto.getNombreNegocio());
            negocio.setDescripcionTaller(registrationDto.getDescripcionTaller());
            negocio.setWhatsapp(registrationDto.getWhatsappNegocio());
            negocio.setEmailPublico(registrationDto.getEmailPublico());
            negocio.setMetodosPagoInfo(registrationDto.getMetodosPagoInfo());
            negocio.setInstagramUrl(registrationDto.getInstagramUrl());
            negocio.setFacebookUrl(registrationDto.getFacebookUrl());
            negocioRepository.save(negocio);
        }

        return savedUser;
    }

    @Override
    public User loginUser(UserLoginDto loginDto) {
        User user = userRepository.findByTelefono(loginDto.getTelefono()) // Changed from findByEmail to findByTelefono
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }
        return user;
    }
}
