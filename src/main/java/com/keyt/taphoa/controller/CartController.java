package com.keyt.taphoa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.keyt.taphoa.model.Product;
import com.keyt.taphoa.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private ProductService productService;
    
    // View cart
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            return "redirect:/login";
        }
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        
        List<CartItem> cartItems = new ArrayList<>();
        double total = 0.0;
        
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productService.findById(entry.getKey());
            if (product != null && product.isAvailable()) {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(entry.getValue());
                item.setSubtotal(product.getPrice().multiply(java.math.BigDecimal.valueOf(entry.getValue())));
                cartItems.add(item);
                total += item.getSubtotal().doubleValue();
            }
        }
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("itemCount", cartItems.size());
        
        return "cart/view";
    }
    
    // Add to cart
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addToCart(@RequestParam Long productId, 
                                        @RequestParam(defaultValue = "1") Integer quantity,
                                        HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || "anonymousUser".equals(auth.getName())) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để thêm vào giỏ hàng!");
            return response;
        }
        
        try {
            Product product = productService.findById(productId);
            if (product == null || !product.isAvailable()) {
                response.put("success", false);
                response.put("message", "Sản phẩm không tồn tại hoặc đã hết hàng!");
                return response;
            }
            
            @SuppressWarnings("unchecked")
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }
            
            // Add or update quantity
            cart.put(productId, cart.getOrDefault(productId, 0) + quantity);
            session.setAttribute("cart", cart);
            
            response.put("success", true);
            response.put("message", "Đã thêm vào giỏ hàng!");
            response.put("cartCount", cart.size());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        return response;
    }
    
    // Update cart item quantity
    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCartItem(@RequestParam Long productId, 
                                            @RequestParam Integer quantity,
                                            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            response.put("success", false);
            response.put("message", "Giỏ hàng trống!");
            return response;
        }
        
        if (quantity <= 0) {
            cart.remove(productId);
        } else {
            cart.put(productId, quantity);
        }
        
        session.setAttribute("cart", cart);
        
        // Calculate new total
        double total = 0.0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            Product product = productService.findById(entry.getKey());
            if (product != null) {
                total += product.getPrice().multiply(java.math.BigDecimal.valueOf(entry.getValue())).doubleValue();
            }
        }
        
        response.put("success", true);
        response.put("total", total);
        response.put("cartCount", cart.size());
        
        return response;
    }
    
    // Remove from cart
    @PostMapping("/remove")
    @ResponseBody
    public Map<String, Object> removeFromCart(@RequestParam Long productId, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(productId);
            session.setAttribute("cart", cart);
        }
        
        response.put("success", true);
        response.put("message", "Đã xóa sản phẩm khỏi giỏ hàng!");
        response.put("cartCount", cart != null ? cart.size() : 0);
        
        return response;
    }
    
    // Clear cart
    @PostMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("cart");
        redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng!");
        return "redirect:/cart";
    }
    
    // Get cart count for AJAX
    @GetMapping("/count")
    @ResponseBody
    public Map<String, Object> getCartCount(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        @SuppressWarnings("unchecked")
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        int count = cart != null ? cart.size() : 0;
        
        response.put("count", count);
        return response;
    }
    
    // Inner class for cart items
    public static class CartItem {
        private Product product;
        private Integer quantity;
        private java.math.BigDecimal subtotal;
        
        // Getters and setters
        public Product getProduct() { return product; }
        public void setProduct(Product product) { this.product = product; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public java.math.BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(java.math.BigDecimal subtotal) { this.subtotal = subtotal; }
    }
} 