# 🛒 Tạp Hóa KeyT - Ecommerce Website

Website thương mại điện tử chuyên bán các dịch vụ số như tài khoản Netflix, công cụ AI, khóa học và phần mềm.

## 🚀 Công nghệ sử dụng

- **Backend**: Java 17, Spring Boot 3.2.1
- **Security**: Spring Security + JWT
- **Database**: PostgreSQL (Production) / H2 (Development)
- **Frontend**: Thymeleaf + Bootstrap 5
- **Build Tool**: Maven

## 📁 Cấu trúc dự án

```
src/
├── main/
│   ├── java/com/keyt/taphoa/
│   │   ├── config/          # Cấu hình (Security, Data Init)
│   │   ├── controller/      # REST Controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── model/          # JPA Entities
│   │   ├── repository/     # JPA Repositories
│   │   ├── security/       # JWT Utils, Auth Filter
│   │   └── service/        # Business Logic
│   └── resources/
│       ├── static/         # CSS, JS, Images
│       ├── templates/      # Thymeleaf Templates
│       └── application*.properties
```

## 🏗️ Đã hoàn thành

### ✅ Giai đoạn 1: Khởi tạo dự án
- [x] Cấu trúc project Maven với Spring Boot
- [x] Dependencies: Web, JPA, Security, Thymeleaf, Mail, JWT
- [x] Cấu hình application.properties cho PostgreSQL và H2
- [x] Cấu trúc thư mục chuẩn

### ✅ Giai đoạn 2: Database & Entities
- [x] Entity User với UserDetails integration
- [x] Entity Product với đầy đủ thông tin
- [x] Entity Order và OrderItem
- [x] Enums: Role, Category, OrderStatus
- [x] JPA Repositories với custom queries

### ✅ Giai đoạn 3: Security & Authentication
- [x] JWT Utils (generate, validate tokens)
- [x] AuthTokenFilter để parse JWT
- [x] UserDetailsService implementation
- [x] WebSecurityConfig với phân quyền ADMIN/USER
- [x] Password encoding với BCrypt

### ✅ Giai đoạn 4: Business Logic
- [x] UserService (register, create admin, password management)
- [x] ProductService (CRUD, search, filter by category)
- [x] DataInitializer với sample data

### ✅ Giai đoạn 5: Frontend
- [x] Layout template với Bootstrap 5
- [x] Trang chủ với hero section, features, sản phẩm nổi bật
- [x] Trang danh sách sản phẩm với filter, search
- [x] Responsive design
- [x] Custom CSS styling

## 🔧 Cách chạy dự án

### Yêu cầu hệ thống
- Java 17+
- Maven 3.6+
- PostgreSQL (cho production) hoặc không cần gì (dùng H2 cho dev)

### Chạy với H2 Database (Development)
```bash
# Clone project
git clone <repository-url>
cd TapHoaKeyT

# Chạy với profile dev (H2 in-memory database)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Hoặc compile trước
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Chạy với PostgreSQL (Production)
```bash
# Tạo database
createdb keyt_shop

# Cập nhật application.properties với thông tin database
# Chạy ứng dụng
mvn spring-boot:run
```

## 🌐 Truy cập ứng dụng

- **Website**: http://localhost:8080
- **H2 Console** (dev only): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:keyt_shop`
  - Username: `sa`
  - Password: (để trống)

## 👥 Tài khoản mặc định

Khi ứng dụng khởi động, hệ thống sẽ tự động tạo:

### Admin
- **Email**: admin@taphoakeyt.com
- **Password**: admin123
- **Role**: ADMIN

### User
- **Email**: user@test.com
- **Password**: user123
- **Role**: USER

## 📊 Dữ liệu mẫu

Hệ thống sẽ tự động tạo 10 sản phẩm mẫu:

### 🤖 AI Tools (3 sản phẩm)
- ChatGPT Plus - 199,000 VNĐ
- Claude Pro - 169,000 VNĐ
- Midjourney - 299,000 VNĐ

### 📚 Courses (2 sản phẩm)
- Udemy Business - 149,000 VNĐ
- Skillshare Premium - 99,000 VNĐ

### 📺 Accounts (3 sản phẩm)
- Netflix Premium - 89,000 VNĐ
- Spotify Premium - 59,000 VNĐ
- YouTube Premium - 79,000 VNĐ

### 💻 Software (2 sản phẩm)
- Microsoft Office 365 - 199,000 VNĐ
- Adobe Creative Cloud - 399,000 VNĐ

## 🎯 Tính năng hiện tại

### 👤 User Features
- [x] Xem trang chủ với sản phẩm nổi bật
- [x] Xem danh sách sản phẩm
- [x] Lọc sản phẩm theo danh mục
- [x] Tìm kiếm sản phẩm
- [x] Xem chi tiết sản phẩm
- [x] Responsive design

### 🔐 Authentication (Cần implement)
- [ ] Đăng ký tài khoản
- [ ] Đăng nhập
- [ ] JWT token management

### 🛒 Shopping (Cần implement)
- [ ] Thêm vào giỏ hàng
- [ ] Quản lý giỏ hàng
- [ ] Đặt hàng
- [ ] Upload biên lai thanh toán

### 👨‍💼 Admin Features (Cần implement)
- [ ] Dashboard thống kê
- [ ] Quản lý sản phẩm (CRUD)
- [ ] Quản lý đơn hàng
- [ ] Gửi mã quà tặng

## 🔄 Các giai đoạn tiếp theo

### Giai đoạn 6: Authentication Controllers
- [ ] AuthController (register, login, refresh token)
- [ ] Login/Register templates
- [ ] Session management

### Giai đoạn 7: Shopping Cart & Orders
- [ ] CartController và CartService
- [ ] OrderController và OrderService
- [ ] Checkout process
- [ ] File upload cho biên lai

### Giai đoạn 8: Admin Panel
- [ ] AdminController
- [ ] Product management CRUD
- [ ] Order management
- [ ] Dashboard với thống kê

### Giai đoạn 9: Email & Notifications
- [ ] Spring Mail configuration
- [ ] Email templates
- [ ] Gift code delivery system

### Giai đoạn 10: Advanced Features
- [ ] Payment integration (Momo, ZaloPay)
- [ ] Dropbox integration
- [ ] Voucher system
- [ ] Analytics

## 🐛 Troubleshooting

### Lỗi không kết nối được database
```bash
# Kiểm tra PostgreSQL đang chạy
pg_ctl status

# Hoặc dùng H2 cho development
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Port 8080 đã được sử dụng
```bash
# Tìm process đang dùng port 8080
netstat -an | findstr ":8080"

# Kill process (Windows)
taskkill /F /PID <process_id>
```

## 📝 Notes

- Project sử dụng Spring Boot 3.x nên cần Java 17+
- Security config có warnings về deprecated methods, sẽ fix trong phiên bản tiếp theo
- Frontend hiện tại dùng Thymeleaf, có thể mở rộng thêm React sau này
- Database schema sẽ tự động tạo khi chạy ứng dụng lần đầu

## 🤝 Contribution

1. Fork project
2. Tạo feature branch
3. Commit changes
4. Push branch
5. Tạo Pull Request

## 📄 License

MIT License - see LICENSE file for details

---

**Tạp Hóa KeyT** - *Dịch vụ số chuyên nghiệp* 🚀 