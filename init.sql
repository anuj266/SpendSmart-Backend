-- Run this once against your LOCAL MySQL to create all databases.
-- Command:  mysql -u root -p < init.sql

CREATE DATABASE IF NOT EXISTS auth_db;
CREATE DATABASE IF NOT EXISTS expense_db;
CREATE DATABASE IF NOT EXISTS budget_db;
CREATE DATABASE IF NOT EXISTS income_db;
CREATE DATABASE IF NOT EXISTS notification_db;
CREATE DATABASE IF NOT EXISTS recurring_db;
CREATE DATABASE IF NOT EXISTS analytics_db;
CREATE DATABASE IF NOT EXISTS category_db;

-- Optional: create a dedicated app user (matches .env credentials)
-- CREATE USER IF NOT EXISTS 'spendsmart'@'localhost' IDENTIFIED BY 'spendsmart123';
-- GRANT ALL PRIVILEGES ON auth_db.*         TO 'spendsmart'@'localhost';
-- GRANT ALL PRIVILEGES ON expense_db.*      TO 'spendsmart'@'localhost';
-- GRANT ALL PRIVILEGES ON budget_db.*       TO 'spendsmart'@'localhost';
-- GRANT ALL PRIVILEGES ON income_db.*       TO 'spendsmart'@'localhost';
-- GRANT ALL PRIVILEGES ON notification_db.* TO 'spendsmart'@'localhost';
-- GRANT ALL PRIVILEGES ON recurring_db.*    TO 'spendsmart'@'localhost';
-- GRANT ALL PRIVILEGES ON analytics_db.*    TO 'spendsmart'@'localhost';
-- GRANT ALL PRIVILEGES ON category_db.*     TO 'spendsmart'@'localhost';
-- FLUSH PRIVILEGES;
