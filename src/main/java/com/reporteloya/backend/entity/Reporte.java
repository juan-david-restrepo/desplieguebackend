package com.reporteloya.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.CascadeType;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Getter
@Setter
@Table(name = "reporte")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_reporte", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_agente")
    @JsonIgnoreProperties({"reportes", "listaTareas", "password", "email", "rol", "nombreCompleto"})
    private Agentes agente;

    @ManyToOne
    @JoinColumn(name = "id_agente_companero")
    @JsonIgnoreProperties({"reportes", "listaTareas", "password", "email", "rol", "nombreCompleto"})
    private Agentes agenteCompanero;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Size(max = 100)
    private String tipoInfraccion;

    @Size(max = 1000)
    private String descripcion;

    @Size(max = 500)
    private String direccion;

    private Double latitud;
    private Double longitud;

    @Size(max = 20)
    @Pattern(regexp = "^[A-Z0-9\\-]*$", message = "Placa inválida")
    private String placa;

    private String estado;

    private Boolean acompanado = false;

    private LocalDate fechaIncidente;
    private LocalTime horaIncidente;

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Evidencia> evidencias;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Column(name = "created_at")
    @JsonIgnore
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonIgnore
    private LocalDateTime updatedAt;

    @Column(length = 1000)
    private String resumenOperativo;

    private Boolean huboComparendo;

    private LocalDateTime fechaAceptado;

    private LocalDateTime fechaFinalizado;

    private LocalDateTime fechaRechazado;

    @Column(length = 500)
    private String motivoRechazo;
}
