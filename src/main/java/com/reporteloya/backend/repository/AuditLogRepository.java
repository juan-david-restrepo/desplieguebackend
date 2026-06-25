package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByUsuarioIdOrderByFechaDesc(UUID usuarioId);

    List<AuditLog> findByEntidadAndEntidadIdOrderByFechaDesc(String entidad, UUID entidadId);
}
