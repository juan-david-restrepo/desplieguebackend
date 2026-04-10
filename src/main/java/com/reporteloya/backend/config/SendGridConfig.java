package com.reporteloya.backend.config;

import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Bean
    public SendGrid sendGrid() {
        String apiKey = System.getenv("SENDGRID_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("SENDGRID_API_KEY no configurada en variables de entorno");
        }
        return new SendGrid(apiKey);
    }

    @Bean
    public String sendgridFromEmail() {
        String email = System.getenv("SENDGRID_FROM_EMAIL");
        return email != null && !email.isBlank() ? email : "reporteloy@gmail.com";
    }

    @Bean
    public String sendgridFromName() {
        return "RepórteloYa";
    }

}