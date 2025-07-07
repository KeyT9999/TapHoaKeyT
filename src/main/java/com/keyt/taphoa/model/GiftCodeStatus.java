package com.keyt.taphoa.model;

public enum GiftCodeStatus {
    AVAILABLE("Có sẵn"),
    RESERVED("Đã đặt"),
    USED("Đã sử dụng"),
    EXPIRED("Hết hạn");
    
    private final String displayName;
    
    GiftCodeStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 