package com.reporteloya.backend.dto;

import com.reporteloya.backend.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String nombreCompleto;
    private String email;
    private String tipoDocumento;
    private String numeroDocumento;
    private Role role;
    private boolean emailVerificado;
}
