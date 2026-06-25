package com.reporteloya.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {
    private UUID id;
    private String tipo;
    private String titulo;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime fechaCreacion;
    private UUID idReferencia;
    private String datosAdicionales;
}
