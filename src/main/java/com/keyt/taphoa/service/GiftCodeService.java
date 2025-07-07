package com.keyt.taphoa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keyt.taphoa.model.GiftCode;
import com.keyt.taphoa.model.GiftCodeStatus;
import com.keyt.taphoa.model.Order;
import com.keyt.taphoa.model.Product;
import com.keyt.taphoa.model.User;
import com.keyt.taphoa.repository.GiftCodeRepository;

@Service
@Transactional
public class GiftCodeService {
    
    @Autowired
    private GiftCodeRepository giftCodeRepository;
    
    @Autowired
    private EmailService emailService;
    
    public GiftCode createGiftCode(Product product) {
        String code = generateUniqueCode();
        GiftCode giftCode = new GiftCode(code, product);
        return giftCodeRepository.save(giftCode);
    }
    
    public List<GiftCode> createGiftCodes(Product product, int quantity) {
        List<GiftCode> giftCodes = new java.util.ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            giftCodes.add(createGiftCode(product));
        }
        return giftCodeRepository.saveAll(giftCodes);
    }
    
    public Optional<GiftCode> getAvailableGiftCode(Product product) {
        return giftCodeRepository.findFirstByProductAndStatusOrderByCreatedAtAsc(product, GiftCodeStatus.AVAILABLE);
    }
    
    public GiftCode reserveGiftCode(Product product) {
        Optional<GiftCode> availableCode = getAvailableGiftCode(product);
        if (availableCode.isPresent()) {
            GiftCode giftCode = availableCode.get();
            giftCode.markAsReserved();
            return giftCodeRepository.save(giftCode);
        }
        throw new RuntimeException("Không có gift code khả dụng cho sản phẩm này!");
    }
    
    public void assignGiftCodeToOrder(GiftCode giftCode, User user, Order order) {
        giftCode.markAsUsed(user, order);
        giftCodeRepository.save(giftCode);
        
        // Gửi email gift code
        try {
            emailService.sendGiftCode(order, giftCode.getCode());
        } catch (Exception e) {
            System.err.println("Không thể gửi email gift code: " + e.getMessage());
        }
    }
    
    public List<GiftCode> getAllGiftCodes() {
        return giftCodeRepository.findAll();
    }
    
    public List<GiftCode> getGiftCodesByProduct(Product product) {
        return giftCodeRepository.findByProduct(product);
    }
    
    public List<GiftCode> getGiftCodesByStatus(GiftCodeStatus status) {
        return giftCodeRepository.findByStatus(status);
    }
    
    public long countAvailableGiftCodes(Product product) {
        return giftCodeRepository.countByProductAndStatus(product, GiftCodeStatus.AVAILABLE);
    }
    
    public long countTotalGiftCodes(Product product) {
        return giftCodeRepository.countByProduct(product);
    }
    
    public long countUsedGiftCodes(Product product) {
        return giftCodeRepository.countByProductAndStatus(product, GiftCodeStatus.USED);
    }
    
    public void deleteGiftCode(Long id) {
        giftCodeRepository.deleteById(id);
    }
    
    public GiftCode updateGiftCode(GiftCode giftCode) {
        return giftCodeRepository.save(giftCode);
    }
    
    public Optional<GiftCode> findByCode(String code) {
        return giftCodeRepository.findByCode(code);
    }
    
    public boolean isCodeValid(String code) {
        Optional<GiftCode> giftCode = findByCode(code);
        return giftCode.isPresent() && giftCode.get().isAvailable();
    }
    
    private String generateUniqueCode() {
        String code;
        do {
            // Tạo code 12 ký tự: 4 chữ cái + 4 số + 4 chữ cái
            String letters1 = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            String numbers = String.valueOf((int)(Math.random() * 9000) + 1000);
            String letters2 = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
            code = letters1 + numbers + letters2;
        } while (giftCodeRepository.findByCode(code).isPresent());
        
        return code;
    }
    
    public void cleanupExpiredGiftCodes() {
        // Có thể thêm logic cleanup gift codes hết hạn
        // Hiện tại chưa có trường expiry date
    }
} 