package com.keyt.taphoa.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.keyt.taphoa.model.Category;
import com.keyt.taphoa.model.Product;
import com.keyt.taphoa.repository.ProductRepository;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }
    
    public Product updateProduct(Product product) {
        // Lấy bản ghi gốc từ database
        Product existingProduct = findById(product.getId());
        
        // Chỉ cập nhật các trường cho phép, giữ nguyên createdAt
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setAvailable(product.isAvailable());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        
        return productRepository.save(existingProduct);
    }
    
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
    }
    
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> findAvailableProducts() {
        return productRepository.findAvailableOrderByCreatedAtDesc();
    }
    
    public List<Product> findProductsByCategory(Category category) {
        return productRepository.findByCategoryAndAvailableTrue(category);
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }
    
    public List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetweenAndAvailableTrue(minPrice, maxPrice);
    }
    
    public List<Product> findProductsByCategoryAndPriceRange(Category category, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByCategoryAndPriceBetweenAndAvailableTrue(category, minPrice, maxPrice);
    }
    
    public void enableProduct(Long productId) {
        Product product = findById(productId);
        product.setAvailable(true);
        productRepository.save(product);
    }
    
    public void disableProduct(Long productId) {
        Product product = findById(productId);
        product.setAvailable(false);
        productRepository.save(product);
    }
    
    public void deleteProduct(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }
    
    public long getTotalProducts() {
        return productRepository.count();
    }
    
    public long getAvailableProductsCount() {
        return productRepository.findByAvailableTrue().size();
    }
    
    public long getProductCountByCategory(Category category) {
        return productRepository.countByCategoryAndAvailableTrue(category);
    }
    
    public List<Product> getLatestProducts(int limit) {
        List<Product> products = productRepository.findAvailableOrderByCreatedAtDesc();
        return products.size() > limit ? products.subList(0, limit) : products;
    }
    
    public List<Product> getFeaturedProducts() {
        // Có thể implement logic để lấy sản phẩm nổi bật
        // Ví dụ: sản phẩm bán chạy, sản phẩm mới, etc.
        return getLatestProducts(6);
    }
    
    public long countProducts() {
        return productRepository.count();
    }
    
    public long countAvailableProducts() {
        return productRepository.countByAvailableTrue();
    }
} 