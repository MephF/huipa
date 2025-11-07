package com.huipa.huipa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "negocios")
@Data
public class Negocio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "negocio_id")
    private UUID negocioId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artesano_user_id", nullable = false)
    private User artesanoUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logistica_user_id")
    private User logisticaUser;

    @NotNull
    @Size(max = 255)
    @Column(name = "nombre_negocio", nullable = false, length = 255)
    private String nombreNegocio;

    @Column(name = "descripcion_taller", columnDefinition = "TEXT")
    private String descripcionTaller;

    @Column(name = "metodos_pago_info", columnDefinition = "TEXT")
    private String metodosPagoInfo;

    @Size(max = 20)
    @Column(name = "whatsapp", length = 20)
    private String whatsapp;

    @Size(max = 255)
    @Column(name = "email_publico", length = 255)
    private String emailPublico;

    @Size(max = 255)
    @Column(name = "instagram_url", length = 255)
    private String instagramUrl;

    @Size(max = 255)
    @Column(name = "facebook_url", length = 255)
    private String facebookUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
