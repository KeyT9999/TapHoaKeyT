package com.keyt.taphoa.config;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.keyt.taphoa.model.Category;
import com.keyt.taphoa.model.Product;
import com.keyt.taphoa.service.ProductService;
import com.keyt.taphoa.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProductService productService;
    
    @Override
    public void run(String... args) throws Exception {
        // Tạo admin user
        if (!userService.existsByEmail("admin@taphoakeyt.com")) {
            userService.createAdmin("admin@taphoakeyt.com", "admin123", "Admin KeyT");
            System.out.println("✅ Đã tạo tài khoản Admin: admin@taphoakeyt.com / admin123");
        }
        
        // Tạo test user
        if (!userService.existsByEmail("user@test.com")) {
            userService.registerUser("user@test.com", "user123", "Nguyễn Văn A", "0123456789");
            System.out.println("✅ Đã tạo tài khoản User: user@test.com / user123");
        }
        
        // Tạo sample products nếu chưa có
        if (productService.getTotalProducts() == 0) {
            createSampleProducts();
            System.out.println("✅ Đã tạo dữ liệu sản phẩm mẫu");
        }
    }
    
    private void createSampleProducts() {
        // AI Products
        Product chatGPT = new Product();
        chatGPT.setName("ChatGPT Plus");
        chatGPT.setCategory(Category.AI);
        chatGPT.setPrice(new BigDecimal("199000"));
        chatGPT.setDescription("Truy cập ChatGPT-4 với tốc độ nhanh hơn, ưu tiên trong giờ cao điểm");
        chatGPT.setDuration("1 tháng");
        chatGPT.setType("AI Chatbot");
        chatGPT.setGiftNote("Tài khoản được chia sẻ, bảo hành 30 ngày");
        chatGPT.setAvailable(true);
        productService.createProduct(chatGPT);
        
        Product claude = new Product();
        claude.setName("Claude Pro");
        claude.setCategory(Category.AI);
        claude.setPrice(new BigDecimal("169000"));
        claude.setDescription("Claude AI Pro với khả năng phân tích và viết văn bản chuyên nghiệp");
        claude.setDuration("1 tháng");
        claude.setType("AI Assistant");
        claude.setGiftNote("Tài khoản riêng, bảo hành 30 ngày");
        claude.setAvailable(true);
        productService.createProduct(claude);
        
        Product midjourney = new Product();
        midjourney.setName("Midjourney");
        midjourney.setCategory(Category.AI);
        midjourney.setPrice(new BigDecimal("299000"));
        midjourney.setDescription("Tạo hình ảnh AI chất lượng cao với Midjourney");
        midjourney.setDuration("1 tháng");
        midjourney.setType("AI Image Generator");
        midjourney.setGiftNote("Tài khoản Basic Plan, 200 ảnh/tháng");
        midjourney.setAvailable(true);
        productService.createProduct(midjourney);
        
        // Course Products
        Product udemy = new Product();
        udemy.setName("Udemy Business");
        udemy.setCategory(Category.COURSE);
        udemy.setPrice(new BigDecimal("149000"));
        udemy.setDescription("Truy cập hơn 20,000 khóa học kinh doanh và công nghệ");
        udemy.setDuration("3 tháng");
        udemy.setType("Online Learning");
        udemy.setGiftNote("Tài khoản chia sẻ, không giới hạn khóa học");
        udemy.setAvailable(true);
        productService.createProduct(udemy);
        
        Product skillshare = new Product();
        skillshare.setName("Skillshare Premium");
        skillshare.setCategory(Category.COURSE);
        skillshare.setPrice(new BigDecimal("99000"));
        skillshare.setDescription("Học sáng tạo, thiết kế và kinh doanh với Skillshare");
        skillshare.setDuration("2 tháng");
        skillshare.setType("Creative Learning");
        skillshare.setGiftNote("Tài khoản Premium, không quảng cáo");
        skillshare.setAvailable(true);
        productService.createProduct(skillshare);
        
        // Account Products
        Product netflix = new Product();
        netflix.setName("Netflix Premium");
        netflix.setCategory(Category.ACCOUNT);
        netflix.setPrice(new BigDecimal("89000"));
        netflix.setDescription("Xem phim 4K không giới hạn, 4 màn hình cùng lúc");
        netflix.setDuration("1 tháng");
        netflix.setType("Streaming Service");
        netflix.setGiftNote("Tài khoản chia sẻ, profile riêng");
        netflix.setAvailable(true);
        productService.createProduct(netflix);
        
        Product spotify = new Product();
        spotify.setName("Spotify Premium");
        spotify.setCategory(Category.ACCOUNT);
        spotify.setPrice(new BigDecimal("59000"));
        spotify.setDescription("Nghe nhạc không quảng cáo, tải offline");
        spotify.setDuration("1 tháng");
        spotify.setType("Music Streaming");
        spotify.setGiftNote("Tài khoản riêng, bảo hành 30 ngày");
        spotify.setAvailable(true);
        productService.createProduct(spotify);
        
        Product youtube = new Product();
        youtube.setName("YouTube Premium");
        youtube.setCategory(Category.ACCOUNT);
        youtube.setPrice(new BigDecimal("79000"));
        youtube.setDescription("Xem YouTube không quảng cáo, YouTube Music");
        youtube.setDuration("1 tháng");
        youtube.setType("Video Streaming");
        youtube.setGiftNote("Tài khoản chia sẻ, bảo hành 30 ngày");
        youtube.setAvailable(true);
        productService.createProduct(youtube);
        
        // Software Products
        Product office = new Product();
        office.setName("Microsoft Office 365");
        office.setCategory(Category.SOFTWARE);
        office.setPrice(new BigDecimal("199000"));
        office.setDescription("Word, Excel, PowerPoint, Outlook và 1TB OneDrive");
        office.setDuration("1 năm");
        office.setType("Office Suite");
        office.setGiftNote("Key bản quyền, kích hoạt vĩnh viễn");
        office.setAvailable(true);
        productService.createProduct(office);
        
        Product adobe = new Product();
        adobe.setName("Adobe Creative Cloud");
        adobe.setCategory(Category.SOFTWARE);
        adobe.setPrice(new BigDecimal("399000"));
        adobe.setDescription("Photoshop, Illustrator, Premiere Pro và toàn bộ ứng dụng Adobe");
        adobe.setDuration("1 tháng");
        adobe.setType("Creative Suite");
        adobe.setGiftNote("Tài khoản chia sẻ, đầy đủ tính năng");
        adobe.setAvailable(true);
        productService.createProduct(adobe);
    }
} 