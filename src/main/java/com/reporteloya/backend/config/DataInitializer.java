package com.reporteloya.backend.config;

import com.reporteloya.backend.entity.Role;
import com.reporteloya.backend.entity.Usuario;
import com.reporteloya.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email:admin@reporteloya.gov.co}")
    private String adminEmail;

    @Value("${admin.password:#{null}}")
    private String adminPassword;

    @Value("${admin.nombre:Administrador Principal}")
    private String adminNombre;

    @Override
    public void run(String... args) {
        if (adminPassword == null || adminPassword.isBlank()) {
            log.warn("Variable ADMIN_PASSWORD no configurada — no se creará el usuario admin por defecto.");
            return;
        }

        if (usuarioRepository.existsByEmail(adminEmail)) {
            log.info("Usuario admin '{}' ya existe, no se crea de nuevo.", adminEmail);
            return;
        }

        Usuario admin = Usuario.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .nombreCompleto(adminNombre)
                .tipoDocumento("CC")
                .numeroDocumento("0000000000")
                .role(Role.ADMIN)
                .emailVerificado(true)
                .build();

        usuarioRepository.save(admin);
        log.info("✅ Usuario admin creado: {}", adminEmail);
    }
}
