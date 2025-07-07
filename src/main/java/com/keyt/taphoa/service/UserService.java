package com.keyt.taphoa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keyt.taphoa.model.PasswordResetToken;
import com.keyt.taphoa.model.Role;
import com.keyt.taphoa.model.User;
import com.keyt.taphoa.repository.PasswordResetTokenRepository;
import com.keyt.taphoa.repository.UserRepository;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    public User registerUser(String email, String password, String fullName, String phoneNumber) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setRole(Role.ROLE_USER);
        user.setEnabled(true);
        
        User savedUser = userRepository.save(user);
        
        // Gửi email chào mừng
        try {
            emailService.sendRegistrationConfirmation(savedUser);
        } catch (Exception e) {
            // Log lỗi nhưng không ảnh hưởng đến việc đăng ký
            System.err.println("Không thể gửi email chào mừng: " + e.getMessage());
        }
        
        return savedUser;
    }
    
    public User createAdmin(String email, String password, String fullName) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã được sử dụng!");
        }
        
        User admin = new User();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setFullName(fullName);
        admin.setRole(Role.ROLE_ADMIN);
        admin.setEnabled(true);
        
        return userRepository.save(admin);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
    }
    
    public Optional<User> findByEmailOptional(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));
    }
    
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public List<User> findUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }
    
    public List<User> searchUsers(String keyword) {
        return userRepository.searchByKeyword(keyword);
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    public void enableUser(Long userId) {
        User user = findById(userId);
        user.setEnabled(true);
        userRepository.save(user);
    }
    
    public void disableUser(Long userId) {
        User user = findById(userId);
        user.setEnabled(false);
        userRepository.save(user);
    }
    
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = findByEmail(email);
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public long getTotalUsers() {
        return userRepository.count();
    }
    
    public long getUserCountByRole(Role role) {
        return userRepository.countByRole(role);
    }
    
    public void deleteUser(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }
    
    // Password Reset Methods
    public void createPasswordResetTokenForUser(String email) {
        User user = findByEmail(email);
        String token = UUID.randomUUID().toString();
        
        // Xóa token cũ nếu có
        passwordResetTokenRepository.deleteByUserEmail(email);
        
        // Tạo token mới
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(passwordResetToken);
        
        // Gửi email
        emailService.sendPasswordReset(user, token);
    }
    
    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElse(null);
        
        if (passwordResetToken == null) {
            return false;
        }
        
        return passwordResetToken.isValid();
    }
    
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ!"));
        
        if (!passwordResetToken.isValid()) {
            throw new RuntimeException("Token đã hết hạn hoặc đã được sử dụng!");
        }
        
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Đánh dấu token đã sử dụng
        passwordResetToken.setUsed(true);
        passwordResetTokenRepository.save(passwordResetToken);
    }
    
    // Cleanup expired tokens
    public void cleanupExpiredTokens() {
        passwordResetTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
} 