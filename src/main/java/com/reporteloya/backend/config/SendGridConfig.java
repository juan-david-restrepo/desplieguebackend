package com.reporteloya.backend.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Value("${spring.sendgrid.api-key}")
    private String sendgridApiKey;

    @Value("${spring.sendgrid.from-email:reporteloy@gmail.com}")
    private String sendgridFromEmail;

    @Bean
    public SendGrid sendGrid() {
        if (sendgridApiKey == null || sendgridApiKey.isBlank()) {
            throw new IllegalStateException("SENDGRID_API_KEY no configurada en variables de entorno");
        }
        return new SendGrid(sendgridApiKey);
    }

    @Bean
    public String sendgridFromEmail() {
        return sendgridFromEmail;
    }

    @Bean
    public String sendgridFromName() {
        return "RepórteloYa";
    }

}