package com.keyt.taphoa.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.keyt.taphoa.model.Order;
import com.keyt.taphoa.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    /**
     * Gửi email đơn giản
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
    
    /**
     * Gửi email HTML sử dụng Thymeleaf template
     */
    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject(subject);
            
            Context context = new Context();
            if (variables != null) {
                variables.forEach(context::setVariable);
            }
            
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Không thể gửi email", e);
        }
    }
    
    /**
     * Gửi email xác nhận đăng ký
     */
    public void sendRegistrationConfirmation(User user) {
        Map<String, Object> variables = Map.of(
            "userName", user.getFullName(),
            "email", user.getEmail()
        );
        
        sendHtmlEmail(
            user.getEmail(),
            "Chào mừng bạn đến với Tạp Hóa KeyT!",
            "email/registration-confirmation",
            variables
        );
    }
    
    /**
     * Gửi email xác nhận đơn hàng
     */
    public void sendOrderConfirmation(Order order) {
        Map<String, Object> variables = Map.of(
            "order", order,
            "userName", order.getUser().getFullName(),
            "orderNumber", order.getId(),
            "totalAmount", order.getTotalAmount()
        );
        
        sendHtmlEmail(
            order.getUser().getEmail(),
            "Xác nhận đơn hàng #" + order.getId(),
            "email/order-confirmation",
            variables
        );
    }
    
    /**
     * Gửi email thông báo đơn hàng đã được xử lý
     */
    public void sendOrderProcessed(Order order) {
        Map<String, Object> variables = Map.of(
            "order", order,
            "userName", order.getUser().getFullName(),
            "orderNumber", order.getId(),
            "status", order.getStatus().getDisplayName()
        );
        
        sendHtmlEmail(
            order.getUser().getEmail(),
            "Đơn hàng #" + order.getId() + " đã được xử lý",
            "email/order-processed",
            variables
        );
    }
    
    /**
     * Gửi email thông báo gift code
     */
    public void sendGiftCode(Order order, String giftCode) {
        Map<String, Object> variables = Map.of(
            "order", order,
            "userName", order.getUser().getFullName(),
            "orderNumber", order.getId(),
            "giftCode", giftCode,
            "productName", order.getItems().get(0).getProduct().getName()
        );
        
        sendHtmlEmail(
            order.getUser().getEmail(),
            "Gift Code cho đơn hàng #" + order.getId(),
            "email/gift-code",
            variables
        );
    }
    
    /**
     * Gửi email thông báo đơn hàng bị từ chối
     */
    public void sendOrderRejected(Order order, String reason) {
        Map<String, Object> variables = Map.of(
            "order", order,
            "userName", order.getUser().getFullName(),
            "orderNumber", order.getId(),
            "reason", reason
        );
        
        sendHtmlEmail(
            order.getUser().getEmail(),
            "Đơn hàng #" + order.getId() + " bị từ chối",
            "email/order-rejected",
            variables
        );
    }
    
    /**
     * Gửi email reset password
     */
    public void sendPasswordReset(User user, String resetToken) {
        Map<String, Object> variables = Map.of(
            "userName", user.getFullName(),
            "resetToken", resetToken,
            "resetUrl", "http://localhost:8080/reset-password?token=" + resetToken
        );
        
        sendHtmlEmail(
            user.getEmail(),
            "Yêu cầu đặt lại mật khẩu",
            "email/password-reset",
            variables
        );
    }
} 