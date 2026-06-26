package com.reporteloya.backend.service;

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

    public RecaptchaService(
            @Value("${recaptcha.secret.key}") String secretKey,
            @Value("${recaptcha.score.threshold:0.5}") double scoreThreshold) {
        this.secretKey = secretKey;
        this.scoreThreshold = scoreThreshold;
        this.httpClient = HttpClient.newHttpClient();
    }

    public void verifyToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token de verificación ausente.");
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

            String responseBody = httpResponse.body();
            if (responseBody == null || !responseBody.contains("\"success\": true")) {
                throw new IllegalArgumentException("Verificación de seguridad fallida.");
            }

            double score = extractScore(responseBody);
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

    private double extractScore(String json) {
        String key = "\"score\":";
        int idx = json.indexOf(key);
        if (idx < 0) return 0.0;

        int start = idx + key.length();
        int end = json.indexOf(',', start);
        if (end < 0) end = json.indexOf('}', start);
        if (end < 0) return 0.0;

        try {
            return Double.parseDouble(json.substring(start, end).trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}
