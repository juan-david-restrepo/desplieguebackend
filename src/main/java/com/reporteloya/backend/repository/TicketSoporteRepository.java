package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.EstadoTicket;
import com.reporteloya.backend.entity.TicketSoporte;
import com.reporteloya.backend.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketSoporteRepository extends JpaRepository<TicketSoporte, UUID> {

    List<TicketSoporte> findByUsuarioOrderByFechaActualizacionDesc(Usuario usuario);

    @EntityGraph(attributePaths = {"usuario", "mensajes"})
    List<TicketSoporte> findAllByOrderByFechaActualizacionDesc();

    @EntityGraph(attributePaths = {"usuario", "mensajes"})
    List<TicketSoporte> findByEstadoOrderByFechaActualizacionDesc(EstadoTicket estado);

    long countByEstado(EstadoTicket estado);

    @Query("SELECT t FROM TicketSoporte t LEFT JOIN FETCH t.mensajes WHERE t.id = :id")
    Optional<TicketSoporte> findByIdWithMensajes(@Param("id") UUID id);

    @EntityGraph(attributePaths = {"usuario", "mensajes"})
    @Query("SELECT t FROM TicketSoporte t WHERE t.usuario.id = :usuarioId ORDER BY t.fechaActualizacion DESC")
    List<TicketSoporte> findByUsuarioIdOrderByFechaActualizacionDesc(@Param("usuarioId") UUID usuarioId);
}
