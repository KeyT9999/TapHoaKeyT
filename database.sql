-- =====================================================
-- TAP HOA KEYT DATABASE
-- Complete SQL file for TapHoa KeyT project
-- =====================================================

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS otps CASCADE;
DROP TABLE IF EXISTS password_reset_tokens CASCADE;
DROP TABLE IF EXISTS notifications CASCADE;
DROP TABLE IF EXISTS gift_codes CASCADE;
DROP TABLE IF EXISTS coupons CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS order_items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- =====================================================
-- SCHEMA CREATION
-- =====================================================

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone VARCHAR(20),
    address TEXT,
    role VARCHAR(20) DEFAULT 'USER',
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    original_price DECIMAL(10,2),
    image_url VARCHAR(500),
    category_id BIGINT REFERENCES categories(id),
    stock_quantity INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    total_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_method VARCHAR(50),
    shipping_address TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order Items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    product_id BIGINT REFERENCES products(id),
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Reviews table
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    product_id BIGINT REFERENCES products(id),
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Gift Codes table
CREATE TABLE gift_codes (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    product_id BIGINT REFERENCES products(id),
    user_id BIGINT REFERENCES users(id),
    order_id BIGINT REFERENCES orders(id),
    status VARCHAR(20) DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    used_at TIMESTAMP
);

-- Coupons table
CREATE TABLE coupons (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_percent INTEGER,
    discount_amount DECIMAL(10,2),
    min_order_amount DECIMAL(10,2),
    max_usage INTEGER,
    used_count INTEGER DEFAULT 0,
    valid_from TIMESTAMP,
    valid_until TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Notifications table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Password Reset Tokens table
CREATE TABLE password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    token VARCHAR(255) UNIQUE NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- OTP table
CREATE TABLE otps (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    otp_code VARCHAR(6) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
('admin', 'admin@taphoakeyt.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Administrator', 'ADMIN', true);

-- Insert sample users (password: user123)
INSERT INTO users (username, email, password, full_name, role, enabled) VALUES 
('user1', 'user1@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Test User 1', 'USER', true),
('user2', 'user2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Test User 2', 'USER', true),
('john_doe', 'john@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'John Doe', 'USER', true);

-- Insert sample coupons
INSERT INTO coupons (code, discount_percent, min_order_amount, max_usage, valid_from, valid_until) VALUES 
('WELCOME10', 10, 50.00, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year'),
('SAVE20', 20, 100.00, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '6 months'),
('NEWUSER15', 15, 30.00, 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year'),
('SUMMER25', 25, 150.00, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3 months'),
('FLASH30', 30, 200.00, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 month');

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