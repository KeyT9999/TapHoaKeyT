package com.keyt.taphoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.keyt.taphoa.model.Category;
import com.keyt.taphoa.model.Product;
import com.keyt.taphoa.service.ProductService;

@Controller
public class HomeController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/")
    public String home(Model model) {
        List<Product> featuredProducts = productService.getFeaturedProducts();
        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("categories", Category.values());
        
        // Thống kê cho trang chủ
        model.addAttribute("totalProducts", productService.getAvailableProductsCount());
        
        return "index";
    }
    
    @GetMapping("/home")
    public String homeAlias(Model model) {
        return home(model);
    }
    
    @GetMapping("/products")
    public String products(Model model, 
                          @RequestParam(value = "category", required = false) String categoryParam,
                          @RequestParam(value = "search", required = false) String search) {
        
        List<Product> products;
        
        if (search != null && !search.trim().isEmpty()) {
            products = productService.searchProducts(search.trim());
            model.addAttribute("searchKeyword", search);
        } else if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                Category category = Category.valueOf(categoryParam.toUpperCase());
                products = productService.findProductsByCategory(category);
                model.addAttribute("selectedCategory", category);
            } catch (IllegalArgumentException e) {
                products = productService.findAvailableProducts();
            }
        } else {
            products = productService.findAvailableProducts();
        }
        
        model.addAttribute("products", products);
        model.addAttribute("categories", Category.values());
        
        return "products";
    }
    
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model) {
        try {
            Product product = productService.findById(id);
            if (!product.isAvailable()) {
                return "redirect:/products?error=product-not-available";
            }
            model.addAttribute("product", product);
            
            // Lấy sản phẩm liên quan (cùng category)
            List<Product> relatedProducts = productService.findProductsByCategory(product.getCategory());
            relatedProducts.removeIf(p -> p.getId().equals(id)); // Loại bỏ sản phẩm hiện tại
            model.addAttribute("relatedProducts", relatedProducts.size() > 4 ? relatedProducts.subList(0, 4) : relatedProducts);
            
            return "product-detail";
        } catch (Exception e) {
            return "redirect:/products?error=product-not-found";
        }
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
} 