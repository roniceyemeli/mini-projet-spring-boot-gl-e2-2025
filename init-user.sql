-- Create schema
CREATE SCHEMA IF NOT EXISTS user_schema;

-- Switch to user_schema
SET
search_path TO user_schema;

-- Drop tables if they exist (for clean initialization)
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- Create roles table
CREATE TABLE roles
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    permissions TEXT,
    is_default  BOOLEAN   DEFAULT FALSE,
    is_system   BOOLEAN   DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create users table
CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    email              VARCHAR(100) NOT NULL UNIQUE,
    password           VARCHAR(255) NOT NULL,
    first_name         VARCHAR(50)  NOT NULL,
    last_name          VARCHAR(50)  NOT NULL,
    phone              VARCHAR(20),
    is_active          BOOLEAN   DEFAULT TRUE,
    is_verified        BOOLEAN   DEFAULT FALSE,
    verification_token VARCHAR(255),
    reset_token        VARCHAR(255),
    last_login         TIMESTAMP,
    role_id            BIGINT       REFERENCES roles (id) ON DELETE SET NULL,
    school_id          BIGINT,
    profile_picture    VARCHAR(500),
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default roles
INSERT INTO roles (name, description, permissions, is_default, is_system)
VALUES ('SUPER_ADMIN', 'Super Administrator with full system access', 'ALL_PERMISSIONS', FALSE, TRUE),
       ('ADMIN', 'Administrator with management access', 'USER_MANAGE,ROLE_MANAGE,SYSTEM_VIEW', FALSE, TRUE),
       ('MODERATOR', 'Moderator with moderation access', 'USER_VIEW,CONTENT_MODERATE', FALSE, FALSE),
       ('TEACHER', 'Teacher role', 'STUDENT_MANAGE,GRADE_MANAGE,CONTENT_CREATE', FALSE, FALSE),
       ('STUDENT', 'Student role', 'COURSE_VIEW,ASSIGNMENT_SUBMIT,PROFILE_EDIT', TRUE, FALSE),
       ('PARENT', 'Parent role', 'STUDENT_VIEW,GRADE_VIEW,ATTENDANCE_VIEW', FALSE, FALSE),
       ('STAFF', 'School staff', 'SCHOOL_INFO_VIEW,STUDENT_VIEW_BASIC', FALSE, FALSE);

-- Insert test users (passwords are bcrypt encoded)
-- Password for all test users: "Test@123"
INSERT INTO users (email, password, first_name, last_name, phone, role_id, is_active, is_verified)
VALUES ('superadmin@school.com', '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', 'Super', 'Admin',
        '+1234567890', 1, TRUE, TRUE),
       ('admin@school.com', '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', 'School', 'Administrator',
        '+1234567891', 2, TRUE, TRUE),
       ('teacher.john@school.com', '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', 'John', 'Doe',
        '+1234567892', 4, TRUE, TRUE),
       ('student.alice@school.com', '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', 'Alice', 'Smith',
        '+1234567893', 5, TRUE, TRUE),
       ('student.bob@school.com', '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', 'Bob', 'Johnson',
        '+1234567894', 5, TRUE, TRUE),
       ('parent.miller@school.com', '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', 'Sarah', 'Miller',
        '+1234567895', 6, TRUE, TRUE);

-- Create indexes for performance
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_users_role_id ON users (role_id);
CREATE INDEX idx_users_school_id ON users (school_id);
CREATE INDEX idx_users_is_active ON users (is_active);
CREATE INDEX idx_roles_name ON roles (name);

-- Grant privileges (adjust based on your user)
GRANT
ALL
PRIVILEGES
ON
ALL
TABLES IN SCHEMA user_schema TO user_admin;
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA user_schema TO user_admin;
GRANT USAGE ON SCHEMA
user_schema TO user_admin;

-- Add comments to tables
COMMENT
ON TABLE roles IS 'Stores user roles and permissions';
COMMENT
ON TABLE users IS 'Stores user account information';