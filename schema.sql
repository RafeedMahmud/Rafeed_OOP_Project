-- إنشاء قاعدة بيانات لمشروع الرحالة
CREATE DATABASE al_rahhala_db;

-- اختيار قاعدة البيانات هذه للاستخدام
USE al_rahhala_db;

-- إنشاء جدول المستخدمين (Admin + Employee)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL  -- مثلا 'ADMIN' أو 'EMPLOYEE'
);

-- إنشاء جدول الشحنات
CREATE TABLE shipments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tracking_code VARCHAR(50) NOT NULL UNIQUE,

    sender_name VARCHAR(100) NOT NULL,
    sender_phone VARCHAR(20),
    sender_address VARCHAR(200),

    receiver_name VARCHAR(100) NOT NULL,
    receiver_phone VARCHAR(20),
    receiver_address VARCHAR(200),

    weight DOUBLE NOT NULL,
    status VARCHAR(30) NOT NULL,       -- 'REGISTERED', 'DELIVERED', ...
    created_at DATETIME NOT NULL,
    delivered_at DATETIME NULL,

    created_by_user_id INT,            -- الموظف الذي أنشأ الشحنة
    FOREIGN KEY (created_by_user_id) REFERENCES users(id)
);
