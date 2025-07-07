package com.keyt.taphoa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "coupons")
public class Coupon {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CouponType type;
    
    @Column(nullable = false)
    private BigDecimal discountValue;
    
    @Column(nullable = false)
    private BigDecimal minimumOrderAmount;
    
    @Column(nullable = false)
    private int maxUsage;
    
    @Column(nullable = false)
    private int currentUsage = 0;
    
    @Column(nullable = false)
    private LocalDateTime validFrom;
    
    @Column(nullable = false)
    private LocalDateTime validTo;
    
    @Column(nullable = false)
    private boolean active = true;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum CouponType {
        PERCENTAGE("Giảm theo phần trăm"),
        FIXED_AMOUNT("Giảm theo số tiền");
        
        private final String displayName;
        
        CouponType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public Coupon() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public Coupon(String code, String name, CouponType type, BigDecimal discountValue) {
        this();
        this.code = code.toUpperCase();
        this.name = name;
        this.type = type;
        this.discountValue = discountValue;
        this.minimumOrderAmount = BigDecimal.ZERO;
        this.maxUsage = 1000; // Mặc định 1000 lần sử dụng
        this.validFrom = LocalDateTime.now();
        this.validTo = LocalDateTime.now().plusMonths(1); // Có hiệu lực 1 tháng
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code.toUpperCase();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public CouponType getType() {
        return type;
    }
    
    public void setType(CouponType type) {
        this.type = type;
    }
    
    public BigDecimal getDiscountValue() {
        return discountValue;
    }
    
    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }
    
    public BigDecimal getMinimumOrderAmount() {
        return minimumOrderAmount;
    }
    
    public void setMinimumOrderAmount(BigDecimal minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }
    
    public int getMaxUsage() {
        return maxUsage;
    }
    
    public void setMaxUsage(int maxUsage) {
        this.maxUsage = maxUsage;
    }
    
    public int getCurrentUsage() {
        return currentUsage;
    }
    
    public void setCurrentUsage(int currentUsage) {
        this.currentUsage = currentUsage;
    }
    
    public LocalDateTime getValidFrom() {
        return validFrom;
    }
    
    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }
    
    public LocalDateTime getValidTo() {
        return validTo;
    }
    
    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Business logic methods
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return active && 
               now.isAfter(validFrom) && 
               now.isBefore(validTo) && 
               currentUsage < maxUsage;
    }
    
    public boolean canApply(BigDecimal orderAmount) {
        return isValid() && orderAmount.compareTo(minimumOrderAmount) >= 0;
    }
    
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!canApply(orderAmount)) {
            return BigDecimal.ZERO;
        }
        
        if (type == CouponType.PERCENTAGE) {
            return orderAmount.multiply(discountValue).divide(BigDecimal.valueOf(100));
        } else {
            return discountValue;
        }
    }
    
    public void incrementUsage() {
        this.currentUsage++;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(validTo);
    }
    
    public boolean isUsageLimitReached() {
        return currentUsage >= maxUsage;
    }
} 