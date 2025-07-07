package com.keyt.taphoa.model;

public enum OrderStatus {
    PENDING("Chờ xử lý"),
    PROCESSING("Đang xử lý"),
    CONFIRMED("Đã xác nhận"),
    APPROVED("Đã duyệt"),
    COMPLETED("Hoàn thành"),
    REJECTED("Từ chối"),
    CANCELLED("Đã hủy");
    
    private final String displayName;
    
    OrderStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 