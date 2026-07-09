package com.reporteloya.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class RecaptchaService {

    private static final Logger log = LoggerFactory.getLogger(RecaptchaService.class);

    private final String secretKey;
    private final double scoreThreshold;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecaptchaService(
            @Value("${recaptcha.secret.key}") String secretKey,
            @Value("${recaptcha.score.threshold:0.5}") double scoreThreshold) {
        this.secretKey = secretKey;
        this.scoreThreshold = scoreThreshold;
        this.httpClient = HttpClient.newHttpClient();
    }

    public void verifyToken(String token) {
        if (secretKey == null || secretKey.isBlank()) {
            return;
        }
        if (token == null || token.isBlank()) {
            return;
        }

        try {
            String params = "secret=" + URLEncoder.encode(secretKey, StandardCharsets.UTF_8)
                    + "&response=" + URLEncoder.encode(token, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.google.com/recaptcha/api/siteverify"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(params))
                    .build();

            HttpResponse<String> httpResponse = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            JsonNode root = objectMapper.readTree(httpResponse.body());
            log.info("[reCAPTCHA] Google response: {}", httpResponse.body());

            boolean success = root.path("success").asBoolean(false);
            double score = root.path("score").asDouble(-1.0);
            log.info("[reCAPTCHA] success={}, score={}, threshold={}", success, score, scoreThreshold);

            if (!success) {
                JsonNode errorCodes = root.path("error-codes");
                log.warn("[reCAPTCHA] Falló: error-codes={}", errorCodes);

                // browser-error: el navegador del usuario tiene restricciones de privacidad
                // (bloqueadores, modo estricto, Brave, etc.). No es un bot — dejar pasar.
                boolean soloBrowserError = errorCodes.isArray()
                        && errorCodes.size() == 1
                        && "browser-error".equals(errorCodes.get(0).asText());
                if (soloBrowserError) {
                    log.info("[reCAPTCHA] browser-error ignorado — usuario legítimo con browser restrictivo");
                    return;
                }

                throw new IllegalArgumentException("Verificación de seguridad fallida.");
            }

            if (score < scoreThreshold) {
                log.warn("[reCAPTCHA] Score {} por debajo del umbral {}", score, scoreThreshold);
                throw new IllegalArgumentException(
                        "No se pudo verificar que eres humano. Intenta de nuevo."
                );
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            log.error("[reCAPTCHA] Excepción inesperada verificando token", e);
            throw new IllegalArgumentException(
                    "Error al verificar la seguridad. Intenta de nuevo."
            );
        }
    }
}
