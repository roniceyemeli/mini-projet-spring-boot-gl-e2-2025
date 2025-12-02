-- User database initialization
CREATE SCHEMA IF NOT EXISTS user_schema;

-- Create users table for user module
CREATE TABLE IF NOT EXISTS user_schema.users (
                                                 id SERIAL PRIMARY KEY,
                                                 full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    sign_up_date VARCHAR(50),
    sign_up_from VARCHAR(50),
    active BOOLEAN DEFAULT true,
    account_type VARCHAR(50),
    avatar TEXT,
    userId INT UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

-- Note: The self-referential relationship will be handled by application logic
-- If you want a foreign key constraint, uncomment and adjust:
-- ALTER TABLE user_schema.users
-- ADD CONSTRAINT fk_user_user FOREIGN KEY (user_id) REFERENCES user_schema.users(id);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_users_email ON user_schema.users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON user_schema.users(active);
CREATE INDEX IF NOT EXISTS idx_users_account_type ON user_schema.users(account_type);

-- Insert sample data (optional)
INSERT INTO user_schema.users
(full_name, email, password, sign_up_date, sign_up_from, active, account_type, avatar)
VALUES
    ('Admin User', 'admin@example.com', '$2a$10$YourHashedPasswordHere', '2024-01-01', 'WEB', true, 'ADMIN', 'default-avatar.png'),
    ('John Doe', 'john@example.com', '$2a$10$YourHashedPasswordHere', '2024-01-15', 'MOBILE', true, 'REGULAR', 'john-avatar.png')
    ON CONFLICT (email) DO NOTHING;