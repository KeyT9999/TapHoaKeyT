package com.keyt.taphoa.model;

public enum Category {
    AI("Công cụ AI"),
    COURSE("Khóa học"),
    ACCOUNT("Tài khoản số"),
    SOFTWARE("Phần mềm"),
    OTHER("Khác");
    
    private final String displayName;
    
    Category(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
} 