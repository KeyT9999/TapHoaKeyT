package com.keyt.taphoa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.keyt.taphoa.model.PasswordResetToken;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    Optional<PasswordResetToken> findByUserEmail(String email);
    
    void deleteByUserEmail(String email);
    
    void deleteByExpiryDateBefore(java.time.LocalDateTime date);
} 