package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, UUID> {

    List<Evidencia> findByReporteId(UUID idReporte);
}
