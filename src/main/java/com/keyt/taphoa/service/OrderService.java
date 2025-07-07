package com.keyt.taphoa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keyt.taphoa.model.Order;
import com.keyt.taphoa.model.OrderStatus;
import com.keyt.taphoa.model.User;
import com.keyt.taphoa.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private EmailService emailService;
    
    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        
        // Gửi email xác nhận đơn hàng
        try {
            emailService.sendOrderConfirmation(savedOrder);
        } catch (Exception e) {
            System.err.println("Không thể gửi email xác nhận đơn hàng: " + e.getMessage());
        }
        
        return savedOrder;
    }
    
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + id));
    }
    
    public List<Order> findAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }
    
    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    public List<Order> findOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }
    
    public List<Order> findPendingOrders() {
        return findOrdersByStatus(OrderStatus.PENDING);
    }
    
    public List<Order> findCompletedOrders() {
        return findOrdersByStatus(OrderStatus.COMPLETED);
    }
    
    public List<Order> findCancelledOrders() {
        return findOrdersByStatus(OrderStatus.CANCELLED);
    }
    
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = findById(orderId);
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        
        // Add status-specific logic
        switch (status) {
            case CONFIRMED:
                order.setConfirmedAt(LocalDateTime.now());
                break;
            case COMPLETED:
                order.setCompletedAt(LocalDateTime.now());
                break;
            case CANCELLED:
                order.setCancelledAt(LocalDateTime.now());
                break;
        }
        
        Order updatedOrder = orderRepository.save(order);
        
        // Gửi email thông báo thay đổi trạng thái
        try {
            if (status == OrderStatus.COMPLETED) {
                emailService.sendOrderProcessed(updatedOrder);
            } else if (status == OrderStatus.CANCELLED) {
                emailService.sendOrderRejected(updatedOrder, "Đơn hàng bị hủy bởi admin");
            }
        } catch (Exception e) {
            System.err.println("Không thể gửi email thông báo trạng thái: " + e.getMessage());
        }
        
        return updatedOrder;
    }
    
    public void cancelOrder(Long orderId) {
        Order order = findById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng ở trạng thái chờ xử lý!");
        }
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }
    
    public void confirmOrder(Long orderId) {
        updateOrderStatus(orderId, OrderStatus.CONFIRMED);
    }
    
    public void completeOrder(Long orderId) {
        updateOrderStatus(orderId, OrderStatus.COMPLETED);
    }
    
    public long getTotalOrders() {
        return orderRepository.count();
    }
    
    public long getOrderCountByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }
    
    public double getTotalRevenue() {
        return orderRepository.findByStatus(OrderStatus.COMPLETED)
                .stream()
                .mapToDouble(order -> order.getTotalAmount().doubleValue())
                .sum();
    }
    
    public double getRevenueByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByStatusAndCreatedAtBetween(OrderStatus.COMPLETED, startDate, endDate)
                .stream()
                .mapToDouble(order -> order.getTotalAmount().doubleValue())
                .sum();
    }
    
    public List<Order> findOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startDate, endDate);
    }
    
    public Optional<Order> findLatestOrderByUser(User user) {
        return orderRepository.findFirstByUserOrderByCreatedAtDesc(user);
    }
    
    public boolean hasActiveOrders(User user) {
        return orderRepository.existsByUserAndStatusIn(user, 
                List.of(OrderStatus.PENDING, OrderStatus.CONFIRMED));
    }
    
    public Order updateOrder(Order order) {
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
    
    public void sendGiftCodeForOrder(Long orderId, String giftCode) {
        Order order = findById(orderId);
        try {
            emailService.sendGiftCode(order, giftCode);
        } catch (Exception e) {
            System.err.println("Không thể gửi email gift code: " + e.getMessage());
        }
    }
} 