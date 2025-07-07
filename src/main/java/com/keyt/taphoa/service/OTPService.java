package com.keyt.taphoa.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keyt.taphoa.model.OTP;
import com.keyt.taphoa.model.OTP.OTPType;
import com.keyt.taphoa.model.User;
import com.keyt.taphoa.repository.OTPRepository;

@Service
@Transactional
public class OTPService {
    
    @Autowired
    private OTPRepository otpRepository;
    
    @Autowired
    private EmailService emailService;
    
    public OTP generateOTP(User user, OTPType type) {
        // Xóa OTP cũ nếu có
        otpRepository.findByUserAndTypeAndUsedFalse(user, type)
                .ifPresent(otp -> otpRepository.delete(otp));
        
        // Kiểm tra giới hạn OTP (tối đa 3 OTP trong 1 giờ)
        long recentOTPs = otpRepository.countRecentOTPsByUser(user, LocalDateTime.now().minusHours(1));
        if (recentOTPs >= 3) {
            throw new RuntimeException("Bạn đã yêu cầu quá nhiều mã OTP. Vui lòng thử lại sau 1 giờ.");
        }
        
        // Tạo OTP mới
        String code = generateOTPCode();
        OTP otp = new OTP(code, user, type);
        OTP savedOTP = otpRepository.save(otp);
        
        // Gửi OTP qua email
        try {
            sendOTPEmail(user, code, type);
        } catch (Exception e) {
            System.err.println("Không thể gửi email OTP: " + e.getMessage());
        }
        
        return savedOTP;
    }
    
    public boolean validateOTP(String code, User user, OTPType type) {
        Optional<OTP> otpOpt = otpRepository.findValidOTPByUserAndType(user, type, LocalDateTime.now());
        
        if (otpOpt.isPresent()) {
            OTP otp = otpOpt.get();
            if (otp.getCode().equals(code)) {
                otp.setUsed(true);
                otpRepository.save(otp);
                return true;
            }
        }
        
        return false;
    }
    
    public void cleanupExpiredOTPs() {
        otpRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
    
    private String generateOTPCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    
    private void sendOTPEmail(User user, String code, OTPType type) {
        String subject = "Mã OTP - " + type.getDisplayName();
        String content = String.format("""
            Xin chào %s,
            
            Mã OTP của bạn là: %s
            
            Mã này có hiệu lực trong 5 phút.
            Vui lòng không chia sẻ mã này với người khác.
            
            Trân trọng,
            Tạp Hóa KeyT
            """, user.getFullName(), code);
        
        emailService.sendSimpleEmail(user.getEmail(), subject, content);
    }
} 