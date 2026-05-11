package com.reporteloya.backend.dto;

import lombok.Data;

@Data
public class CrearAgenteRequest {

    // Campos de Usuario (tabla usuarios)
    private String correo;
    private String password;
    private String nombreCompleto;
    private String tipoDocumento;
    private String numeroDocumento;

    // Campos de Agente (tabla agentes)
    private String placa;
    private String telefono;
    private String documento;
    private String nombre;
    private String foto;
    private String estado;
    private String resumenProfesional1;
    private String resumenProfesional2;
    private String resumenProfesional3;
    private String resumenProfesional4;
}
