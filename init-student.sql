-- init-student.sql
-- Initialize student_schema and populate with sample data

-- Drop existing schema and recreate (for development only)
DROP SCHEMA IF EXISTS student_schema CASCADE;
CREATE SCHEMA student_schema;

-- Set the search path to student_schema
SET search_path TO student_schema;

-- Create students table (matches your entity structure)
CREATE TABLE students (
                          id UUID PRIMARY KEY,
                          user_id UUID NOT NULL UNIQUE,
                          student_code VARCHAR(50) UNIQUE,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          full_name VARCHAR(200),
                          date_of_birth DATE,
                          gender VARCHAR(20),
                          nationality VARCHAR(100),
                          citizenship VARCHAR(100),
                          id_card_number VARCHAR(50) UNIQUE,
                          passport_number VARCHAR(50) UNIQUE,
                          address VARCHAR(500),
                          city VARCHAR(100),
                          state VARCHAR(100),
                          country VARCHAR(100),
                          postal_code VARCHAR(20),
                          phone_number VARCHAR(20),
                          emergency_phone VARCHAR(20),
                          personal_email VARCHAR(100),
                          school_id UUID,
                          program VARCHAR(100),
                          major VARCHAR(100),
                          minor VARCHAR(100),
                          enrollment_year INTEGER,
                          expected_graduation_year INTEGER,
                          enrollment_status VARCHAR(20) DEFAULT 'ACTIVE',
                          academic_level VARCHAR(50),
                          gpa NUMERIC(3,2),
                          total_credits INTEGER DEFAULT 0,
                          completed_credits INTEGER DEFAULT 0,
                          community_id UUID,
                          club_id UUID,
                          advisor_id UUID,
                          profile_picture VARCHAR(500),
                          resume_url VARCHAR(500),
                          linkedin_url VARCHAR(500),
                          github_url VARCHAR(500),
                          portfolio_url VARCHAR(500),
                          is_international BOOLEAN DEFAULT false,
                          visa_status VARCHAR(50),
                          financial_aid_status VARCHAR(50),
                          scholarship_name VARCHAR(200),
                          disabilities TEXT,
                          special_needs TEXT,
                          medical_conditions TEXT,
                          emergency_contact_name VARCHAR(200),
                          emergency_contact_relationship VARCHAR(50),
                          emergency_contact_address VARCHAR(500),
                          emergency_contact_email VARCHAR(100),
                          is_active BOOLEAN DEFAULT true,
                          is_graduated BOOLEAN DEFAULT false,
                          graduation_date DATE,
                          notes TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_by BIGINT,
                          updated_by BIGINT,
                          user_sync_status VARCHAR(20) DEFAULT 'PENDING',
                          advisor_sync_status VARCHAR(20) DEFAULT 'PENDING',
                          last_sync_attempt TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX idx_students_user_id ON students(user_id);
CREATE INDEX idx_students_student_code ON students(student_code);
CREATE INDEX idx_students_school_id ON students(school_id);
CREATE INDEX idx_students_enrollment_status ON students(enrollment_status);
CREATE INDEX idx_students_program ON students(program);
CREATE INDEX idx_students_major ON students(major);
CREATE INDEX idx_students_advisor_id ON students(advisor_id);
CREATE INDEX idx_students_is_active ON students(is_active);
CREATE INDEX idx_students_is_graduated ON students(is_graduated);

-- Insert 3 sample students with realistic data

-- Student 1: Computer Science Student
INSERT INTO students (
    id, user_id, student_code, first_name, last_name, full_name,
    date_of_birth, gender, nationality, citizenship,
    id_card_number, passport_number,
    address, city, state, country, postal_code,
    phone_number, emergency_phone, personal_email,
    school_id, program, major, minor,
    enrollment_year, expected_graduation_year,
    enrollment_status, academic_level, gpa, total_credits, completed_credits,
    community_id, club_id, advisor_id,
    profile_picture, resume_url, linkedin_url, github_url, portfolio_url,
    is_international, visa_status, financial_aid_status, scholarship_name,
    disabilities, special_needs, medical_conditions,
    emergency_contact_name, emergency_contact_relationship, emergency_contact_address, emergency_contact_email,
    is_active, is_graduated, graduation_date, notes,
    created_by, updated_by,
    user_sync_status, advisor_sync_status, last_sync_attempt
) VALUES (
             '11111111-1111-1111-1111-111111111111', -- id
             'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', -- user_id (matches a user from user-service)
             '2023-STU-00123', -- student_code
             'John', 'Smith', 'John Smith', -- names
             '2000-05-15', -- date_of_birth
             'MALE', 'American', 'United States', -- gender, nationality, citizenship
             'ID123456789', 'P987654321', -- id_card_number, passport_number
             '123 Main Street', 'Boston', 'Massachusetts', 'USA', '02115', -- address
             '+1-617-555-0101', '+1-617-555-0202', 'john.smith@personal.com', -- contacts
             '99999999-9999-9999-9999-999999999999', -- school_id (University UUID)
             'Bachelor of Science', 'Computer Science', 'Mathematics', -- program, major, minor
             2023, 2027, -- enrollment_year, expected_graduation_year
             'ACTIVE', 'SOPHOMORE', 3.75, 120, 45, -- status, level, gpa, credits
             'cccccccc-cccc-cccc-cccc-cccccccccccc', -- community_id
             'dddddddd-dddd-dddd-dddd-dddddddddddd', -- club_id
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- advisor_id
             'https://example.com/profiles/john.jpg', -- profile_picture
             'https://example.com/resumes/john.pdf', -- resume_url
             'https://linkedin.com/in/johnsmith', -- linkedin_url
             'https://github.com/johnsmith', -- github_url
             'https://johnsmith.dev', -- portfolio_url
             false, NULL, 'FULL_SCHOLARSHIP', 'Dean''s Scholarship', -- international, visa, financial aid
             NULL, NULL, NULL, -- disabilities, special_needs, medical_conditions
             'Jane Smith', 'Mother', '123 Main Street, Boston, MA 02115', 'jane.smith@emergency.com', -- emergency contact
             true, false, NULL, 'Excellent student with strong programming skills', -- active, graduated, notes
             1, 1, -- created_by, updated_by
             'SYNCED', 'SYNCED', '2024-01-15 10:30:00' -- sync status
         );

-- Student 2: International Business Student
INSERT INTO students (
    id, user_id, student_code, first_name, last_name, full_name,
    date_of_birth, gender, nationality, citizenship,
    id_card_number, passport_number,
    address, city, state, country, postal_code,
    phone_number, emergency_phone, personal_email,
    school_id, program, major, minor,
    enrollment_year, expected_graduation_year,
    enrollment_status, academic_level, gpa, total_credits, completed_credits,
    community_id, club_id, advisor_id,
    profile_picture, resume_url, linkedin_url, github_url, portfolio_url,
    is_international, visa_status, financial_aid_status, scholarship_name,
    disabilities, special_needs, medical_conditions,
    emergency_contact_name, emergency_contact_relationship, emergency_contact_address, emergency_contact_email,
    is_active, is_graduated, graduation_date, notes,
    created_by, updated_by,
    user_sync_status, advisor_sync_status, last_sync_attempt
) VALUES (
             '22222222-2222-2222-2222-222222222222', -- id
             'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', -- user_id
             '2022-STU-00456', -- student_code
             'Maria', 'Garcia', 'Maria Garcia', -- names
             '2001-08-22', -- date_of_birth
             'FEMALE', 'Spanish', 'Spain', -- gender, nationality, citizenship
             'ID987654321', 'P123456789', -- id_card_number, passport_number
             '456 Oak Avenue', 'Madrid', 'Madrid', 'Spain', '28001', -- address
             '+34-91-555-0123', '+34-91-555-0456', 'maria.garcia@personal.com', -- contacts
             '99999999-9999-9999-9999-999999999999', -- school_id
             'Bachelor of Business Administration', 'International Business', 'Spanish Language', -- program, major, minor
             2022, 2026, -- enrollment_year, expected_graduation_year
             'ACTIVE', 'JUNIOR', 3.92, 120, 75, -- status, level, gpa, credits
             'cccccccc-cccc-cccc-cccc-cccccccccccc', -- community_id
             'ffffffff-ffff-ffff-ffff-ffffffffffff', -- club_id (different club)
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- advisor_id
             'https://example.com/profiles/maria.jpg', -- profile_picture
             'https://example.com/resumes/maria.pdf', -- resume_url
             'https://linkedin.com/in/mariagarcia', -- linkedin_url
             'https://github.com/mariagarcia', -- github_url
             'https://mariagarcia.dev', -- portfolio_url
             true, 'F1_STUDENT_VISA', 'PARTIAL_SCHOLARSHIP', 'International Excellence Scholarship', -- international, visa, financial aid
             NULL, NULL, 'Asthma (uses inhaler)', -- disabilities, special_needs, medical_conditions
             'Carlos Garcia', 'Father', '456 Oak Avenue, Madrid, Spain 28001', 'carlos.garcia@emergency.com', -- emergency contact
             true, false, NULL, 'International student from Spain, fluent in 3 languages', -- active, graduated, notes
             1, 1, -- created_by, updated_by
             'SYNCED', 'PENDING', '2024-01-10 14:20:00' -- sync status
         );

-- Student 3: Mechanical Engineering Student (Graduated)
INSERT INTO students (
    id, user_id, student_code, first_name, last_name, full_name,
    date_of_birth, gender, nationality, citizenship,
    id_card_number, passport_number,
    address, city, state, country, postal_code,
    phone_number, emergency_phone, personal_email,
    school_id, program, major, minor,
    enrollment_year, expected_graduation_year,
    enrollment_status, academic_level, gpa, total_credits, completed_credits,
    community_id, club_id, advisor_id,
    profile_picture, resume_url, linkedin_url, github_url, portfolio_url,
    is_international, visa_status, financial_aid_status, scholarship_name,
    disabilities, special_needs, medical_conditions,
    emergency_contact_name, emergency_contact_relationship, emergency_contact_address, emergency_contact_email,
    is_active, is_graduated, graduation_date, notes,
    created_by, updated_by,
    user_sync_status, advisor_sync_status, last_sync_attempt
) VALUES (
             '33333333-3333-3333-3333-333333333333', -- id
             'cccccccc-cccc-cccc-cccc-cccccccccccc', -- user_id
             '2019-STU-00789', -- student_code
             'David', 'Chen', 'David Chen', -- names
             '1998-11-03', -- date_of_birth
             'MALE', 'Chinese', 'China', -- gender, nationality, citizenship
             'ID456789123', 'P321654987', -- id_card_number, passport_number
             '789 Pine Road', 'San Francisco', 'California', 'USA', '94102', -- address
             '+1-415-555-0789', '+1-415-555-0987', 'david.chen@personal.com', -- contacts
             '99999999-9999-9999-9999-999999999999', -- school_id
             'Bachelor of Engineering', 'Mechanical Engineering', 'Business Administration', -- program, major, minor
             2019, 2023, -- enrollment_year, expected_graduation_year
             'GRADUATED', 'SENIOR', 3.68, 120, 120, -- status, level, gpa, credits
             'cccccccc-cccc-cccc-cccc-cccccccccccc', -- community_id
             'dddddddd-dddd-dddd-dddd-dddddddddddd', -- club_id
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- advisor_id
             'https://example.com/profiles/david.jpg', -- profile_picture
             'https://example.com/resumes/david.pdf', -- resume_url
             'https://linkedin.com/in/davidchen', -- linkedin_url
             'https://github.com/davidchen', -- github_url
             'https://davidchen.dev', -- portfolio_url
             true, 'H1B_WORK_VISA', 'FULL_SCHOLARSHIP', 'Engineering Excellence Scholarship', -- international, visa, financial aid
             'Wears glasses', 'Extra time for exams', NULL, -- disabilities, special_needs, medical_conditions
             'Lisa Chen', 'Sister', '789 Pine Road, San Francisco, CA 94102', 'lisa.chen@emergency.com', -- emergency contact
             false, true, '2023-05-15', 'Graduated with honors, now working at Tesla', -- active, graduated, notes
             1, 1, -- created_by, updated_by
             'SYNCED', 'FAILED', '2024-01-05 09:15:00' -- sync status
         );

-- Create a view for active students
CREATE OR REPLACE VIEW active_students AS
SELECT * FROM students WHERE is_active = true;

-- Create a view for international students
CREATE OR REPLACE VIEW international_students AS
SELECT * FROM students WHERE is_international = true;

-- Create a view for students needing sync
CREATE OR REPLACE VIEW students_needing_sync AS
SELECT * FROM students
WHERE user_sync_status = 'PENDING'
   OR user_sync_status = 'FAILED'
   OR advisor_sync_status = 'PENDING'
   OR advisor_sync_status = 'FAILED';

-- Sample queries for verification
SELECT 'Total Students' as metric, COUNT(*) as value FROM students
UNION ALL
SELECT 'Active Students', COUNT(*) FROM students WHERE is_active = true
UNION ALL
SELECT 'International Students', COUNT(*) FROM students WHERE is_international = true
UNION ALL
SELECT 'Graduated Students', COUNT(*) FROM students WHERE is_graduated = true
UNION ALL
SELECT 'Average GPA', ROUND(AVG(gpa), 2) FROM students WHERE gpa IS NOT NULL;

-- Display inserted students
SELECT
    student_code,
    full_name,
    program,
    major,
    academic_level,
    enrollment_status,
    gpa,
    is_international,
    is_active
FROM students
ORDER BY enrollment_year DESC, last_name ASC;