-- =====================================================
-- TAP HOA KEYT DATABASE - MICROSOFT SQL SERVER VERSION
-- Complete SQL file for TapHoa KeyT project
-- =====================================================

-- Drop existing tables if they exist (for clean setup)
IF OBJECT_ID('otps', 'U') IS NOT NULL DROP TABLE otps;
IF OBJECT_ID('password_reset_tokens', 'U') IS NOT NULL DROP TABLE password_reset_tokens;
IF OBJECT_ID('notifications', 'U') IS NOT NULL DROP TABLE notifications;
IF OBJECT_ID('gift_codes', 'U') IS NOT NULL DROP TABLE gift_codes;
IF OBJECT_ID('coupons', 'U') IS NOT NULL DROP TABLE coupons;
IF OBJECT_ID('reviews', 'U') IS NOT NULL DROP TABLE reviews;
IF OBJECT_ID('order_items', 'U') IS NOT NULL DROP TABLE order_items;
IF OBJECT_ID('orders', 'U') IS NOT NULL DROP TABLE orders;
IF OBJECT_ID('products', 'U') IS NOT NULL DROP TABLE products;
IF OBJECT_ID('categories', 'U') IS NOT NULL DROP TABLE categories;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;

-- =====================================================
-- SCHEMA CREATION
-- =====================================================

-- Users table
CREATE TABLE users (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) UNIQUE NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    password NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(100),
    phone NVARCHAR(20),
    address NVARCHAR(MAX),
    role NVARCHAR(20) DEFAULT 'USER',
    enabled BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE()
);

-- Categories table
CREATE TABLE categories (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(100) NOT NULL,
    description NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Products table
CREATE TABLE products (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(200) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    image_url NVARCHAR(500),
    category_id BIGINT,
    stock_quantity INT DEFAULT 0,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Orders table
CREATE TABLE orders (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    total_amount DECIMAL(10,2) NOT NULL,
    status NVARCHAR(20) DEFAULT 'PENDING',
    payment_method NVARCHAR(50),
    shipping_address NVARCHAR(MAX),
    notes NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Order Items table
CREATE TABLE order_items (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id BIGINT,
    product_id BIGINT,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Reviews table
CREATE TABLE reviews (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    product_id BIGINT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    status NVARCHAR(20) DEFAULT 'PENDING',
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Gift Codes table
CREATE TABLE gift_codes (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(50) UNIQUE NOT NULL,
    product_id BIGINT,
    user_id BIGINT,
    order_id BIGINT,
    status NVARCHAR(20) DEFAULT 'AVAILABLE',
    created_at DATETIME2 DEFAULT GETDATE(),
    used_at DATETIME2,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- Coupons table
CREATE TABLE coupons (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    code NVARCHAR(50) UNIQUE NOT NULL,
    discount_percent INT,
    discount_amount DECIMAL(10,2),
    min_order_amount DECIMAL(10,2),
    max_usage INT,
    used_count INT DEFAULT 0,
    valid_from DATETIME2,
    valid_until DATETIME2,
    is_active BIT DEFAULT 1,
    created_at DATETIME2 DEFAULT GETDATE()
);

-- Notifications table
CREATE TABLE notifications (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    title NVARCHAR(200) NOT NULL,
    message NVARCHAR(MAX) NOT NULL,
    is_read BIT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Password Reset Tokens table
CREATE TABLE password_reset_tokens (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    token NVARCHAR(255) UNIQUE NOT NULL,
    expiry_date DATETIME2 NOT NULL,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- OTP table
CREATE TABLE otps (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id BIGINT,
    otp_code NVARCHAR(6) NOT NULL,
    expiry_date DATETIME2 NOT NULL,
    is_used BIT DEFAULT 0,
    created_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_reviews_product ON reviews(product_id);
CREATE INDEX idx_gift_codes_code ON gift_codes(code);
CREATE INDEX idx_gift_codes_product ON gift_codes(product_id);
CREATE INDEX idx_coupons_code ON coupons(code);

-- =====================================================
-- SAMPLE DATA INSERTION
-- =====================================================

-- Insert sample categories
INSERT INTO categories (name, description) VALUES 
('Game Keys', 'Digital game keys for popular games'),
('Software Licenses', 'Software licenses and activation keys'),
('Gift Cards', 'Digital gift cards for various platforms'),
('Streaming Services', 'Subscription keys for streaming platforms');

-- Insert sample products
INSERT INTO products (name, description, price, original_price, category_id, stock_quantity, image_url) VALUES 
('Steam Gift Card $50', 'Digital gift card for Steam platform', 50.00, 55.00, 1, 100, '/images/steam-card.jpg'),
('Netflix 1 Month', 'Netflix subscription for 1 month', 15.99, 18.99, 4, 50, '/images/netflix.jpg'),
('Spotify Premium 3 Months', 'Spotify Premium subscription for 3 months', 29.99, 35.99, 4, 75, '/images/spotify.jpg'),
('Microsoft Office 365', 'Microsoft Office 365 Personal 1 year', 69.99, 79.99, 2, 25, '/images/office365.jpg'),
('PlayStation Plus 12 Months', 'PlayStation Plus membership for 12 months', 59.99, 69.99, 1, 30, '/images/psplus.jpg'),
('Amazon Gift Card $100', 'Amazon gift card worth $100', 100.00, 110.00, 3, 200, '/images/amazon-card.jpg'),
('Adobe Creative Cloud', 'Adobe Creative Cloud annual subscription', 599.99, 699.99, 2, 10, '/images/adobe.jpg'),
('Disney+ 1 Year', 'Disney+ subscription for 1 year', 79.99, 89.99, 4, 40, '/images/disney.jpg'),
('Xbox Game Pass Ultimate 3 Months', 'Xbox Game Pass Ultimate for 3 months', 44.99, 49.99, 1, 60, '/images/xbox-gamepass.jpg'),
('YouTube Premium 1 Year', 'YouTube Premium subscription for 1 year', 119.99, 139.99, 4, 35, '/images/youtube-premium.jpg');

-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, full_name, role, enabled) VALUES 
('admin', 'admin@taphoakeyt.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Administrator', 'ADMIN', 1);

-- Insert sample users (password: user123)
INSERT INTO users (username, email, password, full_name, role, enabled) VALUES 
('user1', 'user1@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Test User 1', 'USER', 1),
('user2', 'user2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Test User 2', 'USER', 1),
('john_doe', 'john@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'John Doe', 'USER', 1);

-- Insert sample coupons
INSERT INTO coupons (code, discount_percent, min_order_amount, max_usage, valid_from, valid_until) VALUES 
('WELCOME10', 10, 50.00, 100, GETDATE(), DATEADD(YEAR, 1, GETDATE())),
('SAVE20', 20, 100.00, 50, GETDATE(), DATEADD(MONTH, 6, GETDATE())),
('NEWUSER15', 15, 30.00, 200, GETDATE(), DATEADD(YEAR, 1, GETDATE())),
('SUMMER25', 25, 150.00, 30, GETDATE(), DATEADD(MONTH, 3, GETDATE())),
('FLASH30', 30, 200.00, 20, GETDATE(), DATEADD(MONTH, 1, GETDATE()));

-- Insert sample gift codes for products
INSERT INTO gift_codes (code, product_id, status) VALUES 
-- Steam Gift Cards
('STEAM50ABC123', 1, 'AVAILABLE'),
('STEAM50DEF456', 1, 'AVAILABLE'),
('STEAM50GHI789', 1, 'AVAILABLE'),
('STEAM50JKL012', 1, 'AVAILABLE'),
('STEAM50MNO345', 1, 'AVAILABLE'),

-- Netflix Subscriptions
('NETFLIX1MTH001', 2, 'AVAILABLE'),
('NETFLIX1MTH002', 2, 'AVAILABLE'),
('NETFLIX1MTH003', 2, 'AVAILABLE'),
('NETFLIX1MTH004', 2, 'AVAILABLE'),
('NETFLIX1MTH005', 2, 'AVAILABLE'),

-- Spotify Premium
('SPOTIFY3MTH001', 3, 'AVAILABLE'),
('SPOTIFY3MTH002', 3, 'AVAILABLE'),
('SPOTIFY3MTH003', 3, 'AVAILABLE'),
('SPOTIFY3MTH004', 3, 'AVAILABLE'),
('SPOTIFY3MTH005', 3, 'AVAILABLE'),

-- Microsoft Office 365
('OFFICE365YEAR001', 4, 'AVAILABLE'),
('OFFICE365YEAR002', 4, 'AVAILABLE'),
('OFFICE365YEAR003', 4, 'AVAILABLE'),
('OFFICE365YEAR004', 4, 'AVAILABLE'),
('OFFICE365YEAR005', 4, 'AVAILABLE'),

-- PlayStation Plus
('PSPLUS12MTH001', 5, 'AVAILABLE'),
('PSPLUS12MTH002', 5, 'AVAILABLE'),
('PSPLUS12MTH003', 5, 'AVAILABLE'),
('PSPLUS12MTH004', 5, 'AVAILABLE'),
('PSPLUS12MTH005', 5, 'AVAILABLE'),

-- Amazon Gift Cards
('AMAZON100CARD001', 6, 'AVAILABLE'),
('AMAZON100CARD002', 6, 'AVAILABLE'),
('AMAZON100CARD003', 6, 'AVAILABLE'),
('AMAZON100CARD004', 6, 'AVAILABLE'),
('AMAZON100CARD005', 6, 'AVAILABLE'),

-- Adobe Creative Cloud
('ADOBE1YEAR001', 7, 'AVAILABLE'),
('ADOBE1YEAR002', 7, 'AVAILABLE'),
('ADOBE1YEAR003', 7, 'AVAILABLE'),
('ADOBE1YEAR004', 7, 'AVAILABLE'),
('ADOBE1YEAR005', 7, 'AVAILABLE'),

-- Disney+
('DISNEY1YEAR001', 8, 'AVAILABLE'),
('DISNEY1YEAR002', 8, 'AVAILABLE'),
('DISNEY1YEAR003', 8, 'AVAILABLE'),
('DISNEY1YEAR004', 8, 'AVAILABLE'),
('DISNEY1YEAR005', 8, 'AVAILABLE'),

-- Xbox Game Pass
('XBOX3MTH001', 9, 'AVAILABLE'),
('XBOX3MTH002', 9, 'AVAILABLE'),
('XBOX3MTH003', 9, 'AVAILABLE'),
('XBOX3MTH004', 9, 'AVAILABLE'),
('XBOX3MTH005', 9, 'AVAILABLE'),

-- YouTube Premium
('YOUTUBE1YEAR001', 10, 'AVAILABLE'),
('YOUTUBE1YEAR002', 10, 'AVAILABLE'),
('YOUTUBE1YEAR003', 10, 'AVAILABLE'),
('YOUTUBE1YEAR004', 10, 'AVAILABLE'),
('YOUTUBE1YEAR005', 10, 'AVAILABLE');

-- Insert sample orders
INSERT INTO orders (user_id, total_amount, status, payment_method, shipping_address, notes) VALUES 
(2, 65.98, 'COMPLETED', 'CREDIT_CARD', '123 Main St, City, Country', 'First order'),
(2, 129.98, 'PENDING', 'PAYPAL', '123 Main St, City, Country', 'Second order'),
(3, 79.99, 'COMPLETED', 'BANK_TRANSFER', '456 Oak Ave, Town, Country', 'Office subscription'),
(4, 159.98, 'PROCESSING', 'CREDIT_CARD', '789 Pine Rd, Village, Country', 'Gaming subscription');

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, quantity, price) VALUES 
(1, 2, 2, 15.99),  -- 2x Netflix
(1, 3, 1, 29.99),  -- 1x Spotify
(2, 1, 1, 50.00),  -- 1x Steam Card
(2, 5, 1, 59.99),  -- 1x PS Plus
(2, 6, 1, 100.00), -- 1x Amazon Card
(3, 4, 1, 69.99),  -- 1x Office 365
(3, 8, 1, 79.99),  -- 1x Disney+
(4, 5, 1, 59.99),  -- 1x PS Plus
(4, 9, 2, 44.99);  -- 2x Xbox Game Pass

-- Insert sample reviews
INSERT INTO reviews (user_id, product_id, rating, comment, status) VALUES 
(2, 2, 5, 'Great service, Netflix key worked perfectly!', 'APPROVED'),
(2, 3, 4, 'Spotify Premium is working well, good price', 'APPROVED'),
(3, 4, 5, 'Office 365 activation was smooth', 'APPROVED'),
(4, 5, 4, 'PS Plus key activated successfully', 'APPROVED'),
(2, 6, 5, 'Amazon gift card received instantly', 'APPROVED');

-- Insert sample notifications
INSERT INTO notifications (user_id, title, message) VALUES 
(2, 'Order Confirmed', 'Your order #1 has been confirmed and is being processed.'),
(2, 'Order Shipped', 'Your order #1 has been shipped and gift codes are available.'),
(3, 'Welcome', 'Welcome to TapHoa KeyT! Enjoy your shopping experience.'),
(4, 'Order Status Update', 'Your order #4 status has been updated to PROCESSING.');

-- =====================================================
-- DATABASE COMPLETED
-- =====================================================

-- Display summary
SELECT 'Database setup completed successfully!' as status;
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as total_products FROM products;
SELECT COUNT(*) as total_categories FROM categories;
SELECT COUNT(*) as total_orders FROM orders;
SELECT COUNT(*) as total_gift_codes FROM gift_codes; 