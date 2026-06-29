package com.reporteloya.backend.service;

import com.reporteloya.backend.entity.Reporte;
import com.reporteloya.backend.repository.ReporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ReporteExpirationScheduler {

    private static final Logger logger = Logger.getLogger(ReporteExpirationScheduler.class.getName());

    private final ReporteRepository reporteRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ReporteService reporteService;

    /**
     * Corre cada hora. Marca como EXPIRADO todo reporte que lleve más de 24 horas
     * en estado PENDIENTE sin ser atendido. Los reportes expirados dejan de
     * aparecer en la cola del agente/admin pero siguen contando en las
     * estadísticas de "reportes creados hoy" porque esa query no filtra por estado.
     */
    @Scheduled(fixedRate = 3_600_000)
    @Transactional
    public void expirarReportesPendientes() {
        LocalDateTime limite = LocalDateTime.now().minusHours(24);

        List<Reporte> vencidos = reporteRepository
                .findByEstadoAndCreatedAtBefore("PENDIENTE", limite);

        if (vencidos.isEmpty()) return;

        vencidos.forEach(reporte -> reporte.setEstado("EXPIRADO"));
        reporteRepository.saveAll(vencidos);
        vencidos.forEach(reporte ->
            messagingTemplate.convertAndSend("/topic/reportes", reporteService.convertirADTO(reporte))
        );

        logger.info("[Scheduler] Reportes expirados por inactividad de 24h: " + vencidos.size());
    }
}
