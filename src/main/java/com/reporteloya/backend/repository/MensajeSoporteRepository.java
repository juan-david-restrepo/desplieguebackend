package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.MensajeSoporte;
import com.reporteloya.backend.entity.TicketSoporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MensajeSoporteRepository extends JpaRepository<MensajeSoporte, UUID> {

    List<MensajeSoporte> findByTicketOrderByFechaEnvioAsc(TicketSoporte ticket);
}
