package com.reporteloya.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificaciones")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agente_id", nullable = true)
    private Agentes agente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, length = 255)
    private String titulo;

    @Column(length = 500)
    private String mensaje;

    @Column(nullable = false)
    private Boolean leida = false;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "id_referencia", columnDefinition = "VARCHAR(36)")
    private UUID idReferencia;

    @Column(name = "datos_adicionales", length = 2000)
    private String datosAdicionales;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (leida == null) {
            leida = false;
        }
    }
}
