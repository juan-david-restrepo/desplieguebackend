package com.reporteloya.backend.service;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class ChatAISyncService {

    private final RestTemplate restTemplate;
    private final String syncUserUrl = "https://backend-ia-8in0.onrender.com/sync-user";

    public ChatAISyncService() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        this.restTemplate = new RestTemplate(factory);
    }

    @Async
    public void syncUser(UUID userId, String email, String nombreCompleto,
                         String role, String tipoDocumento, String numeroDocumento) {
        try {
            Map<String, Object> request = Map.of(
                "user_id", userId,
                "email", email,
                "nombre_completo", nombreCompleto,
                "role", role,
                "tipo_documento", tipoDocumento != null ? tipoDocumento : "",
                "numero_documento", numeroDocumento != null ? numeroDocumento : ""
            );
            restTemplate.postForObject(syncUserUrl, request, Map.class);
        } catch (Exception e) {
            System.err.println("Chat AI sync failed (non-blocking): " + e.getMessage());
        }
    }
}
