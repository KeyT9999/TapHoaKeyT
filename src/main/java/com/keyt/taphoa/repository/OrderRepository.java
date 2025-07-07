package com.keyt.taphoa.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.keyt.taphoa.model.Order;
import com.keyt.taphoa.model.OrderStatus;
import com.keyt.taphoa.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    List<Order> findByUser(User user);
    
    List<Order> findByStatus(OrderStatus status);
    
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllOrderByCreatedAtDesc();
    
    @Query("SELECT o FROM Order o WHERE o.status = :status ORDER BY o.createdAt DESC")
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    long countByStatus(OrderStatus status);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status")
    BigDecimal sumTotalByStatus(OrderStatus status);
    
    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status AND o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.customerEmail LIKE %:keyword% OR o.customerName LIKE %:keyword% OR o.customerPhone LIKE %:keyword%")
    List<Order> searchByKeyword(String keyword);
    
    List<Order> findByGiftDeliveredFalseAndStatus(OrderStatus status);
    
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findAllByOrderByCreatedAtDesc();
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o WHERE o.user = :user ORDER BY o.createdAt DESC")
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    
    @Query("SELECT o FROM Order o WHERE o.user = :user ORDER BY o.createdAt DESC")
    java.util.Optional<Order> findFirstByUserOrderByCreatedAtDesc(User user);
    
    @Query("SELECT COUNT(o) > 0 FROM Order o WHERE o.user = :user AND o.status IN :statuses")
    boolean existsByUserAndStatusIn(User user, List<OrderStatus> statuses);
} 