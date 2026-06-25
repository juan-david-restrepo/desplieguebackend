package com.reporteloya.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Audit trail for sensitive operations.
 * OWASP A09 - Security Logging and Monitoring.
 */
@Entity
@Table(name = "audit_log", indexes = {
    @Index(name = "idx_audit_usuario", columnList = "usuario_id"),
    @Index(name = "idx_audit_fecha", columnList = "fecha"),
    @Index(name = "idx_audit_accion", columnList = "accion")
})
@Getter
@Setter
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "usuario_id", columnDefinition = "VARCHAR(36)")
    private UUID usuarioId;

    @Column(name = "email_usuario", length = 255)
    private String emailUsuario;

    @Column(name = "accion", nullable = false, length = 100)
    private String accion;

    @Column(name = "entidad", length = 100)
    private String entidad;

    @Column(name = "entidad_id", columnDefinition = "VARCHAR(36)")
    private UUID entidadId;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "ip_origen", length = 50)
    private String ipOrigen;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "exitoso", nullable = false)
    private boolean exitoso = true;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

    public static AuditLog of(String accion, String entidad, UUID entidadId,
                               UUID usuarioId, String emailUsuario, String ip) {
        AuditLog log = new AuditLog();
        log.accion = accion;
        log.entidad = entidad;
        log.entidadId = entidadId;
        log.usuarioId = usuarioId;
        log.emailUsuario = emailUsuario;
        log.ipOrigen = ip;
        log.exitoso = true;
        return log;
    }
}
