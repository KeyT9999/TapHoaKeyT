package com.keyt.taphoa.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.keyt.taphoa.model.Order;
import com.keyt.taphoa.model.OrderItem;
import com.keyt.taphoa.model.OrderStatus;
import com.keyt.taphoa.model.Product;
import com.keyt.taphoa.model.Role;
import com.keyt.taphoa.model.User;
import com.keyt.taphoa.service.OrderService;
import com.keyt.taphoa.service.ProductService;
import com.keyt.taphoa.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    // View user orders
    @GetMapping
    public String viewOrders(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }
        
        User user = userService.findByEmail(auth.getName());
        List<Order> orders = orderService.findOrdersByUser(user);
        
        model.addAttribute("orders", orders);
        return "orders/list";
    }
    
    // View order details
    @GetMapping("/{orderId}")
    public String viewOrderDetails(@PathVariable Long orderId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }
        
        User user = userService.findByEmail(auth.getName());
        Order order = orderService.findById(orderId);
        
        // Check if order belongs to user or user is admin
        if (!order.getUser().getId().equals(user.getId()) && 
            user.getRole() != Role.ROLE_ADMIN) {
            return "redirect:/orders";
        }
        
        model.addAttribute("order", order);
        return "orders/detail";
    }
    
    // Checkout page
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }
        
        // Build checkout items
        List<CheckoutItem> checkoutItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productService.findById(entry.getKey());
            if (product != null && product.isAvailable()) {
                CheckoutItem item = new CheckoutItem();
                item.setProduct(product);
                item.setQuantity(entry.getValue());
                item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
                checkoutItems.add(item);
                total = total.add(item.getSubtotal());
            }
        }
        
        if (checkoutItems.isEmpty()) {
            return "redirect:/cart";
        }
        
        User user = userService.findByEmail(auth.getName());
        
        model.addAttribute("checkoutItems", checkoutItems);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        
        return "orders/checkout";
    }
    
    // Place order
    @PostMapping("/place")
    public String placeOrder(@RequestParam String customerName,
                           @RequestParam String customerPhone,
                           @RequestParam String customerEmail,
                           @RequestParam String paymentMethod,
                           @RequestParam(required = false) String notes,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
            return "redirect:/cart";
        }
        
        try {
            User user = userService.findByEmail(auth.getName());
            
            // Create order
            Order order = new Order();
            order.setUser(user);
            order.setCustomerName(customerName);
            order.setCustomerPhone(customerPhone);
            order.setCustomerEmail(customerEmail);
            order.setPaymentMethod(paymentMethod);
            order.setNotes(notes);
            order.setStatus(OrderStatus.PENDING);
            order.setTotalAmount(BigDecimal.ZERO);
            
            // Create order items
            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;
            
            for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
                Product product = productService.findById(entry.getKey());
                if (product != null && product.isAvailable()) {
                    OrderItem item = new OrderItem();
                    item.setOrder(order);
                    item.setProduct(product);
                    item.setQuantity(entry.getValue());
                    item.setPrice(product.getPrice());
                    // Subtotal is calculated by getSubtotal() method
                    orderItems.add(item);
                    total = total.add(item.getSubtotal());
                }
            }
            
            order.setTotalAmount(total);
            order.setOrderItems(orderItems);
            
            // Save order
            Order savedOrder = orderService.createOrder(order);
            
            // Clear cart
            session.removeAttribute("cart");
            
            redirectAttributes.addFlashAttribute("success", 
                "Đặt hàng thành công! Mã đơn hàng: " + savedOrder.getId());
            return "redirect:/orders/" + savedOrder.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
            return "redirect:/checkout";
        }
    }
    
    // Cancel order
    @PostMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Long orderId, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }
        
        try {
            User user = userService.findByEmail(auth.getName());
            Order order = orderService.findById(orderId);
            
            // Check if order belongs to user
            if (!order.getUser().getId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Bạn không có quyền hủy đơn hàng này!");
                return "redirect:/orders";
            }
            
            // Check if order can be cancelled
            if (order.getStatus() != OrderStatus.PENDING) {
                redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng ở trạng thái này!");
                return "redirect:/orders/" + orderId;
            }
            
            orderService.cancelOrder(orderId);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công!");
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        return "redirect:/orders";
    }
    
    // Inner class for checkout items
    public static class CheckoutItem {
        private Product product;
        private Integer quantity;
        private BigDecimal subtotal;
        
        // Getters and setters
        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }
} 