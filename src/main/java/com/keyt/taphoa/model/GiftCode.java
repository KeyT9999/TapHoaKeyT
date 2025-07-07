package com.keyt.taphoa.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "gift_codes")
public class GiftCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String code;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GiftCodeStatus status = GiftCodeStatus.AVAILABLE;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column
    private LocalDateTime usedAt;
    
    @ManyToOne
    @JoinColumn(name = "used_by_user_id")
    private User usedByUser;
    
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    
    @Column
    private String notes;
    
    public GiftCode() {
        this.createdAt = LocalDateTime.now();
    }
    
    public GiftCode(String code, Product product) {
        this();
        this.code = code;
        this.product = product;
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
        this.code = code;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public GiftCodeStatus getStatus() {
        return status;
    }
    
    public void setStatus(GiftCodeStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUsedAt() {
        return usedAt;
    }
    
    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }
    
    public User getUsedByUser() {
        return usedByUser;
    }
    
    public void setUsedByUser(User usedByUser) {
        this.usedByUser = usedByUser;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public boolean isAvailable() {
        return status == GiftCodeStatus.AVAILABLE;
    }
    
    public boolean isUsed() {
        return status == GiftCodeStatus.USED;
    }
    
    public boolean isReserved() {
        return status == GiftCodeStatus.RESERVED;
    }
    
    public void markAsUsed(User user, Order order) {
        this.status = GiftCodeStatus.USED;
        this.usedAt = LocalDateTime.now();
        this.usedByUser = user;
        this.order = order;
    }
    
    public void markAsReserved() {
        this.status = GiftCodeStatus.RESERVED;
    }
    
    public void markAsAvailable() {
        this.status = GiftCodeStatus.AVAILABLE;
        this.usedAt = null;
        this.usedByUser = null;
        this.order = null;
    }
} 