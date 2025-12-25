-- init-student.sql
-- Initialize student_schema and populate with sample data

-- Drop existing schema and recreate (for development only)
DROP SCHEMA IF EXISTS student_schema CASCADE;
CREATE SCHEMA student_schema;

-- Set the search path to student_schema
SET search_path TO student_schema;

-- Create students table (matches your entity structure)
CREATE TABLE students (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
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

-- Insert sample students with proper UUIDs
DO $$
DECLARE
    -- Student IDs
student1_id UUID := 'a123b456-c789-d012-e345-f67890123456';
    student2_id UUID := 'b234c567-d890-e123-f456-789012345678';
    student3_id UUID := 'c345d678-e901-f234-5678-901234567890';

    -- User IDs (assuming these are real user UUIDs from user service)
    user1_id UUID := '5a6b7c8d-9e0f-1234-5678-9abcdef01234';
    user2_id UUID := '6b7c8d9e-0f12-3456-789a-bcdef0123456';
    user3_id UUID := '7c8d9e0f-1234-5678-9abc-def012345678';

    -- School ID (from school service)
    school1_id UUID := 'a1b2c3d4-e5f6-7890-abcd-ef1234567890';

    -- Community ID (from community service)
    community1_id UUID := 'd8e9f0a1-b2c3-4567-8901-23456789abcd';

    -- Club IDs (from club service)
    club1_id UUID := 'e9f0a1b2-c3d4-5678-9012-3456789abcde';
    club2_id UUID := 'f0a1b2c3-d4e5-6789-0123-456789abcdef';

    -- Advisor ID (from faculty service)
    advisor1_id UUID := 'a2b3c4d5-e6f7-8901-2345-6789abcdef01';
BEGIN
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
             student1_id,
             user1_id,
             '2023-CS-00123',
             'John',
             'Smith',
             'John Smith',
             '2000-05-15',
             'MALE',
             'American',
             'United States',
             'ID-USA-123456789',
             'P-USA-987654321',
             '123 Main Street',
             'Boston',
             'Massachusetts',
             'USA',
             '02115',
             '+1-617-555-0101',
             '+1-617-555-0202',
             'john.smith.personal@example.com',
             school1_id,
             'Bachelor of Science',
             'Computer Science',
             'Mathematics',
             2023,
             2027,
             'ACTIVE',
             'SOPHOMORE',
             3.75,
             120,
             45,
             community1_id,
             club1_id,
             advisor1_id,
             'https://storage.example.com/profiles/john-smith-2023.jpg',
             'https://storage.example.com/resumes/john-smith-cv.pdf',
             'https://linkedin.com/in/john-smith-cs',
             'https://github.com/johnsmith-dev',
             'https://johnsmith.dev',
             false,
             NULL,
             'FULL_SCHOLARSHIP',
             'Dean''s Scholarship for Academic Excellence',
             NULL,
             NULL,
             NULL,
             'Jane Smith',
             'Mother',
             '123 Main Street, Boston, MA 02115',
             'jane.smith@emergency.example.com',
             true,
             false,
             NULL,
             'Excellent student with strong programming skills in Java and Python. Active in ACM club.',
             1001,
             1001,
             'SYNCED',
             'SYNCED',
             '2024-01-15 10:30:00'
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
             student2_id,
             user2_id,
             '2022-IB-00456',
             'Maria',
             'Garcia',
             'Maria Garcia',
             '2001-08-22',
             'FEMALE',
             'Spanish',
             'Spain',
             'ID-ESP-987654321',
             'P-ESP-123456789',
             '456 Gran Via',
             'Madrid',
             'Madrid',
             'Spain',
             '28013',
             '+34-91-555-0123',
             '+34-91-555-0456',
             'maria.garcia.personal@example.com',
             school1_id,
             'Bachelor of Business Administration',
             'International Business',
             'Spanish Language & Culture',
             2022,
             2026,
             'ACTIVE',
             'JUNIOR',
             3.92,
             120,
             75,
             community1_id,
             club2_id,
             advisor1_id,
             'https://storage.example.com/profiles/maria-garcia-2022.jpg',
             'https://storage.example.com/resumes/maria-garcia-cv.pdf',
             'https://linkedin.com/in/maria-garcia-ib',
             'https://github.com/mariagarcia-biz',
             'https://mariagarcia-business.com',
             true,
             'F1_STUDENT_VISA',
             'PARTIAL_SCHOLARSHIP',
             'International Excellence Scholarship',
             NULL,
             NULL,
             'Asthma (managed with inhaler)',
             'Carlos Garcia',
             'Father',
             'Calle Alcalá 456, Madrid, Spain 28013',
             'carlos.garcia@emergency.example.com',
             true,
             false,
             NULL,
             'International student from Spain. Fluent in Spanish, English, and French. President of International Business Club.',
             1002,
             1002,
             'SYNCED',
             'PENDING',
             '2024-01-10 14:20:00'
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
             student3_id,
             user3_id,
             '2019-ME-00789',
             'David',
             'Chen',
             'David Chen',
             '1998-11-03',
             'MALE',
             'Chinese',
             'China',
             'ID-CHN-456789123',
             'P-CHN-321654987',
             '789 Market Street',
             'San Francisco',
             'California',
             'USA',
             '94102',
             '+1-415-555-0789',
             '+1-415-555-0987',
             'david.chen.personal@example.com',
             school1_id,
             'Bachelor of Engineering',
             'Mechanical Engineering',
             'Business Administration',
             2019,
             2023,
             'GRADUATED',
             'SENIOR',
             3.68,
             120,
             120,
             community1_id,
             club1_id,
             advisor1_id,
             'https://storage.example.com/profiles/david-chen-2019.jpg',
             'https://storage.example.com/resumes/david-chen-cv.pdf',
             'https://linkedin.com/in/david-chen-me',
             'https://github.com/davidchen-eng',
             'https://davidchen-engineering.com',
             true,
             'H1B_WORK_VISA',
             'FULL_SCHOLARSHIP',
             'Engineering Excellence Scholarship',
             'Corrective lenses (wears glasses)',
             'Additional time for written exams (approved by disability services)',
             NULL,
             'Lisa Chen',
             'Sister',
             '789 Market Street, San Francisco, CA 94102',
             'lisa.chen@emergency.example.com',
             false,
             true,
             '2023-05-15',
             'Graduated with honors. Senior project: "Design of sustainable energy system for campus buildings". Now working as Mechanical Engineer at Tesla.',
             1003,
             1003,
             'SYNCED',
             'FAILED',
             '2024-01-05 09:15:00'
         );
END $$;

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
CREATE INDEX idx_students_country ON students(country);
CREATE INDEX idx_students_gpa ON students(gpa);
CREATE INDEX idx_students_enrollment_year ON students(enrollment_year);

-- Create a view for active students
CREATE OR REPLACE VIEW active_students AS
SELECT
    id,
    student_code,
    full_name,
    program,
    major,
    academic_level,
    enrollment_status,
    gpa,
    is_international,
    country,
    created_at
FROM students
WHERE is_active = true;

-- Create a view for international students
CREATE OR REPLACE VIEW international_students AS
SELECT
    id,
    student_code,
    full_name,
    nationality,
    citizenship,
    visa_status,
    country,
    program,
    enrollment_status
FROM students
WHERE is_international = true;

-- Create a view for students needing sync
CREATE OR REPLACE VIEW students_needing_sync AS
SELECT
    id,
    student_code,
    full_name,
    user_sync_status,
    advisor_sync_status,
    last_sync_attempt,
    CASE
        WHEN user_sync_status = 'FAILED' OR advisor_sync_status = 'FAILED' THEN 'URGENT'
        WHEN user_sync_status = 'PENDING' OR advisor_sync_status = 'PENDING' THEN 'PENDING'
        ELSE 'SYNCED'
        END as sync_priority
FROM students
WHERE user_sync_status IN ('PENDING', 'FAILED')
   OR advisor_sync_status IN ('PENDING', 'FAILED');

-- Create a view for academic statistics
CREATE OR REPLACE VIEW student_academic_stats AS
SELECT
    program,
    COUNT(*) as total_students,
    ROUND(AVG(gpa), 2) as avg_gpa,
    MIN(gpa) as min_gpa,
    MAX(gpa) as max_gpa,
    SUM(CASE WHEN is_international = true THEN 1 ELSE 0 END) as international_count,
    SUM(CASE WHEN is_graduated = true THEN 1 ELSE 0 END) as graduated_count,
    SUM(completed_credits) as total_credits_completed
FROM students
WHERE is_active = true
GROUP BY program;

-- Create a view for student demographics
CREATE OR REPLACE VIEW student_demographics AS
SELECT
    country,
    COUNT(*) as total_students,
    SUM(CASE WHEN gender = 'MALE' THEN 1 ELSE 0 END) as male_count,
    SUM(CASE WHEN gender = 'FEMALE' THEN 1 ELSE 0 END) as female_count,
    SUM(CASE WHEN gender NOT IN ('MALE', 'FEMALE') THEN 1 ELSE 0 END) as other_gender_count,
    SUM(CASE WHEN is_international = true THEN 1 ELSE 0 END) as international_count,
    ROUND(AVG(EXTRACT(YEAR FROM age(CURRENT_DATE, date_of_birth))), 1) as avg_age
FROM students
WHERE is_active = true
GROUP BY country
ORDER BY total_students DESC;

-- Sample queries for verification
SELECT 'Total Students' as metric, COUNT(*) as value FROM students
UNION ALL
SELECT 'Active Students', COUNT(*) FROM students WHERE is_active = true
UNION ALL
SELECT 'International Students', COUNT(*) FROM students WHERE is_international = true
UNION ALL
SELECT 'Graduated Students', COUNT(*) FROM students WHERE is_graduated = true
UNION ALL
SELECT 'Average GPA', ROUND(AVG(gpa), 2) FROM students WHERE gpa IS NOT NULL
UNION ALL
SELECT 'Total Credits Completed', SUM(completed_credits) FROM students WHERE is_active = true;

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
    country,
    is_active
FROM students
ORDER BY enrollment_year DESC, last_name ASC;

-- Display sync status summary
SELECT
    user_sync_status,
    advisor_sync_status,
    COUNT(*) as student_count
FROM students
GROUP BY user_sync_status, advisor_sync_status
ORDER BY student_count DESC;