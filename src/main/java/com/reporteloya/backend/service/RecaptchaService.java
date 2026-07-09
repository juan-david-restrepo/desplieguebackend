package com.reporteloya.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

            if (!root.path("success").asBoolean(false)) {
                throw new IllegalArgumentException("Verificación de seguridad fallida.");
            }

            double score = root.path("score").asDouble(0.0);
            if (score < scoreThreshold) {
                throw new IllegalArgumentException(
                        "No se pudo verificar que eres humano. Intenta de nuevo."
                );
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Error al verificar la seguridad. Intenta de nuevo."
            );
        }
    }
}
