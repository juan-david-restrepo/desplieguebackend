package com.reporteloya.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "evidencia", indexes = {
    @Index(name = "idx_evidencia_reporte", columnList = "reporte_id")
})
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_evidencia", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private UUID id;

    private String tipo;
    private String archivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id")
    @JsonIgnore
    private Reporte reporte;

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public void setReporte(Reporte reporte) {
        this.reporte = reporte;
    }

}