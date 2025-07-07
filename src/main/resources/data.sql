-- Sample data for TapHoa KeyT
-- This file will be executed automatically by Spring Boot

-- Insert sample categories
INSERT INTO categories (name, description) VALUES 
('Game Keys', 'Digital game keys for popular games'),
('Software Licenses', 'Software licenses and activation keys'),
('Gift Cards', 'Digital gift cards for various platforms'),
('Streaming Services', 'Subscription keys for streaming platforms')
ON CONFLICT DO NOTHING;

-- Insert sample products
INSERT INTO products (name, description, price, original_price, category_id, stock_quantity, image_url) VALUES 
('Steam Gift Card $50', 'Digital gift card for Steam platform', 50.00, 55.00, 1, 100, '/images/steam-card.jpg'),
('Netflix 1 Month', 'Netflix subscription for 1 month', 15.99, 18.99, 4, 50, '/images/netflix.jpg'),
('Spotify Premium 3 Months', 'Spotify Premium subscription for 3 months', 29.99, 35.99, 4, 75, '/images/spotify.jpg'),
('Microsoft Office 365', 'Microsoft Office 365 Personal 1 year', 69.99, 79.99, 2, 25, '/images/office365.jpg'),
('PlayStation Plus 12 Months', 'PlayStation Plus membership for 12 months', 59.99, 69.99, 1, 30, '/images/psplus.jpg'),
('Amazon Gift Card $100', 'Amazon gift card worth $100', 100.00, 110.00, 3, 200, '/images/amazon-card.jpg'),
('Adobe Creative Cloud', 'Adobe Creative Cloud annual subscription', 599.99, 699.99, 2, 10, '/images/adobe.jpg'),
('Disney+ 1 Year', 'Disney+ subscription for 1 year', 79.99, 89.99, 4, 40, '/images/disney.jpg')
ON CONFLICT DO NOTHING;

-- Insert admin user (password: admin123)
INSERT INTO users (username, email, password, full_name, role, enabled) VALUES 
('admin', 'admin@taphoakeyt.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'Administrator', 'ADMIN', true)
ON CONFLICT DO NOTHING;

-- Insert sample user (password: user123)
INSERT INTO users (username, email, password, full_name, role, enabled) VALUES 
('user1', 'user1@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Test User', 'USER', true)
ON CONFLICT DO NOTHING;

-- Insert sample coupons
INSERT INTO coupons (code, discount_percent, min_order_amount, max_usage, valid_from, valid_until) VALUES 
('WELCOME10', 10, 50.00, 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year'),
('SAVE20', 20, 100.00, 50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '6 months'),
('NEWUSER15', 15, 30.00, 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 year')
ON CONFLICT DO NOTHING;

-- Insert sample gift codes for products
INSERT INTO gift_codes (code, product_id, status) VALUES 
('STEAM50ABC123', 1, 'AVAILABLE'),
('STEAM50DEF456', 1, 'AVAILABLE'),
('STEAM50GHI789', 1, 'AVAILABLE'),
('NETFLIX1MTH001', 2, 'AVAILABLE'),
('NETFLIX1MTH002', 2, 'AVAILABLE'),
('SPOTIFY3MTH001', 3, 'AVAILABLE'),
('SPOTIFY3MTH002', 3, 'AVAILABLE'),
('OFFICE365YEAR001', 4, 'AVAILABLE'),
('PSPLUS12MTH001', 5, 'AVAILABLE'),
('AMAZON100CARD001', 6, 'AVAILABLE'),
('AMAZON100CARD002', 6, 'AVAILABLE'),
('ADOBE1YEAR001', 7, 'AVAILABLE'),
('DISNEY1YEAR001', 8, 'AVAILABLE')
ON CONFLICT DO NOTHING; 