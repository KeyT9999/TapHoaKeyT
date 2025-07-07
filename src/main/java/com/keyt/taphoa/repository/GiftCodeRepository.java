package com.keyt.taphoa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.keyt.taphoa.model.GiftCode;
import com.keyt.taphoa.model.GiftCodeStatus;
import com.keyt.taphoa.model.Product;

@Repository
public interface GiftCodeRepository extends JpaRepository<GiftCode, Long> {
    
    Optional<GiftCode> findByCode(String code);
    
    List<GiftCode> findByProduct(Product product);
    
    List<GiftCode> findByProductAndStatus(Product product, GiftCodeStatus status);
    
    List<GiftCode> findByStatus(GiftCodeStatus status);
    
    @Query("SELECT COUNT(g) FROM GiftCode g WHERE g.product = :product AND g.status = :status")
    long countByProductAndStatus(@Param("product") Product product, @Param("status") GiftCodeStatus status);
    
    @Query("SELECT COUNT(g) FROM GiftCode g WHERE g.status = :status")
    long countByStatus(@Param("status") GiftCodeStatus status);
    
    @Query("SELECT g FROM GiftCode g WHERE g.product = :product AND g.status = 'AVAILABLE' ORDER BY g.createdAt ASC")
    List<GiftCode> findAvailableByProduct(@Param("product") Product product);
    
    Optional<GiftCode> findFirstByProductAndStatusOrderByCreatedAtAsc(Product product, GiftCodeStatus status);
} 