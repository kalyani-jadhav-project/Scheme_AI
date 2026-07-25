-- ==========================================
-- KrushiMitra AI - Database Initialization
-- ==========================================

CREATE DATABASE IF NOT EXISTS krushimitra_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE krushimitra_db;

-- Roles will be created by DataInitializer on app startup
-- Admin user will be created by DataInitializer on app startup
-- Sample schemes will be created by DataInitializer on app startup

-- Optional: Pre-populate sample data below
-- The Hibernate ddl-auto=update will create all tables automatically

-- Sample Additional Farmers (optional)
-- INSERT INTO users (username, email, password, full_name, phone_number, active, email_verified)
-- VALUES ('ramesh_k', 'ramesh@example.com', '$2a$10$...', 'Ramesh Kumar', '9876543210', 1, 1);

-- Verify setup
SELECT 'KrushiMitra AI Database Ready!' AS status;
