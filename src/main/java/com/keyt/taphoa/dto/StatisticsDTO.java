package com.keyt.taphoa.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class StatisticsDTO {
    
    // Tổng quan
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private Long totalUsers;
    private Long totalProducts;
    
    // Doanh thu theo thời gian
    private Map<LocalDate, BigDecimal> revenueByDate;
    private Map<String, BigDecimal> revenueByMonth;
    private Map<Integer, BigDecimal> revenueByYear;
    
    // Thống kê sản phẩm
    private List<ProductStats> topSellingProducts;
    private Map<String, Long> ordersByCategory;
    private Map<String, BigDecimal> revenueByCategory;
    
    // Thống kê người dùng
    private List<UserStats> topSpendingUsers;
    private Map<String, Long> userRegistrationsByMonth;
    
    // Thống kê đơn hàng
    private Map<String, Long> ordersByStatus;
    private BigDecimal averageOrderValue;
    private Long pendingOrders;
    private Long completedOrders;
    
    // Thống kê theo thời gian
    private String periodType; // daily, weekly, monthly, yearly
    private LocalDate startDate;
    private LocalDate endDate;
    
    public static class ProductStats {
        private Long productId;
        private String productName;
        private Long quantitySold;
        private BigDecimal revenue;
        private BigDecimal averageRating;
        
        public ProductStats() {}
        
        public ProductStats(Long productId, String productName, Long quantitySold, BigDecimal revenue) {
            this.productId = productId;
            this.productName = productName;
            this.quantitySold = quantitySold;
            this.revenue = revenue;
        }
        
        // Getters and Setters
        public Long getProductId() {
            return productId;
        }
        
        public void setProductId(Long productId) {
            this.productId = productId;
        }
        
        public String getProductName() {
            return productName;
        }
        
        public void setProductName(String productName) {
            this.productName = productName;
        }
        
        public Long getQuantitySold() {
            return quantitySold;
        }
        
        public void setQuantitySold(Long quantitySold) {
            this.quantitySold = quantitySold;
        }
        
        public BigDecimal getRevenue() {
            return revenue;
        }
        
        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
        
        public BigDecimal getAverageRating() {
            return averageRating;
        }
        
        public void setAverageRating(BigDecimal averageRating) {
            this.averageRating = averageRating;
        }
    }
    
    public static class UserStats {
        private Long userId;
        private String userName;
        private String userEmail;
        private Long orderCount;
        private BigDecimal totalSpent;
        private LocalDate lastOrderDate;
        
        public UserStats() {}
        
        public UserStats(Long userId, String userName, String userEmail, Long orderCount, BigDecimal totalSpent) {
            this.userId = userId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.orderCount = orderCount;
            this.totalSpent = totalSpent;
        }
        
        // Getters and Setters
        public Long getUserId() {
            return userId;
        }
        
        public void setUserId(Long userId) {
            this.userId = userId;
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getUserEmail() {
            return userEmail;
        }
        
        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }
        
        public Long getOrderCount() {
            return orderCount;
        }
        
        public void setOrderCount(Long orderCount) {
            this.orderCount = orderCount;
        }
        
        public BigDecimal getTotalSpent() {
            return totalSpent;
        }
        
        public void setTotalSpent(BigDecimal totalSpent) {
            this.totalSpent = totalSpent;
        }
        
        public LocalDate getLastOrderDate() {
            return lastOrderDate;
        }
        
        public void setLastOrderDate(LocalDate lastOrderDate) {
            this.lastOrderDate = lastOrderDate;
        }
    }
    
    // Getters and Setters
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    
    public Long getTotalOrders() {
        return totalOrders;
    }
    
    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }
    
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public Long getTotalProducts() {
        return totalProducts;
    }
    
    public void setTotalProducts(Long totalProducts) {
        this.totalProducts = totalProducts;
    }
    
    public Map<LocalDate, BigDecimal> getRevenueByDate() {
        return revenueByDate;
    }
    
    public void setRevenueByDate(Map<LocalDate, BigDecimal> revenueByDate) {
        this.revenueByDate = revenueByDate;
    }
    
    public Map<String, BigDecimal> getRevenueByMonth() {
        return revenueByMonth;
    }
    
    public void setRevenueByMonth(Map<String, BigDecimal> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }
    
    public Map<Integer, BigDecimal> getRevenueByYear() {
        return revenueByYear;
    }
    
    public void setRevenueByYear(Map<Integer, BigDecimal> revenueByYear) {
        this.revenueByYear = revenueByYear;
    }
    
    public List<ProductStats> getTopSellingProducts() {
        return topSellingProducts;
    }
    
    public void setTopSellingProducts(List<ProductStats> topSellingProducts) {
        this.topSellingProducts = topSellingProducts;
    }
    
    public Map<String, Long> getOrdersByCategory() {
        return ordersByCategory;
    }
    
    public void setOrdersByCategory(Map<String, Long> ordersByCategory) {
        this.ordersByCategory = ordersByCategory;
    }
    
    public Map<String, BigDecimal> getRevenueByCategory() {
        return revenueByCategory;
    }
    
    public void setRevenueByCategory(Map<String, BigDecimal> revenueByCategory) {
        this.revenueByCategory = revenueByCategory;
    }
    
    public List<UserStats> getTopSpendingUsers() {
        return topSpendingUsers;
    }
    
    public void setTopSpendingUsers(List<UserStats> topSpendingUsers) {
        this.topSpendingUsers = topSpendingUsers;
    }
    
    public Map<String, Long> getUserRegistrationsByMonth() {
        return userRegistrationsByMonth;
    }
    
    public void setUserRegistrationsByMonth(Map<String, Long> userRegistrationsByMonth) {
        this.userRegistrationsByMonth = userRegistrationsByMonth;
    }
    
    public Map<String, Long> getOrdersByStatus() {
        return ordersByStatus;
    }
    
    public void setOrdersByStatus(Map<String, Long> ordersByStatus) {
        this.ordersByStatus = ordersByStatus;
    }
    
    public BigDecimal getAverageOrderValue() {
        return averageOrderValue;
    }
    
    public void setAverageOrderValue(BigDecimal averageOrderValue) {
        this.averageOrderValue = averageOrderValue;
    }
    
    public Long getPendingOrders() {
        return pendingOrders;
    }
    
    public void setPendingOrders(Long pendingOrders) {
        this.pendingOrders = pendingOrders;
    }
    
    public Long getCompletedOrders() {
        return completedOrders;
    }
    
    public void setCompletedOrders(Long completedOrders) {
        this.completedOrders = completedOrders;
    }
    
    public String getPeriodType() {
        return periodType;
    }
    
    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
} 