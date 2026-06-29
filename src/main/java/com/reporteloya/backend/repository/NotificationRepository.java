package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("SELECT n FROM Notification n WHERE n.agente.id = :agenteId AND n.leida = false ORDER BY n.fechaCreacion DESC")
    List<Notification> findNoLeidasPorAgenteId(@Param("agenteId") UUID agenteId);

    @Query("SELECT n FROM Notification n WHERE n.agente.id = :agenteId ORDER BY n.fechaCreacion DESC")
    List<Notification> findPorAgenteId(@Param("agenteId") UUID agenteId);

    @Query("SELECT n FROM Notification n WHERE n.usuario.id = :usuarioId AND n.leida = false ORDER BY n.fechaCreacion DESC")
    List<Notification> findNoLeidasPorUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT n FROM Notification n WHERE n.usuario.id = :usuarioId ORDER BY n.fechaCreacion DESC")
    List<Notification> findPorUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT n FROM Notification n WHERE n.usuario.id = :usuarioId ORDER BY n.fechaCreacion DESC LIMIT 50")
    List<Notification> findUltimas50PorUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.usuario.id = :usuarioId AND n.id NOT IN (SELECT n2.id FROM Notification n2 WHERE n2.usuario.id = :usuarioId ORDER BY n2.fechaCreacion DESC LIMIT 50)")
    void eliminarExtrasParaUsuario(@Param("usuarioId") UUID usuarioId);

    @Modifying
    @Query("UPDATE Notification n SET n.leida = true WHERE n.agente.id = :agenteId AND n.leida = false")
    int marcarTodasLeidasPorAgenteId(@Param("agenteId") UUID agenteId);

    @Modifying
    @Query("UPDATE Notification n SET n.leida = true WHERE n.usuario.id = :usuarioId AND n.leida = false")
    int marcarTodasLeidasPorUsuarioId(@Param("usuarioId") UUID usuarioId);
}
