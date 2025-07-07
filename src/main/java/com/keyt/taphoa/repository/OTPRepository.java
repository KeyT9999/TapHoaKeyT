package com.keyt.taphoa.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.keyt.taphoa.model.OTP;
import com.keyt.taphoa.model.OTP.OTPType;
import com.keyt.taphoa.model.User;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {
    
    Optional<OTP> findByCode(String code);
    
    Optional<OTP> findByUserAndTypeAndUsedFalse(User user, OTPType type);
    
    @Query("SELECT o FROM OTP o WHERE o.user = :user AND o.type = :type AND o.used = false AND o.expiryDate > :now ORDER BY o.createdAt DESC")
    Optional<OTP> findValidOTPByUserAndType(@Param("user") User user, @Param("type") OTPType type, @Param("now") LocalDateTime now);
    
    void deleteByExpiryDateBefore(LocalDateTime date);
    
    @Query("SELECT COUNT(o) FROM OTP o WHERE o.user = :user AND o.createdAt > :since")
    long countRecentOTPsByUser(@Param("user") User user, @Param("since") LocalDateTime since);
} 