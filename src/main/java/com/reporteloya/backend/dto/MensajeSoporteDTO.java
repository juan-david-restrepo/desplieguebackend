package com.reporteloya.backend.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensajeSoporteDTO {
    private UUID id;
    private UUID ticketId;
    private String emisorNombre;
    private String contenido;
    private Boolean esAdmin;
    private Boolean leido;
    private LocalDateTime fechaEnvio;
}
