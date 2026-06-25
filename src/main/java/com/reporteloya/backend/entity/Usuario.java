package com.reporteloya.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.reporteloya.backend.security.EncryptedStringConverter;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_usuario", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "tipo_documento")
    @Size(max = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento")
    @Convert(converter = EncryptedStringConverter.class)
    @Size(max = 255)
    private String numeroDocumento;

    @Column(name = "nombre_completo", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String nombreCompleto;

    @Column(name = "correo", unique = true, nullable = false)
    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "email_verificado", nullable = false)
    private boolean emailVerificado = false;

    // ===== UserDetails =====
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (role == Role.ADMIN || role == Role.AGENTE) {
            return true;
        }
        return emailVerificado;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
