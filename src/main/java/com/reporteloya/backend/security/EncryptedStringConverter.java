package com.reporteloya.backend.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-256-GCM encryption for PII fields stored in the database.
 * OWASP A02 - Cryptographic Failures protection.
 *
 * Requires env variable: DB_ENCRYPTION_KEY (32-byte Base64-encoded key)
 * Generate with: openssl rand -base64 32
 */
@Converter
@Component
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private static SecretKeySpec secretKey;

    @Value("${app.encryption.key:#{null}}")
    public void setEncryptionKey(String keyBase64) {
        if (keyBase64 != null && !keyBase64.isBlank()) {
            byte[] keyBytes = Base64.getDecoder().decode(keyBase64);
            secretKey = new SecretKeySpec(keyBytes, "AES");
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null || secretKey == null) return attribute;
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            byte[] encrypted = cipher.doFinal(attribute.getBytes("UTF-8"));

            byte[] combined = new byte[GCM_IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, GCM_IV_LENGTH);
            System.arraycopy(encrypted, 0, combined, GCM_IV_LENGTH, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new IllegalStateException("Error encriptando campo PII", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null || secretKey == null) return dbData;
        try {
            byte[] combined = Base64.getDecoder().decode(dbData);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(combined, GCM_IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(GCM_TAG_LENGTH, iv));
            return new String(cipher.doFinal(encrypted), "UTF-8");
        } catch (Exception e) {
            throw new IllegalStateException("Error desencriptando campo PII", e);
        }
    }
}
