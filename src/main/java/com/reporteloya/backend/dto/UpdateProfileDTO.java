package com.reporteloya.backend.dto;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private String nombreCompleto;
    private String newPassword;
}
