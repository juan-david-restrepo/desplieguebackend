package com.reporteloya.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_verification_token")
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, unique = true, length = 100)
    private String token;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "id_usuario", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID idUsuario;

    @Column(nullable = false)
    private boolean used = false;

    public EmailVerificationToken() {
    }

    public EmailVerificationToken(String email, String token, LocalDateTime expirationDate, UUID idUsuario) {
        this.email = email;
        this.token = token;
        this.expirationDate = expirationDate;
        this.idUsuario = idUsuario;
        this.used = false;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getToken() { return token; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public boolean isUsed() { return used; }
    public UUID getIdUsuario() { return idUsuario; }

    public void setEmail(String email) { this.email = email; }
    public void setToken(String token) { this.token = token; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
    public void setUsed(boolean used) { this.used = used; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expirationDate);
    }
}
