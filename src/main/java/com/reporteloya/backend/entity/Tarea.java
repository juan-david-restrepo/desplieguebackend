package com.reporteloya.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tareas")
@Data
@ToString(exclude = "agente")
@EqualsAndHashCode(exclude = "agente")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;
    private String titulo;
    private String descripcion;
    @Column(name = "resumen_operativo")
    private String resumenOperativo;
    private String fecha;
    private String hora;
    private String prioridad;
    private String estado;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agente")
    @JsonIgnore // Evita que al consultar una tarea se traiga a todo el agente (bucle)
    private Agentes agente;
}