-- Test Database Connection Script
-- Run this script to verify database connection and table creation

-- Check if database exists
SELECT current_database() as current_db;

-- List all tables
SELECT table_name 
FROM information_schema.tables 
WHERE table_schema = 'public'
ORDER BY table_name;

-- Check users table
SELECT COUNT(*) as user_count FROM users;

-- Check products table
SELECT COUNT(*) as product_count FROM products;

-- Check categories table
SELECT COUNT(*) as category_count FROM categories;

-- Check gift codes table
SELECT COUNT(*) as gift_code_count FROM gift_codes;

-- Check coupons table
SELECT COUNT(*) as coupon_count FROM coupons;

-- Sample data verification
SELECT 'Users:' as info, username, email, role FROM users LIMIT 5;
SELECT 'Products:' as info, name, price, stock_quantity FROM products LIMIT 5;
SELECT 'Categories:' as info, name, description FROM categories;
SELECT 'Gift Codes:' as info, code, status FROM gift_codes LIMIT 5; 