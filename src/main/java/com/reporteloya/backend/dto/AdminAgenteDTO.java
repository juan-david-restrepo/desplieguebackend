package com.reporteloya.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAgenteDTO {

    private UUID id;
    private String placa;
    private String nombre;
    private String estado;
    private String telefono;
    private String documento;
    private String foto;
    private Double promedioResenas;
}

