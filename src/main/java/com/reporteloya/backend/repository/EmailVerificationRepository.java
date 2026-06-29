package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

public interface EmailVerificationRepository extends JpaRepository<EmailVerificationToken, UUID> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByEmail(String email);

    void deleteByEmail(String email);

    void deleteByExpirationDateBefore(LocalDateTime date);

    List<EmailVerificationToken> findByExpirationDateBeforeAndUsedFalse(LocalDateTime date);
}
