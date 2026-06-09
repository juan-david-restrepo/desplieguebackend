package com.reporteloya.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api.key:}")
    private String apiKey;

    @Value("${brevo.from.email:noreply@reporteloya.com}")
    private String fromEmail;

    @Async
    public void enviarCorreoRecuperacion(String destinatario, String enlace) {
        String subject = "Recupera tu contraseña - RepórteloYa";
        String html = """
                <div style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                    <div style="max-width:600px; margin:auto; background:white; padding:30px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                        <div style="text-align:center; margin-bottom:20px;">
                            <img src="https://res.cloudinary.com/drsvslvlg/image/upload/f_auto,q_auto/LogoNuevo2_cepyj6" alt="Repórtelo Ya" style="width:120px; height:auto; display:block; margin:auto;">
                        </div>
                        <h2 style="color:#2563eb; text-align:center;">Recuperación de contraseña</h2>
                        <p>Hola,</p>
                        <p>Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en Repórtelo Ya.</p>
                        <div style="text-align:center; margin:30px 0;">
                            <a href="%s" style="background-color:#2563eb; color:white; padding:12px 25px; text-decoration:none; border-radius:6px; font-weight:bold; display:inline-block;">
                               Restablecer contraseña
                            </a>
                        </div>
                        <p style="font-size:14px; color:#555;">Este enlace expirará en 60 minutos.</p>
                        <hr style="margin:25px 0;">
                        <p style="font-size:12px; color:#888;">Si no solicitaste este cambio, puedes ignorar este mensaje.</p>
                        <p style="font-size:12px; color:#888;">© 2026 Repórtelo Ya</p>
                    </div>
                </div>
                """.formatted(enlace);

        enviar(destinatario, subject, html);
    }

    @Async
    public void enviarCorreoVerificacion(String destinatario, String enlace) {
        String subject = "Verifica tu correo electrónico - RepórteloYa";
        String html = """
                <div style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                    <div style="max-width:600px; margin:auto; background:white; padding:30px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                        <div style="text-align:center; margin-bottom:20px;">
                            <img src="https://res.cloudinary.com/drsvslvlg/image/upload/f_auto,q_auto/LogoNuevo2_cepyj6" alt="Repórtelo Ya" style="width:120px; height:auto; display:block; margin:auto;">
                        </div>
                        <h2 style="color:#2563eb; text-align:center;">Verifica tu correo electrónico</h2>
                        <p>Hola,</p>
                        <p>Gracias por registrarte en Repórtelo Ya. Haz clic en el botón para verificar tu cuenta:</p>
                        <div style="text-align:center; margin:30px 0;">
                            <a href="%s" style="background-color:#2563eb; color:white; padding:12px 25px; text-decoration:none; border-radius:6px; font-weight:bold; display:inline-block;">
                               Verificar correo electrónico
                            </a>
                        </div>
                        <p style="font-size:14px; color:#555;">Este enlace expirará en 60 minutos.</p>
                        <hr style="margin:25px 0;">
                        <p style="font-size:12px; color:#888;">Si no creaste esta cuenta, puedes ignorar este mensaje.</p>
                        <p style="font-size:12px; color:#888;">© 2026 Repórtelo Ya</p>
                    </div>
                </div>
                """.formatted(enlace);

        enviar(destinatario, subject, html);
    }

    private void enviar(String destinatario, String subject, String html) {
        if (apiKey == null || apiKey.isBlank()) {
            log.error("BREVO_API_KEY no configurada - correo no enviado a {}", destinatario);
            return;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("api-key", apiKey);

            Map<String, Object> body = Map.of(
                "sender", Map.of("name", "RepórteloYa", "email", fromEmail),
                "to", List.of(Map.of("email", destinatario)),
                "subject", subject,
                "htmlContent", html
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Correo enviado a {}", destinatario);
            } else {
                log.error("Error Brevo API - status: {} body: {}", response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            log.error("Error enviando correo a {}: {}", destinatario, e.getMessage());
        }
    }
}
