package com.keyt.taphoa.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.keyt.taphoa.model.Category;
import com.keyt.taphoa.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByCategory(Category category);
    
    List<Product> findByAvailableTrue();
    
    List<Product> findByAvailableFalse();
    
    List<Product> findByCategoryAndAvailableTrue(Category category);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.type LIKE %:keyword%")
    List<Product> searchByKeyword(String keyword);
    
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.available = true")
    List<Product> findByPriceBetweenAndAvailableTrue(BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price BETWEEN :minPrice AND :maxPrice AND p.available = true")
    List<Product> findByCategoryAndPriceBetweenAndAvailableTrue(Category category, BigDecimal minPrice, BigDecimal maxPrice);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category AND p.available = true")
    long countByCategoryAndAvailableTrue(Category category);
    
    @Query("SELECT p FROM Product p ORDER BY p.createdAt DESC")
    List<Product> findAllOrderByCreatedAtDesc();
    
    @Query("SELECT p FROM Product p WHERE p.available = true ORDER BY p.createdAt DESC")
    List<Product> findAvailableOrderByCreatedAtDesc();
    
    @Query("SELECT p FROM Product p WHERE p.available = true ORDER BY p.createdAt DESC")
    List<Product> findAvailableProducts();
    
    long countByAvailableTrue();
} 