package com.reporteloya.backend.controller;

import com.reporteloya.backend.dto.UpdateProfileDTO;
import com.reporteloya.backend.dto.UserProfileDTO;
import com.reporteloya.backend.entity.Usuario;
import com.reporteloya.backend.repository.ReporteRepository;
import com.reporteloya.backend.repository.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ciudadano")
public class UserController {

    private final UsuarioRepository userRepository;
    private final ReporteRepository reporteRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UsuarioRepository userRepository, ReporteRepository reporteRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.reporteRepository = reporteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(Authentication authentication) {
        String email = authentication.getName();
        Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(toDTO(user));
    }

    @GetMapping("/reportes/total")
    public ResponseEntity<Map<String, Integer>> getTotalReportes(Authentication authentication) {
        String email = authentication.getName();
        Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int totalReportes = reporteRepository.countByUsuario_Id(user.getId());

        Map<String, Integer> response = new HashMap<>();
        response.put("total_reportes", totalReportes);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @RequestBody UpdateProfileDTO dto,
            Authentication authentication) {

        String email = authentication.getName();
        Usuario user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (dto.getNombreCompleto() != null && !dto.getNombreCompleto().isBlank()) {
            user.setNombreCompleto(dto.getNombreCompleto());
        }

        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        Usuario saved = userRepository.save(user);
        return ResponseEntity.ok(toDTO(saved));
    }

    private UserProfileDTO toDTO(Usuario u) {
        return UserProfileDTO.builder()
                .id(u.getId())
                .nombreCompleto(u.getNombreCompleto())
                .email(u.getEmail())
                .tipoDocumento(u.getTipoDocumento())
                .numeroDocumento(u.getNumeroDocumento())
                .role(u.getRole())
                .emailVerificado(u.isEmailVerificado())
                .build();
    }
}
