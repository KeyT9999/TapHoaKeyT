package com.keyt.taphoa.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.keyt.taphoa.model.Category;
import com.keyt.taphoa.model.Order;
import com.keyt.taphoa.model.OrderStatus;
import com.keyt.taphoa.model.Product;
import com.keyt.taphoa.model.Role;
import com.keyt.taphoa.model.User;
import com.keyt.taphoa.service.OrderService;
import com.keyt.taphoa.service.ProductService;
import com.keyt.taphoa.service.UserService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("totalOrders", orderService.getTotalOrders());
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("pendingOrders", orderService.getOrderCountByStatus(OrderStatus.PENDING));
        model.addAttribute("completedOrders", orderService.getOrderCountByStatus(OrderStatus.COMPLETED));
        model.addAttribute("adminCount", userService.getUserCountByRole(Role.ROLE_ADMIN));
        model.addAttribute("userCount", userService.getUserCountByRole(Role.ROLE_USER));
        return "admin/dashboard";
    }

    // Quản lý sản phẩm
    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("categories", Category.values());
        return "admin/products/list";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", Category.values());
        return "admin/products/form";
    }

    @PostMapping("/products")
    public String createProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được tạo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/edit")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", Category.values());
        return "admin/products/form";
    }

    @PostMapping("/products/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, 
                              RedirectAttributes redirectAttributes) {
        try {
            product.setId(id);
            productService.updateProduct(product);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "Sản phẩm đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/toggle")
    @ResponseBody
    public String toggleProductStatus(@PathVariable Long id) {
        try {
            Product product = productService.findById(id);
            product.setAvailable(!product.isAvailable());
            productService.updateProduct(product);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    // Quản lý đơn hàng
    @GetMapping("/orders")
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        model.addAttribute("orderStatuses", OrderStatus.values());
        return "admin/orders/list";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        model.addAttribute("orderStatuses", OrderStatus.values());
        return "admin/orders/detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status, 
                                  RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật trạng thái đơn hàng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/orders/{id}/gift-delivered")
    public String markGiftDelivered(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.findById(id);
            order.setGiftDelivered(true);
            orderService.updateOrder(order);
            redirectAttributes.addFlashAttribute("success", "Đã đánh dấu gift code đã giao!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/orders/{id}/admin-note")
    public String updateAdminNote(@PathVariable Long id, @RequestParam String adminNote, 
                                RedirectAttributes redirectAttributes) {
        try {
            Order order = orderService.findById(id);
            order.setAdminNote(adminNote);
            orderService.updateOrder(order);
            redirectAttributes.addFlashAttribute("success", "Đã cập nhật ghi chú!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    @PostMapping("/orders/{id}/send-gift-code")
    public String sendGiftCode(@PathVariable Long id, @RequestParam String giftCode, 
                              RedirectAttributes redirectAttributes) {
        try {
            orderService.sendGiftCodeForOrder(id, giftCode);
            redirectAttributes.addFlashAttribute("success", "Đã gửi gift code qua email!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    // Quản lý người dùng
    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", Role.values());
        return "admin/users/list";
    }

    @PostMapping("/users/{id}/toggle-status")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id);
            user.setEnabled(!user.isEnabled());
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", 
                "Đã " + (user.isEnabled() ? "kích hoạt" : "vô hiệu hóa") + " tài khoản!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/change-role")
    public String changeUserRole(@PathVariable Long id, @RequestParam Role role, 
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id);
            user.setRole(role);
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "Đã thay đổi vai trò người dùng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findById(id);
            if (user.getRole() == Role.ROLE_ADMIN) {
                redirectAttributes.addFlashAttribute("error", "Không thể xóa tài khoản admin!");
                return "redirect:/admin/users";
            }
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa người dùng!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // Thống kê chi tiết
    @GetMapping("/statistics")
    public String statistics(Model model) {
        // Thống kê đơn hàng theo trạng thái
        model.addAttribute("pendingOrders", orderService.getOrderCountByStatus(OrderStatus.PENDING));
        model.addAttribute("confirmedOrders", orderService.getOrderCountByStatus(OrderStatus.CONFIRMED));
        model.addAttribute("completedOrders", orderService.getOrderCountByStatus(OrderStatus.COMPLETED));
        model.addAttribute("cancelledOrders", orderService.getOrderCountByStatus(OrderStatus.CANCELLED));
        
        // Thống kê doanh thu
        model.addAttribute("totalRevenue", orderService.getTotalRevenue());
        model.addAttribute("monthlyRevenue", orderService.getRevenueByPeriod(
            LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0),
            LocalDateTime.now()
        ));
        
        // Thống kê người dùng
        model.addAttribute("totalUsers", userService.getTotalUsers());
        model.addAttribute("adminUsers", userService.getUserCountByRole(Role.ROLE_ADMIN));
        model.addAttribute("regularUsers", userService.getUserCountByRole(Role.ROLE_USER));
        
        // Thống kê sản phẩm
        model.addAttribute("totalProducts", productService.countProducts());
        model.addAttribute("availableProducts", productService.countAvailableProducts());
        
        return "admin/statistics";
    }
} 