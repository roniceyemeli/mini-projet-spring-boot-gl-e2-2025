-- init-user.sql
-- Initialize user_schema with UUID-based tables for microservice architecture

-- Drop existing schema and recreate (for development only)
DROP SCHEMA IF EXISTS user_schema CASCADE;
CREATE SCHEMA user_schema;

-- Set the search path to user_schema
SET search_path TO user_schema;

-- Create roles table with UUID primary key
CREATE TABLE roles (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(50) NOT NULL UNIQUE,
                       description VARCHAR(255),
                       permissions TEXT,
                       is_default BOOLEAN DEFAULT FALSE,
                       is_system BOOLEAN DEFAULT FALSE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create users table with UUID primary key
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       phone VARCHAR(20),
                       is_active BOOLEAN DEFAULT TRUE,
                       is_verified BOOLEAN DEFAULT FALSE,
                       verification_token VARCHAR(255),
                       reset_token VARCHAR(255),
                       last_login TIMESTAMP,
                       role_id UUID REFERENCES roles(id) ON DELETE SET NULL,
                       profile_picture VARCHAR(500),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default roles with UUIDs that match your test data
INSERT INTO roles (id, name, description, permissions, is_default, is_system) VALUES
                                                                                  (
                                                                                      'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', -- SUPER_ADMIN
                                                                                      'SUPER_ADMIN',
                                                                                      'Super Administrator with full system access',
                                                                                      'USER_CREATE,USER_READ,USER_UPDATE,USER_DELETE,ROLE_CREATE,ROLE_READ,ROLE_UPDATE,ROLE_DELETE,SYSTEM_ADMIN',
                                                                                      FALSE,
                                                                                      TRUE
                                                                                  ),
                                                                                  (
                                                                                      'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', -- ADMIN
                                                                                      'ADMIN',
                                                                                      'Administrator with management access',
                                                                                      'USER_CREATE,USER_READ,USER_UPDATE,ROLE_READ,SYSTEM_VIEW,STUDENT_MANAGE,TEACHER_MANAGE',
                                                                                      FALSE,
                                                                                      TRUE
                                                                                  ),
                                                                                  (
                                                                                      'cccccccc-cccc-cccc-cccc-cccccccccccc', -- MODERATOR
                                                                                      'MODERATOR',
                                                                                      'Moderator with moderation access',
                                                                                      'USER_READ,CONTENT_MODERATE,COMMENT_DELETE,REPORT_MANAGE',
                                                                                      FALSE,
                                                                                      FALSE
                                                                                  ),
                                                                                  (
                                                                                      'dddddddd-dddd-dddd-dddd-dddddddddddd', -- TEACHER
                                                                                      'TEACHER',
                                                                                      'Teacher role with academic permissions',
                                                                                      'STUDENT_READ,STUDENT_GRADE,COURSE_CREATE,ASSIGNMENT_CREATE,CONTENT_CREATE,ATTENDANCE_MANAGE',
                                                                                      FALSE,
                                                                                      FALSE
                                                                                  ),
                                                                                  (
                                                                                      'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- STUDENT (Default role)
                                                                                      'STUDENT',
                                                                                      'Student role for learners',
                                                                                      'COURSE_VIEW,ASSIGNMENT_SUBMIT,PROFILE_EDIT,GRADE_VIEW,ATTENDANCE_VIEW',
                                                                                      TRUE,
                                                                                      FALSE
                                                                                  ),
                                                                                  (
                                                                                      'ffffffff-ffff-ffff-ffff-ffffffffffff', -- PARENT
                                                                                      'PARENT',
                                                                                      'Parent role for student guardians',
                                                                                      'STUDENT_VIEW,GRADE_VIEW,ATTENDANCE_VIEW,REPORT_VIEW,PAYMENT_VIEW',
                                                                                      FALSE,
                                                                                      FALSE
                                                                                  ),
                                                                                  (
                                                                                      '11111111-1111-1111-1111-111111111111', -- STAFF
                                                                                      'STAFF',
                                                                                      'School staff and support personnel',
                                                                                      'SCHOOL_INFO_VIEW,STUDENT_VIEW_BASIC,EVENT_VIEW,RESOURCE_ACCESS',
                                                                                      FALSE,
                                                                                      FALSE
                                                                                  );

-- Insert test users that match the student-service test data
-- All passwords are bcrypt encoded "Test@123" = '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO'
-- These UUIDs match the user_id values in the student-service init-student.sql

-- Super Admin (matches SUPER_ADMIN role)
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture
) VALUES (
             '11111111-2222-3333-4444-555555555555', -- id
             'superadmin@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'Super', 'Admin', '+1-555-010-0000', -- name and phone
             'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', -- SUPER_ADMIN role
             true, true, 'https://example.com/profiles/superadmin.jpg'
         );

-- School Admin (matches ADMIN role)
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture
) VALUES (
             '22222222-3333-4444-5555-666666666666', -- id
             'admin@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'School', 'Administrator', '+1-555-010-0001', -- name and phone
             'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', -- ADMIN role
             true, true, 'https://example.com/profiles/admin.jpg'
         );

-- Teacher (John - matches TEACHER role)
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture
) VALUES (
             '33333333-4444-5555-6666-777777777777', -- id
             'teacher.john@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'John', 'Teacher', '+1-555-010-0002', -- name and phone
             'dddddddd-dddd-dddd-dddd-dddddddddddd', -- TEACHER role
             true, true, 'https://example.com/profiles/teacher_john.jpg'
         );

-- STUDENT 1: John Smith (matches student 1 in student-service)
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, last_login
) VALUES (
             'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', -- id (MATCHES student.user_id in student-service)
             'john.smith@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'John', 'Smith', '+1-617-555-0101', -- name and phone
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- STUDENT role
             true, true, 'https://example.com/profiles/john.jpg', '2024-01-15 10:30:00'
         );

-- STUDENT 2: Maria Garcia (matches student 2 in student-service)
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, last_login
) VALUES (
             'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', -- id (MATCHES student.user_id in student-service)
             'maria.garcia@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'Maria', 'Garcia', '+34-91-555-0123', -- name and phone
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- STUDENT role
             true, true, 'https://example.com/profiles/maria.jpg', '2024-01-10 14:20:00'
         );

-- STUDENT 3: David Chen (matches student 3 in student-service)
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, last_login
) VALUES (
             'cccccccc-cccc-cccc-cccc-cccccccccccc', -- id (MATCHES student.user_id in student-service)
             'david.chen@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'David', 'Chen', '+1-415-555-0789', -- name and phone
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- STUDENT role
             true, true, 'https://example.com/profiles/david.jpg', '2024-01-05 09:15:00'
         );

-- Parent User
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture
) VALUES (
             '44444444-5555-6666-7777-888888888888', -- id
             'parent.miller@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'Sarah', 'Miller', '+1-555-010-0003', -- name and phone
             'ffffffff-ffff-ffff-ffff-ffffffffffff', -- PARENT role
             true, true, 'https://example.com/profiles/sarah.jpg'
         );

-- Staff User
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture
) VALUES (
             '55555555-6666-7777-8888-999999999999', -- id
             'staff.jones@school.com', -- email
             '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO', -- password
             'Robert', 'Jones', '+1-555-010-0004', -- name and phone
             '11111111-1111-1111-1111-111111111111', -- STAFF role
             true, true, 'https://example.com/profiles/robert.jpg'
         );

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role_id ON users(role_id);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_is_verified ON users(is_verified);
CREATE INDEX idx_users_last_login ON users(last_login);
CREATE INDEX idx_roles_name ON roles(name);
CREATE INDEX idx_roles_is_default ON roles(is_default);
CREATE INDEX idx_roles_is_system ON roles(is_system);

-- Create view for active users
CREATE OR REPLACE VIEW active_users AS
SELECT u.*, r.name as role_name, r.permissions
FROM users u
         LEFT JOIN roles r ON u.role_id = r.id
WHERE u.is_active = true;

-- Create view for users with student role
CREATE OR REPLACE VIEW student_users AS
SELECT u.*, r.name as role_name
FROM users u
         JOIN roles r ON u.role_id = r.id
WHERE r.name = 'STUDENT' AND u.is_active = true;

-- Create view for teacher users
CREATE OR REPLACE VIEW teacher_users AS
SELECT u.*, r.name as role_name, r.permissions
FROM users u
         JOIN roles r ON u.role_id = r.id
WHERE r.name = 'TEACHER' AND u.is_active = true;

-- Grant privileges to user_admin
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA user_schema TO user_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA user_schema TO user_admin;
GRANT USAGE ON SCHEMA user_schema TO user_admin;
GRANT SELECT ON ALL TABLES IN SCHEMA user_schema TO user_admin;

-- Add table comments
COMMENT ON TABLE roles IS 'Stores user roles and permissions with UUID primary keys';
COMMENT ON TABLE users IS 'Stores user account information with UUID primary keys for microservice architecture';
COMMENT ON COLUMN users.role_id IS 'References roles.id (UUID) for role-based access control';
COMMENT ON COLUMN roles.permissions IS 'Comma-separated list of permission strings (e.g., USER_READ,USER_WRITE)';

-- Verification queries
SELECT 'Total Users' as metric, COUNT(*) as value FROM users
UNION ALL
SELECT 'Active Users', COUNT(*) FROM users WHERE is_active = true
UNION ALL
SELECT 'Verified Users', COUNT(*) FROM users WHERE is_verified = true
UNION ALL
SELECT 'Student Users', COUNT(*) FROM users u
                                          JOIN roles r ON u.role_id = r.id WHERE r.name = 'STUDENT'
UNION ALL
SELECT 'Teacher Users', COUNT(*) FROM users u
                                          JOIN roles r ON u.role_id = r.id WHERE r.name = 'TEACHER';

-- Display inserted users with their roles
SELECT
    u.email,
    CONCAT(u.first_name, ' ', u.last_name) as full_name,
    r.name as role,
    u.is_active,
    u.is_verified,
    u.last_login
FROM users u
         LEFT JOIN roles r ON u.role_id = r.id
ORDER BY r.name, u.last_name;