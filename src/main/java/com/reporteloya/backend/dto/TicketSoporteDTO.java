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
public class TicketSoporteDTO {
    private UUID id;
    private String titulo;
    private String descripcion;
    private String prioridad;
    private String estado;
    private String nombreUsuario;
    private UUID usuarioId;
    private int cantidadMensajes;
    private String ultimoMensaje;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
