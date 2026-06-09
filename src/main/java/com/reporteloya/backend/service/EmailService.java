package com.reporteloya.backend.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from-email:noreply@reporteloya.com}")
    private String fromEmail;

    public void enviarCorreoRecuperacion(String destinatario, String enlace) {
        String subject = "Recupera tu contraseña - RepórteloYa";
        String html = """
                <div style="font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;">
                    <div style="max-width:600px; margin:auto; background:white; padding:30px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                        <div style="text-align:center; margin-bottom:20px;">
                            <img src="https://res.cloudinary.com/drsvslvlg/image/upload/f_auto,q_auto/LogoNuevo2_cepyj6" alt="Repórtelo Ya" style="width:120px; height:auto; display:block; margin:auto;">
                        </div>
                        <h2 style="color:#2563eb; text-align:center;">Recuperación de contraseña - Repórtelo Ya</h2>
                        <p>Hola,</p>
                        <p>Hemos recibido una solicitud para restablecer la contraseña asociada a su cuenta en Repórtelo Ya.</p>
                        <p>Para continuar con el proceso, haga clic en el siguiente botón:</p>
                        <div style="text-align:center; margin:30px 0;">
                            <a href="%s" style="background-color:#2563eb; color:white; padding:12px 25px; text-decoration:none; border-radius:6px; font-weight:bold; display:inline-block;">
                               Restablecer contraseña
                            </a>
                        </div>
                        <p style="font-size:14px; color:#555;">Este enlace expirará en 60 minutos por motivos de seguridad.</p>
                        <hr style="margin:25px 0;">
                        <p style="font-size:12px; color:#888;">Si usted no solicitó este cambio, puede ignorar este mensaje.</p>
                        <p style="font-size:12px; color:#888;">Atentamente,<br>El equipo de Repórtelo Ya</p>
                        <p style="font-size:12px; color:#888;">© 2026 Repórtelo Ya</p>
                    </div>
                </div>
                """.formatted(enlace);

        enviar(destinatario, subject, html);
    }

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
                        <p>Gracias por registrarte en Repórtelo Ya. Para completar tu registro, verifica tu correo haciendo clic en el botón de abajo:</p>
                        <div style="text-align:center; margin:30px 0;">
                            <a href="%s" style="background-color:#2563eb; color:white; padding:12px 25px; text-decoration:none; border-radius:6px; font-weight:bold; display:inline-block;">
                               Verificar correo electrónico
                            </a>
                        </div>
                        <p style="font-size:14px; color:#555;">Este enlace expirará en 60 minutos por motivos de seguridad.</p>
                        <hr style="margin:25px 0;">
                        <p style="font-size:12px; color:#888;">Si no creaste una cuenta en Repórtelo Ya, puedes ignorar este mensaje.</p>
                        <p style="font-size:12px; color:#888;">Atentamente,<br>El equipo de Repórtelo Ya</p>
                        <p style="font-size:12px; color:#888;">© 2026 Repórtelo Ya</p>
                    </div>
                </div>
                """.formatted(enlace);

        enviar(destinatario, subject, html);
    }

    private void enviar(String destinatario, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail, "RepórteloYa");
            helper.setTo(destinatario);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            log.info("Correo enviado a {}", destinatario);
        } catch (Exception e) {
            log.error("Error enviando correo a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error enviando correo", e);
        }
    }
}
