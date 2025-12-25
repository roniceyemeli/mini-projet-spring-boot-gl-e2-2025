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

-- Insert default roles with proper UUIDs
DO $$
DECLARE
role_super_admin UUID := 'a1b2c3d4-e5f6-7890-abcd-ef1234567890';
    role_admin UUID := 'b2c3d4e5-f6a7-8901-bcde-f23456789012';
    role_moderator UUID := 'c3d4e5f6-a7b8-9012-cdef-345678901234';
    role_teacher UUID := 'd4e5f6a7-b8c9-0123-def0-456789012345';
    role_student UUID := 'e5f6a7b8-c9d0-1234-ef01-567890123456';
    role_parent UUID := 'f6a7b8c9-d0e1-2345-f012-678901234567';
    role_staff UUID := 'a7b8c9d0-e1f2-3456-7890-abcd12345678';
BEGIN
INSERT INTO roles (id, name, description, permissions, is_default, is_system) VALUES
                                                                                  (role_super_admin,
                                                                                   'SUPER_ADMIN',
                                                                                   'Super Administrator with full system access',
                                                                                   'USER_CREATE,USER_READ,USER_UPDATE,USER_DELETE,ROLE_CREATE,ROLE_READ,ROLE_UPDATE,ROLE_DELETE,SYSTEM_ADMIN,SYSTEM_CONFIG,LOG_VIEW,AUDIT_VIEW',
                                                                                   FALSE,
                                                                                   TRUE),

                                                                                  (role_admin,
                                                                                   'ADMIN',
                                                                                   'Administrator with management access',
                                                                                   'USER_CREATE,USER_READ,USER_UPDATE,ROLE_READ,SYSTEM_VIEW,STUDENT_MANAGE,TEACHER_MANAGE,SCHOOL_MANAGE,COURSE_MANAGE',
                                                                                   FALSE,
                                                                                   TRUE),

                                                                                  (role_moderator,
                                                                                   'MODERATOR',
                                                                                   'Moderator with moderation access',
                                                                                   'USER_READ,CONTENT_MODERATE,COMMENT_DELETE,REPORT_MANAGE,FLAG_REVIEW,COMMUNITY_MANAGE',
                                                                                   FALSE,
                                                                                   FALSE),

                                                                                  (role_teacher,
                                                                                   'TEACHER',
                                                                                   'Teacher role with academic permissions',
                                                                                   'STUDENT_READ,STUDENT_GRADE,COURSE_CREATE,ASSIGNMENT_CREATE,CONTENT_CREATE,ATTENDANCE_MANAGE,GRADEBOOK_ACCESS,ANNOUNCEMENT_CREATE',
                                                                                   FALSE,
                                                                                   FALSE),

                                                                                  (role_student,
                                                                                   'STUDENT',
                                                                                   'Student role for learners',
                                                                                   'COURSE_VIEW,ASSIGNMENT_SUBMIT,PROFILE_EDIT,GRADE_VIEW,ATTENDANCE_VIEW,DISCUSSION_PARTICIPATE,RESOURCE_ACCESS',
                                                                                   TRUE,
                                                                                   FALSE),

                                                                                  (role_parent,
                                                                                   'PARENT',
                                                                                   'Parent role for student guardians',
                                                                                   'STUDENT_VIEW,GRADE_VIEW,ATTENDANCE_VIEW,REPORT_VIEW,PAYMENT_VIEW,SCHEDULE_VIEW,COMMUNICATION_SEND',
                                                                                   FALSE,
                                                                                   FALSE),

                                                                                  (role_staff,
                                                                                   'STAFF',
                                                                                   'School staff and support personnel',
                                                                                   'SCHOOL_INFO_VIEW,STUDENT_VIEW_BASIC,EVENT_VIEW,RESOURCE_ACCESS,HELPDESK_SUPPORT,SYSTEM_REPORT',
                                                                                   FALSE,
                                                                                   FALSE);
END $$;

-- Insert test users with proper UUIDs
DO $$
DECLARE
    -- User IDs
user_super_admin UUID := 'f1e2d3c4-b5a6-7890-fedc-ba9876543210';
    user_admin UUID := 'e2d3c4b5-a6f7-8901-edcb-a98765432109';
    user_teacher UUID := 'd3c4b5a6-f7e8-9012-dcba-987654321098';
    user_student1 UUID := '5a6b7c8d-9e0f-1234-5678-9abcdef01234';  -- Matches student1 in student-service
    user_student2 UUID := '6b7c8d9e-0f12-3456-789a-bcdef0123456';  -- Matches student2 in student-service
    user_student3 UUID := '7c8d9e0f-1234-5678-9abc-def012345678';  -- Matches student3 in student-service
    user_parent UUID := 'c4b5a6f7-e8d9-0123-cba9-876543210987';
    user_staff UUID := 'b5a6f7e8-d9c0-1234-ba98-765432109876';

    -- Role IDs (from above)
    role_super_admin UUID := 'a1b2c3d4-e5f6-7890-abcd-ef1234567890';
    role_admin UUID := 'b2c3d4e5-f6a7-8901-bcde-f23456789012';
    role_moderator UUID := 'c3d4e5f6-a7b8-9012-cdef-345678901234';
    role_teacher UUID := 'd4e5f6a7-b8c9-0123-def0-456789012345';
    role_student UUID := 'e5f6a7b8-c9d0-1234-ef01-567890123456';
    role_parent UUID := 'f6a7b8c9-d0e1-2345-f012-678901234567';
    role_staff UUID := 'a7b8c9d0-e1f2-3456-7890-abcd12345678';

    -- Common bcrypt hashed password for "Test@123"
    hashed_password VARCHAR(255) := '$2a$12$EN2zVUWW71jQkKsRMuQ5ruNihdaI02HVCsspebKC8zy5Ipr3844cO';
BEGIN
    -- Super Admin
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, created_at, updated_at
) VALUES (
             user_super_admin,
             'admin.super@schoolsystem.edu',
             hashed_password,
             'Alexander',
             'Morgan',
             '+1-800-555-0100',
             role_super_admin,
             true,
             true,
             'https://storage.example.com/profiles/admin-super-alexander.jpg',
             '2023-01-15 09:00:00',
             '2024-12-01 14:30:00'
         );

-- School Admin
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, created_at, updated_at
) VALUES (
             user_admin,
             'admin.school@greenschool.edu',
             hashed_password,
             'Jennifer',
             'Williams',
             '+1-555-020-1001',
             role_admin,
             true,
             true,
             'https://storage.example.com/profiles/admin-jennifer.jpg',
             '2023-02-20 10:15:00',
             '2024-11-25 11:45:00'
         );

-- Teacher (John Davis)
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, last_login, created_at, updated_at
) VALUES (
             user_teacher,
             'teacher.john.davis@greenschool.edu',
             hashed_password,
             'John',
             'Davis',
             '+1-555-020-1002',
             role_teacher,
             true,
             true,
             'https://storage.example.com/profiles/teacher-john-davis.jpg',
             '2024-12-10 08:30:00',
             '2023-03-10 14:20:00',
             '2024-12-10 08:35:00'
         );

-- STUDENT 1: John Smith (Computer Science) - matches student service
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, last_login, created_at, updated_at
) VALUES (
             user_student1,
             'student.john.smith@greenschool.edu',
             hashed_password,
             'John',
             'Smith',
             '+1-617-555-0101',
             role_student,
             true,
             true,
             'https://storage.example.com/profiles/student-john-smith.jpg',
             '2024-01-15 10:30:00',
             '2023-08-25 09:00:00',
             '2024-01-15 10:35:00'
         );

-- STUDENT 2: Maria Garcia (International Business) - matches student service
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, last_login, created_at, updated_at
) VALUES (
             user_student2,
             'student.maria.garcia@greenschool.edu',
             hashed_password,
             'Maria',
             'Garcia',
             '+34-91-555-0123',
             role_student,
             true,
             true,
             'https://storage.example.com/profiles/student-maria-garcia.jpg',
             '2024-01-10 14:20:00',
             '2022-09-05 11:30:00',
             '2024-01-10 14:25:00'
         );

-- STUDENT 3: David Chen (Mechanical Engineering) - matches student service
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, last_login, created_at, updated_at
) VALUES (
             user_student3,
             'student.david.chen@greenschool.edu',
             hashed_password,
             'David',
             'Chen',
             '+1-415-555-0789',
             role_student,
             false,  -- inactive (graduated)
             true,
             'https://storage.example.com/profiles/student-david-chen.jpg',
             '2024-01-05 09:15:00',
             '2019-08-20 13:45:00',
             '2023-05-20 16:00:00'
         );

-- Parent User
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, created_at, updated_at
) VALUES (
             user_parent,
             'parent.sarah.miller@greenschool.edu',
             hashed_password,
             'Sarah',
             'Miller',
             '+1-555-020-1003',
             role_parent,
             true,
             true,
             'https://storage.example.com/profiles/parent-sarah-miller.jpg',
             '2023-09-10 15:30:00',
             '2024-11-15 10:20:00'
         );

-- Staff User
INSERT INTO users (
    id, email, password, first_name, last_name, phone,
    role_id, is_active, is_verified, profile_picture, created_at, updated_at
) VALUES (
             user_staff,
             'staff.robert.jones@greenschool.edu',
             hashed_password,
             'Robert',
             'Jones',
             '+1-555-020-1004',
             role_staff,
             true,
             true,
             'https://storage.example.com/profiles/staff-robert-jones.jpg',
             '2023-06-15 08:45:00',
             '2024-12-05 09:30:00'
         );
END $$;

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role_id ON users(role_id);
CREATE INDEX idx_users_is_active ON users(is_active);
CREATE INDEX idx_users_is_verified ON users(is_verified);
CREATE INDEX idx_users_last_login ON users(last_login);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_updated_at ON users(updated_at);
CREATE INDEX idx_users_full_name ON users(first_name, last_name);

CREATE INDEX idx_roles_name ON roles(name);
CREATE INDEX idx_roles_is_default ON roles(is_default);
CREATE INDEX idx_roles_is_system ON roles(is_system);
CREATE INDEX idx_roles_created_at ON roles(created_at);

-- Create view for active users
CREATE OR REPLACE VIEW active_users AS
SELECT
    u.id,
    u.email,
    CONCAT(u.first_name, ' ', u.last_name) as full_name,
    u.phone,
    r.name as role_name,
    r.permissions,
    u.is_active,
    u.is_verified,
    u.last_login,
    u.created_at,
    u.updated_at
FROM users u
         LEFT JOIN roles r ON u.role_id = r.id
WHERE u.is_active = true;

-- Create view for users with student role
CREATE OR REPLACE VIEW student_users AS
SELECT
    u.id,
    u.email,
    CONCAT(u.first_name, ' ', u.last_name) as full_name,
    u.phone,
    u.is_verified,
    u.last_login,
    u.created_at
FROM users u
         JOIN roles r ON u.role_id = r.id
WHERE r.name = 'STUDENT' AND u.is_active = true;

-- Create view for teacher users
CREATE OR REPLACE VIEW teacher_users AS
SELECT
    u.id,
    u.email,
    CONCAT(u.first_name, ' ', u.last_name) as full_name,
    u.phone,
    r.permissions,
    u.is_verified,
    u.last_login,
    u.created_at
FROM users u
         JOIN roles r ON u.role_id = r.id
WHERE r.name = 'TEACHER' AND u.is_active = true;

-- Create view for user role distribution
CREATE OR REPLACE VIEW user_role_distribution AS
SELECT
    r.name as role_name,
    COUNT(u.id) as user_count,
    SUM(CASE WHEN u.is_active = true THEN 1 ELSE 0 END) as active_users,
    SUM(CASE WHEN u.is_verified = true THEN 1 ELSE 0 END) as verified_users,
    ROUND(AVG(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - u.created_at))/86400), 1) as avg_account_age_days
FROM roles r
         LEFT JOIN users u ON r.id = u.role_id
GROUP BY r.name, r.id
ORDER BY user_count DESC;

-- Create view for user activity analysis
CREATE OR REPLACE VIEW user_activity_analysis AS
SELECT
    CASE
        WHEN last_login >= CURRENT_DATE - INTERVAL '7 days' THEN 'Active (Last 7 days)'
        WHEN last_login >= CURRENT_DATE - INTERVAL '30 days' THEN 'Active (Last 30 days)'
        WHEN last_login >= CURRENT_DATE - INTERVAL '90 days' THEN 'Semi-Active (Last 90 days)'
        WHEN last_login IS NULL THEN 'Never Logged In'
        ELSE 'Inactive (> 90 days)'
END as activity_level,
    COUNT(*) as user_count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM users), 2) as percentage
FROM users
WHERE is_active = true
GROUP BY activity_level
ORDER BY
    CASE activity_level
        WHEN 'Active (Last 7 days)' THEN 1
        WHEN 'Active (Last 30 days)' THEN 2
        WHEN 'Semi-Active (Last 90 days)' THEN 3
        WHEN 'Never Logged In' THEN 4
        ELSE 5
END;

-- Grant privileges to user_admin
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA user_schema TO user_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA user_schema TO user_admin;
GRANT USAGE ON SCHEMA user_schema TO user_admin;
GRANT SELECT ON ALL TABLES IN SCHEMA user_schema TO user_admin;
GRANT SELECT ON active_users TO user_admin;
GRANT SELECT ON student_users TO user_admin;
GRANT SELECT ON teacher_users TO user_admin;
GRANT SELECT ON user_role_distribution TO user_admin;
GRANT SELECT ON user_activity_analysis TO user_admin;

-- Add table comments
COMMENT ON TABLE roles IS 'Stores user roles and permissions with UUID primary keys';
COMMENT ON TABLE users IS 'Stores user account information with UUID primary keys for microservice architecture';
COMMENT ON COLUMN users.role_id IS 'References roles.id (UUID) for role-based access control';
COMMENT ON COLUMN roles.permissions IS 'Comma-separated list of permission strings (e.g., USER_READ,USER_WRITE)';
COMMENT ON COLUMN users.verification_token IS 'Token for email verification (null after verification)';
COMMENT ON COLUMN users.reset_token IS 'Token for password reset (temporary, expires after use)';

-- Verification queries
SELECT 'Total Users' as metric, COUNT(*) as value FROM users
UNION ALL
SELECT 'Active Users', COUNT(*) FROM users WHERE is_active = true
UNION ALL
SELECT 'Verified Users', COUNT(*) FROM users WHERE is_verified = true
UNION ALL
SELECT 'Student Users', COUNT(*) FROM users u JOIN roles r ON u.role_id = r.id WHERE r.name = 'STUDENT'
UNION ALL
SELECT 'Teacher Users', COUNT(*) FROM users u JOIN roles r ON u.role_id = r.id WHERE r.name = 'TEACHER'
UNION ALL
SELECT 'Admin Users', COUNT(*) FROM users u JOIN roles r ON u.role_id = r.id WHERE r.name IN ('SUPER_ADMIN', 'ADMIN')
UNION ALL
SELECT 'Average Account Age (days)', ROUND(AVG(EXTRACT(EPOCH FROM (CURRENT_TIMESTAMP - created_at))/86400), 1) FROM users;

-- Display inserted users with their roles
SELECT
    u.email,
    CONCAT(u.first_name, ' ', u.last_name) as full_name,
    r.name as role,
    u.is_active,
    u.is_verified,
    TO_CHAR(u.last_login, 'YYYY-MM-DD HH24:MI') as last_login,
    TO_CHAR(u.created_at, 'YYYY-MM-DD') as created_date
FROM users u
         LEFT JOIN roles r ON u.role_id = r.id
ORDER BY
    CASE r.name
        WHEN 'SUPER_ADMIN' THEN 1
        WHEN 'ADMIN' THEN 2
        WHEN 'TEACHER' THEN 3
        WHEN 'STAFF' THEN 4
        WHEN 'PARENT' THEN 5
        WHEN 'STUDENT' THEN 6
        ELSE 7
        END,
    u.last_name;

-- Display role permissions summary
SELECT
    r.name as role_name,
    r.description,
    CASE WHEN r.is_default THEN 'Yes' ELSE 'No' END as is_default,
    CASE WHEN r.is_system THEN 'Yes' ELSE 'No' END as is_system,
    STRING_AGG(DISTINCT u.email, ', ') as sample_users
FROM roles r
         LEFT JOIN users u ON r.id = u.role_id
GROUP BY r.id, r.name, r.description, r.is_default, r.is_system
ORDER BY
    CASE r.name
        WHEN 'SUPER_ADMIN' THEN 1
        WHEN 'ADMIN' THEN 2
        WHEN 'TEACHER' THEN 3
        WHEN 'STAFF' THEN 4
        WHEN 'PARENT' THEN 5
        WHEN 'STUDENT' THEN 6
        WHEN 'MODERATOR' THEN 7
        ELSE 8
        END;