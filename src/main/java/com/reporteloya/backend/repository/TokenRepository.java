package com.reporteloya.backend.repository;

import com.reporteloya.backend.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByEmail(String email);

    void deleteByExpirationDateBefore(LocalDateTime date);
}