# 📧 Hướng dẫn cấu hình Email - Tạp Hóa KeyT

## 🎯 Tổng quan

Hệ thống Tạp Hóa KeyT sử dụng Spring Mail để gửi email thông báo tự động cho người dùng. Các loại email bao gồm:

- ✅ Email chào mừng khi đăng ký
- 📦 Email xác nhận đơn hàng
- 🎁 Email gửi gift code
- ✅ Email thông báo đơn hàng đã xử lý
- ❌ Email thông báo đơn hàng bị từ chối
- 🔐 Email đặt lại mật khẩu

## ⚙️ Cấu hình Email

### 1. Cấu hình Gmail SMTP

Mở file `src/main/resources/application.properties` và cập nhật thông tin email:

```properties
# Mail Configuration (Gmail)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
```

### 2. Tạo App Password cho Gmail

1. **Bật 2FA cho Gmail:**
   - Vào Google Account Settings
   - Bật 2-Step Verification

2. **Tạo App Password:**
   - Vào Security > 2-Step Verification
   - Chọn "App passwords"
   - Tạo password cho "Mail"
   - Sử dụng password này trong cấu hình

### 3. Cấu hình cho các nhà cung cấp khác

#### Outlook/Hotmail:
```properties
spring.mail.host=smtp-mail.outlook.com
spring.mail.port=587
spring.mail.username=your-email@outlook.com
spring.mail.password=your-password
```

#### Yahoo:
```properties
spring.mail.host=smtp.mail.yahoo.com
spring.mail.port=587
spring.mail.username=your-email@yahoo.com
spring.mail.password=your-app-password
```

## 📧 Các loại Email được gửi

### 1. Email chào mừng (Registration Confirmation)
- **Khi gửi:** Sau khi đăng ký thành công
- **Template:** `email/registration-confirmation.html`
- **Nội dung:** Chào mừng, hướng dẫn sử dụng

### 2. Email xác nhận đơn hàng (Order Confirmation)
- **Khi gửi:** Sau khi đặt hàng thành công
- **Template:** `email/order-confirmation.html`
- **Nội dung:** Chi tiết đơn hàng, quy trình xử lý

### 3. Email gift code (Gift Code Delivery)
- **Khi gửi:** Khi admin gửi gift code
- **Template:** `email/gift-code.html`
- **Nội dung:** Gift code, hướng dẫn sử dụng

### 4. Email đơn hàng đã xử lý (Order Processed)
- **Khi gửi:** Khi admin cập nhật trạng thái COMPLETED
- **Template:** `email/order-processed.html`
- **Nội dung:** Thông báo đã xử lý, chuẩn bị gift code

### 5. Email đơn hàng bị từ chối (Order Rejected)
- **Khi gửi:** Khi admin cập nhật trạng thái CANCELLED
- **Template:** `email/order-rejected.html`
- **Nội dung:** Lý do từ chối, hướng dẫn hỗ trợ

### 6. Email đặt lại mật khẩu (Password Reset)
- **Khi gửi:** Khi yêu cầu đặt lại mật khẩu
- **Template:** `email/password-reset.html`
- **Nội dung:** Link đặt lại mật khẩu

## 🔧 Testing Email

### 1. Test với Gmail
```bash
# Cấu hình test
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-test-email@gmail.com
spring.mail.password=your-app-password
```

### 2. Test với MailHog (Local Development)
```properties
# Cấu hình MailHog
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
```

### 3. Test với Ethereal Email
```properties
# Cấu hình Ethereal (Test emails)
spring.mail.host=smtp.ethereal.email
spring.mail.port=587
spring.mail.username=your-ethereal-username
spring.mail.password=your-ethereal-password
```

## 🚀 Cách sử dụng

### 1. Gửi email đơn giản
```java
@Autowired
private EmailService emailService;

// Gửi email đơn giản
emailService.sendSimpleEmail("user@example.com", "Subject", "Content");
```

### 2. Gửi email HTML với template
```java
Map<String, Object> variables = Map.of(
    "userName", "Nguyễn Văn A",
    "orderNumber", "12345"
);

emailService.sendHtmlEmail(
    "user@example.com",
    "Xác nhận đơn hàng",
    "email/order-confirmation",
    variables
);
```

### 3. Gửi email tự động
```java
// Email chào mừng khi đăng ký
User user = userService.registerUser(email, password, fullName, phone);
// Email sẽ được gửi tự động trong UserService

// Email xác nhận đơn hàng
Order order = orderService.createOrder(order);
// Email sẽ được gửi tự động trong OrderService
```

## 🛠️ Troubleshooting

### 1. Lỗi Authentication
```
javax.mail.AuthenticationFailedException: 535 5.7.8 Username and Password not accepted
```
**Giải pháp:** Kiểm tra lại username/password, đảm bảo đã bật "Less secure app access" hoặc sử dụng App Password

### 2. Lỗi Connection Timeout
```
javax.mail.MessagingException: Could not connect to SMTP host
```
**Giải pháp:** Kiểm tra firewall, proxy, hoặc thử port khác (465 thay vì 587)

### 3. Lỗi Template không tìm thấy
```
org.thymeleaf.exceptions.TemplateInputException: Error resolving template
```
**Giải pháp:** Kiểm tra đường dẫn template trong thư mục `templates/email/`

## 📝 Lưu ý quan trọng

1. **Bảo mật:** Không commit thông tin email thật vào git
2. **Rate Limiting:** Gmail có giới hạn 500 emails/ngày
3. **Spam:** Đảm bảo nội dung email không bị coi là spam
4. **Testing:** Luôn test email trước khi deploy production
5. **Monitoring:** Theo dõi log để phát hiện lỗi gửi email

## 🔗 Tài liệu tham khảo

- [Spring Mail Documentation](https://docs.spring.io/spring-framework/reference/integration/email.html)
- [Gmail SMTP Settings](https://support.google.com/mail/answer/7126229)
- [Thymeleaf Email Templates](https://www.thymeleaf.org/doc/articles/springmail.html) 