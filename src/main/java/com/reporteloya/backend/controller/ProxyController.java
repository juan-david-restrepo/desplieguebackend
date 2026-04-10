package com.reporteloya.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Proxy para llamadas a OSRM (servicio de rutas)
     * Evita problemas de CORS con el navegador
     */
    @GetMapping("/route/{profile}/{coordinates}")
    public ResponseEntity<?> getRoute(
            @PathVariable String profile,
            @PathVariable String coordinates,
            @RequestParam(required = false, defaultValue = "full") String overview,
            @RequestParam(required = false, defaultValue = "geojson") String geometries) {

        try {
            String osrmUrl = String.format(
                    "https://router.project-osrm.org/route/v1/%s/%s?overview=%s&geometries=%s",
                    profile, coordinates, overview, geometries
            );

            String response = restTemplate.getForObject(osrmUrl, String.class);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al consultar rutas", "message", e.getMessage()));
        }
    }

    /**
     * Proxy alternativo con POST para mayor flexibilidad
     */
    @PostMapping("/route")
    public ResponseEntity<?> getRoutePost(@RequestBody Map<String, Object> request) {

        try {
            String profile = (String) request.getOrDefault("profile", "driving");
            String coordinates = (String) request.get("coordinates");
            String overview = (String) request.getOrDefault("overview", "full");
            String geometries = (String) request.getOrDefault("geometries", "geojson");

            if (coordinates == null || coordinates.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Coordinates son requeridas"));
            }

            String osrmUrl = String.format(
                    "https://router.project-osrm.org/route/v1/%s/%s?overview=%s&geometries=%s",
                    profile, coordinates, overview, geometries
            );

            String response = restTemplate.getForObject(osrmUrl, String.class);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al consultar rutas", "message", e.getMessage()));
        }
    }
}