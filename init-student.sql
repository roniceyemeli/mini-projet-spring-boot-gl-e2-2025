-- init-student.sql
-- Create schema and initialize student data
-- Compatible with refactored Student entity class

-- Create schema if not exists
CREATE SCHEMA IF NOT EXISTS student_schema;

-- Switch to student_schema
SET search_path TO student_schema;

-- Drop table if exists (for clean initialization)
DROP TABLE IF EXISTS students CASCADE;

-- Create students table (matching the refactored Java entity)
CREATE TABLE students (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL UNIQUE,
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
                          email VARCHAR(100),
                          personal_email VARCHAR(100),
                          school_id BIGINT,
                          program VARCHAR(100),
                          major VARCHAR(100),
                          minor VARCHAR(100),
                          enrollment_year INTEGER,
                          expected_graduation_year INTEGER,
                          enrollment_status VARCHAR(20) DEFAULT 'ACTIVE',
                          academic_level VARCHAR(50),
                          gpa NUMERIC(3, 1), -- Changed from NUMERIC(3,2) to NUMERIC(3,1) for 3.5, 4.0 values
                          total_credits INTEGER DEFAULT 0,
                          completed_credits INTEGER DEFAULT 0,
                          community_id BIGINT,
                          club_id BIGINT,
                          advisor_id BIGINT,
                          profile_picture VARCHAR(500),
                          resume_url VARCHAR(500),
                          linkedin_url VARCHAR(500),
                          github_url VARCHAR(500),
                          portfolio_url VARCHAR(500),
                          is_international BOOLEAN DEFAULT FALSE,
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
                          is_active BOOLEAN DEFAULT TRUE,
                          is_graduated BOOLEAN DEFAULT FALSE,
                          graduation_date DATE,
                          notes TEXT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          created_by BIGINT,
                          updated_by BIGINT,

    -- Foreign key constraint to users table in user_schema
                          CONSTRAINT fk_student_user FOREIGN KEY (user_id)
                              REFERENCES user_schema.users(id) ON DELETE CASCADE,

    -- Constraint to ensure expected_graduation_year is after enrollment_year
                          CONSTRAINT chk_graduation_year CHECK (
                              expected_graduation_year IS NULL OR
                              enrollment_year IS NULL OR
                              expected_graduation_year >= enrollment_year
                              ),

    -- Constraint for valid GPA range (0.0 to 4.0)
                          CONSTRAINT chk_gpa_range CHECK (
                              gpa IS NULL OR
                              (gpa >= 0.0 AND gpa <= 4.0)
                              ),

    -- Constraint for credits
                          CONSTRAINT chk_credits CHECK (
                              completed_credits <= total_credits
                              )
);

-- Insert student data (matching existing users from user_schema)
-- Note: We'll only create students for existing users (IDs 4 and 5)
-- Additional users (7, 8, 9) should be created in user_schema first
INSERT INTO students (
    user_id,
    student_code,
    first_name,
    last_name,
    full_name,
    date_of_birth,
    gender,
    nationality,
    citizenship,
    id_card_number,
    passport_number,
    address,
    city,
    state,
    country,
    postal_code,
    phone_number,
    emergency_phone,
    email,
    personal_email,
    school_id,
    program,
    major,
    minor,
    enrollment_year,
    expected_graduation_year,
    enrollment_status,
    academic_level,
    gpa,
    total_credits,
    completed_credits,
    community_id,
    club_id,
    advisor_id,
    profile_picture,
    resume_url,
    linkedin_url,
    github_url,
    portfolio_url,
    is_international,
    visa_status,
    financial_aid_status,
    scholarship_name,
    disabilities,
    special_needs,
    medical_conditions,
    emergency_contact_name,
    emergency_contact_relationship,
    emergency_contact_address,
    emergency_contact_email,
    is_active,
    is_graduated,
    graduation_date,
    notes,
    created_at,
    updated_at,
    created_by,
    updated_by
) VALUES
-- Student 1: Alice Smith (user_id = 4 from user_schema.users - existing user)
(
    4, -- user_id from user_schema.users (Alice Smith)
    '2023-001-54321',
    'Alice',
    'Smith',
    'Alice Smith',
    '2002-05-15',
    'FEMALE',
    'American',
    'USA',
    'S-001-2002',
    'P12345678',
    '123 Main Street',
    'New York',
    'NY',
    'USA',
    '10001',
    '+1234567893',
    '+1987654321',
    'student.alice@school.com',
    'alice.smith.personal@example.com',
    1,
    'Computer Science',
    'Software Engineering',
    'Mathematics',
    2023,
    2027,
    'ACTIVE',
    'SOPHOMORE',
    3.8, -- Changed to 3.8 (compatible with NUMERIC(3,1))
    120,
    45,
    1,
    3,
    3, -- advisor_id (teacher John Doe - user_id 3)
    'https://example.com/profiles/alice.jpg',
    'https://example.com/resumes/alice.pdf',
    'https://linkedin.com/in/alicesmith',
    'https://github.com/alicesmith',
    'https://alice-portfolio.com',
    false,
    NULL,
    'PARTIAL',
    'Dean''s Scholarship',
    NULL,
    'Requires extra time for exams',
    'Asthma (mild)',
    'Robert Smith',
    'Father',
    '123 Main Street, New York, NY 10001',
    'robert.smith@example.com',
    true,
    false,
    NULL,
    'Honor roll student, active in coding club',
    '2023-08-15 09:00:00',
    '2024-01-10 14:30:00',
    1, -- created_by: superadmin (user_id 1)
    1  -- updated_by: superadmin (user_id 1)
),

-- Student 2: Bob Johnson (user_id = 5 from user_schema.users - existing user)
(
    5, -- user_id from user_schema.users (Bob Johnson)
    '2023-001-54322',
    'Bob',
    'Johnson',
    'Bob Johnson',
    '2003-02-28',
    'MALE',
    'Canadian',
    'Canada',
    'S-002-2003',
    'P87654321',
    '456 Oak Avenue',
    'Toronto',
    'ON',
    'Canada',
    'M5H 2N2',
    '+1234567894',
    '+14165551234',
    'student.bob@school.com',
    'bob.johnson.personal@example.com',
    1,
    'Business Administration',
    'Marketing',
    'Psychology',
    2023,
    2027,
    'ACTIVE',
    'SOPHOMORE',
    3.2, -- Changed to 3.2
    120,
    42,
    2,
    1,
    3, -- advisor_id (teacher John Doe - user_id 3)
    'https://example.com/profiles/bob.jpg',
    'https://example.com/resumes/bob.pdf',
    'https://linkedin.com/in/bobjohnson',
    'https://github.com/bobjohnson',
    NULL,
    true,
    'F-1 Student Visa',
    'NONE',
    NULL,
    NULL,
    'Needs note-taking assistance',
    NULL,
    'Mary Johnson',
    'Mother',
    '456 Oak Avenue, Toronto, ON M5H 2N2',
    'mary.johnson@example.com',
    true,
    false,
    NULL,
    'International student, member of business club',
    '2023-08-16 10:15:00',
    '2024-01-12 11:20:00',
    1,
    1
),

-- Student 3: Create a new student with a new user (we'll create user_id 6 as another student)
-- First, we need to add this user to user_schema.users. Let's create a placeholder.
-- Assuming we'll run this after creating additional users
(
    6, -- New student user (if created in user_schema)
    '2023-001-54326',
    'Carol',
    'Williams',
    'Carol Williams',
    '2002-08-10',
    'FEMALE',
    'American',
    'USA',
    'S-003-2002',
    'P33445566',
    '789 Pine Street',
    'Chicago',
    'IL',
    'USA',
    '60601',
    '+13125551234',
    '+13125551235',
    'carol.williams@school.com',
    'carol.williams@example.com',
    1,
    'Biology',
    'Pre-Medicine',
    'Chemistry',
    2023,
    2027,
    'ACTIVE',
    'SOPHOMORE',
    3.9,
    120,
    48,
    3,
    2,
    3, -- advisor_id (teacher John Doe)
    'https://example.com/profiles/carol.jpg',
    'https://example.com/resumes/carol.pdf',
    'https://linkedin.com/in/carolwilliams',
    'https://github.com/carolw',
    'https://carol-med-portfolio.com',
    false,
    NULL,
    'FULL',
    'Medical Scholars Program',
    NULL,
    NULL,
    NULL,
    'David Williams',
    'Father',
    '789 Pine Street, Chicago, IL 60601',
    'david.williams@example.com',
    true,
    false,
    NULL,
    'Pre-med student, volunteer at local hospital',
    '2023-08-17 11:30:00',
    '2024-01-14 15:20:00',
    2, -- created_by: admin
    2  -- updated_by: admin
),

-- Student 4: Another new student
(
    10, -- Assuming user_id 10 will be created
    '2024-001-54327',
    'David',
    'Chen',
    'David Chen',
    '2004-03-25',
    'MALE',
    'Chinese',
    'China',
    'S-004-2004',
    'P66778899',
    '101 Tech Park',
    'Shanghai',
    NULL,
    'China',
    '200000',
    '+862112345678',
    '+862112345679',
    'david.chen@school.com',
    'david.chen@example.cn',
    1,
    'Computer Science',
    'Artificial Intelligence',
    'Statistics',
    2024,
    2028,
    'ACTIVE',
    'FRESHMAN',
    3.7,
    120,
    18,
    4,
    1,
    3, -- advisor_id (teacher John Doe)
    'https://example.com/profiles/david.jpg',
    NULL,
    'https://linkedin.com/in/davidchen',
    'https://github.com/davidchen',
    'https://david-ai-portfolio.com',
    true,
    'X1 Student Visa',
    'PARTIAL',
    'International STEM Scholarship',
    NULL,
    'ESL support',
    NULL,
    'Wei Chen',
    'Father',
    '101 Tech Park, Shanghai, China 200000',
    'wei.chen@example.cn',
    true,
    false,
    NULL,
    'International student, programming competition winner',
    '2024-08-25 10:00:00',
    '2024-12-10 14:15:00',
    2,
    2
),

-- Student 5: Another new student with different status
(
    11, -- Assuming user_id 11 will be created
    '2022-001-54328',
    'Emma',
    'Garcia',
    'Emma Garcia',
    '2001-12-05',
    'FEMALE',
    'Mexican',
    'Mexico',
    'S-005-2001',
    'P22334455',
    '555 Sunset Blvd',
    'Los Angeles',
    'CA',
    'USA',
    '90001',
    '+13235551234',
    '+13235551235',
    'emma.garcia@school.com',
    'emma.garcia@example.com',
    1,
    'Political Science',
    'International Relations',
    'Economics',
    2022,
    2026,
    'ACTIVE',
    'JUNIOR',
    3.4,
    130,
    82,
    2,
    4,
    3, -- advisor_id (teacher John Doe)
    'https://example.com/profiles/emma.jpg',
    'https://example.com/resumes/emma.pdf',
    'https://linkedin.com/in/emmagarcia',
    NULL,
    'https://emma-polisci-portfolio.com',
    false,
    NULL,
    'NONE',
    NULL,
    NULL,
    'Access to recording lectures',
    'Seasonal allergies',
    'Maria Garcia',
    'Mother',
    '555 Sunset Blvd, Los Angeles, CA 90001',
    'maria.garcia@example.com',
    true,
    false,
    NULL,
    'Student government president, Model UN participant',
    '2022-09-05 08:45:00',
    '2024-01-20 09:30:00',
    1,
    1
);

-- Create indexes for performance (matching common query patterns)
CREATE INDEX idx_students_user_id ON students(user_id);
CREATE INDEX idx_students_student_code ON students(student_code);
CREATE INDEX idx_students_school_id ON students(school_id);
CREATE INDEX idx_students_enrollment_status ON students(enrollment_status);
CREATE INDEX idx_students_academic_level ON students(academic_level);
CREATE INDEX idx_students_is_active ON students(is_active);
CREATE INDEX idx_students_is_graduated ON students(is_graduated);
CREATE INDEX idx_students_advisor_id ON students(advisor_id);
CREATE INDEX idx_students_community_id ON students(community_id);
CREATE INDEX idx_students_club_id ON students(club_id);
CREATE INDEX idx_students_is_international ON students(is_international);

-- Create unique constraints for sensitive data (with NULL handling)
CREATE UNIQUE INDEX idx_unique_id_card ON students(id_card_number) WHERE id_card_number IS NOT NULL;
CREATE UNIQUE INDEX idx_unique_passport ON students(passport_number) WHERE passport_number IS NOT NULL;

-- Create index for full name search
CREATE INDEX idx_students_full_name ON students(full_name);

-- Create index for email (personal email)
CREATE INDEX idx_students_personal_email ON students(personal_email);

-- Create index for date ranges
CREATE INDEX idx_students_enrollment_year ON students(enrollment_year);
CREATE INDEX idx_students_expected_graduation_year ON students(expected_graduation_year);
CREATE INDEX idx_students_graduation_date ON students(graduation_date);
CREATE INDEX idx_students_date_of_birth ON students(date_of_birth);

-- Create composite indexes for common queries
CREATE INDEX idx_students_status_active ON students(enrollment_status, is_active, is_graduated);
CREATE INDEX idx_students_program_major ON students(program, major);
CREATE INDEX idx_students_credits ON students(completed_credits, total_credits);

-- Update academic levels based on completed credits (matching Java entity logic)
UPDATE students
SET academic_level = 'SENIOR'
WHERE completed_credits >= 90;

UPDATE students
SET academic_level = 'JUNIOR'
WHERE completed_credits >= 60 AND completed_credits < 90;

UPDATE students
SET academic_level = 'SOPHOMORE'
WHERE completed_credits >= 30 AND completed_credits < 60;

UPDATE students
SET academic_level = 'FRESHMAN'
WHERE completed_credits < 30 OR academic_level IS NULL;

-- Add a trigger to automatically update academic_level when completed_credits change
CREATE OR REPLACE FUNCTION update_student_academic_level()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.completed_credits IS DISTINCT FROM OLD.completed_credits THEN
        IF NEW.completed_credits >= 90 THEN
            NEW.academic_level := 'SENIOR';
        ELSIF NEW.completed_credits >= 60 THEN
            NEW.academic_level := 'JUNIOR';
        ELSIF NEW.completed_credits >= 30 THEN
            NEW.academic_level := 'SOPHOMORE';
ELSE
            NEW.academic_level := 'FRESHMAN';
END IF;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_academic_level
    BEFORE UPDATE ON students
    FOR EACH ROW
    EXECUTE FUNCTION update_student_academic_level();

-- Grant privileges (adjust based on your user)
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA student_schema TO student_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA student_schema TO student_admin;
GRANT USAGE ON SCHEMA student_schema TO student_admin;

-- Add comment to table
COMMENT ON TABLE students IS 'Stores student academic and personal information linked to user accounts';

-- Display inserted data for verification
SELECT
    id,
    user_id,
    student_code,
    full_name,
    program,
    major,
    enrollment_status,
    academic_level,
    gpa,
    completed_credits || '/' || total_credits as credits,
    is_active,
    is_international,
    is_graduated
FROM students
ORDER BY id;

-- Display summary statistics
SELECT
    COUNT(*) as total_students,
    COUNT(CASE WHEN is_active THEN 1 END) as active_students,
    COUNT(CASE WHEN is_international THEN 1 END) as international_students,
    COUNT(CASE WHEN is_graduated THEN 1 END) as graduated_students,
    ROUND(AVG(gpa), 2) as average_gpa,
    AVG(completed_credits) as avg_completed_credits
FROM students;

-- Reset search path
RESET search_path;