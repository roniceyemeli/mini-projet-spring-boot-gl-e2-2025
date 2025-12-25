-- Create schema
CREATE SCHEMA IF NOT EXISTS school_schema;

SET search_path TO school_schema;

-- Drop tables if they exist
DROP TABLE IF EXISTS schools CASCADE;

-- Create schools table (with all fields from your Entity)
CREATE TABLE schools
(
    id                        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name                      VARCHAR(200) NOT NULL,
    title                     VARCHAR(200),
    description               TEXT,
    slug                      VARCHAR(255) UNIQUE,
    address                   VARCHAR(500),
    full_address              VARCHAR(500),
    email                     VARCHAR(100) NOT NULL UNIQUE,
    website                   VARCHAR(100),
    phone_number              VARCHAR(20),
    fax_number                VARCHAR(20),
    founding_year             INT,
    type                      VARCHAR(20) DEFAULT 'UNIVERSITY',
    status                    VARCHAR(20) DEFAULT 'ACTIVE',
    accreditation_number      VARCHAR(100),
    tax_id                    VARCHAR(50),
    registration_number       VARCHAR(100),
    logo_url                  VARCHAR(500),
    banner_url                VARCHAR(500),
    country                   VARCHAR(100),
    city                      VARCHAR(100),
    postal_code               VARCHAR(20),
    latitude                  DECIMAL(10, 8),
    longitude                 DECIMAL(11, 8),
    total_students            INT         DEFAULT 0,
    total_teachers            INT         DEFAULT 0,
    total_staff               INT         DEFAULT 0,
    is_public                 BOOLEAN     DEFAULT TRUE,
    tuition_range             VARCHAR(100),
    admissions_email          VARCHAR(100),
    admissions_phone          VARCHAR(20),
    contact_person            VARCHAR(100),
    contact_position          VARCHAR(100),
    facebook_url              VARCHAR(500),
    twitter_url               VARCHAR(500),
    linkedin_url              VARCHAR(500),
    instagram_url             VARCHAR(500),
    is_featured               BOOLEAN     DEFAULT FALSE,
    ranking                   INT,
    accreditation_status      VARCHAR(50),
    accreditation_expiry_date TIMESTAMP,
    motto                     VARCHAR(500),
    vision                    TEXT,
    mission                   TEXT,
    is_active                 BOOLEAN     DEFAULT TRUE,
    verified                  BOOLEAN     DEFAULT FALSE,
    verification_date         TIMESTAMP,
    created_at                TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    created_by                UUID,
    updated_by                UUID
);

-- Insert sample schools with proper UUIDs
DO $$
DECLARE
    -- School IDs
school1_id UUID := 'a1b2c3d4-e5f6-7890-abcd-ef1234567890';
    school2_id UUID := 'b2c3d4e5-f6a7-8901-bcde-f23456789012';
    school3_id UUID := 'c3d4e5f6-a7b8-9012-cdef-345678901234';
    school4_id UUID := 'd4e5f6a7-b8c9-0123-def0-456789012345';

    -- Creator IDs (assuming these are real user UUIDs from your system)
    creator_admin UUID := '5c6d7e8f-90ab-cdef-0123-456789abcdef';
    creator_user1 UUID := '6d7e8f90-abcd-ef01-2345-6789abcdef01';
    creator_user2 UUID := '7e8f90ab-cdef-0123-4567-89abcdef0123';
    creator_user3 UUID := '8f90abcd-ef01-2345-6789-abcdef012345';
BEGIN
    -- Insert sample schools with real UUIDs
INSERT INTO schools (id, name, title, description, slug, email, website, phone_number,
                     founding_year, type, accreditation_number, country, city,
                     total_students, total_teachers, is_public, motto, vision, mission,
                     is_featured, verified, created_by, status, is_active,
                     address, facebook_url, linkedin_url, accreditation_status)
VALUES
    (school1_id,
     'Greenwood International School',
     'Greenwood International School - Excellence in Education',
     'Greenwood International School is a premier educational institution committed to academic excellence, character development, and global citizenship. We offer a comprehensive curriculum from kindergarten through high school.',
     'greenwood-international-school',
     'info@greenwood.edu',
     'https://www.greenwood.edu',
     '+1-555-0201',
     1995,
     'K12',
     'ACC-2024-GIS001',
     'USA',
     'Springfield',
     1500,
     120,
     FALSE,
     'Knowledge, Integrity, Excellence',
     'To be a world-class institution that nurtures innovative thinkers and responsible global citizens.',
     'To provide a transformative educational experience that empowers students to achieve their full potential and make meaningful contributions to society.',
     TRUE,
     TRUE,
     creator_admin,
     'ACTIVE',
     TRUE,
     '123 Education Blvd, Springfield, IL 62704',
     'https://facebook.com/greenwood.edu',
     'https://linkedin.com/school/greenwood',
     'ACCREDITED'),

    (school2_id,
     'Springfield University',
     'Springfield University - Advancing Knowledge',
     'A comprehensive public university offering undergraduate, graduate, and professional programs across various disciplines. Known for research excellence and community engagement.',
     'springfield-university',
     'admissions@springfield.edu',
     'https://www.springfield.edu',
     '+1-555-0202',
     1950,
     'UNIVERSITY',
     'ACC-2024-SU002',
     'USA',
     'Springfield',
     25000,
     1500,
     TRUE,
     'Truth and Wisdom',
     'To be a leading research university that transforms lives through education, discovery, and engagement.',
     'To provide accessible, high-quality education, foster groundbreaking research, and serve our communities.',
     TRUE,
     TRUE,
     creator_admin,
     'ACTIVE',
     TRUE,
     '456 University Ave, Springfield, IL 62705',
     'https://facebook.com/springfield.university',
     'https://linkedin.com/school/springfield-university',
     'ACCREDITED'),

    (school3_id,
     'Tech Valley High School',
     'Tech Valley High - STEM Excellence',
     'A specialized high school focusing on Science, Technology, Engineering, and Mathematics (STEM) education. Partnered with local tech industries for real-world learning experiences.',
     'tech-valley-high-school',
     'admissions@techvalley.edu',
     'https://www.techvalley.edu',
     '+1-555-0203',
     2005,
     'HIGH_SCHOOL',
     'ACC-2024-TVHS003',
     'USA',
     'Tech City',
     800,
     65,
     TRUE,
     'Innovate, Create, Elevate',
     'To prepare the next generation of innovators and problem-solvers for the challenges of tomorrow.',
     'To provide a rigorous STEM-focused education that develops critical thinking and practical skills.',
     FALSE,
     TRUE,
     creator_user1,
     'ACTIVE',
     TRUE,
     '789 Innovation Drive, Tech City, CA 94025',
     'https://facebook.com/techvalleyhigh',
     'https://linkedin.com/school/tech-valley-high',
     'PROVISIONAL'),

    (school4_id,
     'Community Arts Academy',
     'Community Arts Academy - Nurturing Creativity',
     'A private arts academy offering specialized programs in visual arts, music, dance, and theater for students of all ages.',
     'community-arts-academy',
     'info@artsacademy.edu',
     'https://www.artsacademy.edu',
     '+1-555-0204',
     1980,
     'ACADEMY',
     'ACC-2024-CAA004',
     'USA',
     'Artsville',
     400,
     40,
     FALSE,
     'Art Inspires Life',
     'To cultivate artistic excellence and creative expression in a supportive community.',
     'To provide comprehensive arts education that fosters talent, creativity, and personal growth.',
     FALSE,
     TRUE,
     creator_user2,
     'ACTIVE',
     TRUE,
     '321 Creativity Lane, Artsville, NY 10001',
     'https://facebook.com/communityartsacademy',
     'https://linkedin.com/school/community-arts-academy',
     'ACCREDITED');
END $$;

-- Update additional fields with realistic data
UPDATE schools
SET
    full_address = CASE
                       WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN '123 Education Blvd, Springfield, Illinois 62704, United States'
                       WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN '456 University Avenue, Springfield, Illinois 62705, United States'
                       WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN '789 Innovation Drive, Tech City, California 94025, United States'
                       WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN '321 Creativity Lane, Artsville, New York 10001, United States'
        END,
    postal_code = CASE
                      WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN '62704'
                      WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN '62705'
                      WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN '94025'
                      WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN '10001'
        END,
    latitude = CASE
                   WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN 39.781721
                   WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN 39.782500
                   WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN 37.386051
                   WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN 40.748817
        END,
    longitude = CASE
                    WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN -89.650148
                    WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN -89.651000
                    WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN -122.083855
                    WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN -73.985428
        END,
    tuition_range = CASE
                        WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN '$15,000 - $25,000'
                        WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN '$8,000 - $12,000'
                        WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN 'Free (Public)'
                        WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN '$10,000 - $18,000'
        END,
    admissions_email = CASE
                           WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN 'admissions@greenwood.edu'
                           WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN 'admissions@springfield.edu'
                           WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN 'admissions@techvalley.edu'
                           WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN 'admissions@artsacademy.edu'
        END,
    admissions_phone = CASE
                           WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN '+1-555-0201'
                           WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN '+1-555-0202'
                           WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN '+1-555-0203'
                           WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN '+1-555-0204'
        END,
    contact_person = CASE
                         WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN 'Dr. Sarah Johnson'
                         WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN 'Prof. Michael Chen'
                         WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN 'Ms. Elena Rodriguez'
                         WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN 'Mr. David Wilson'
        END,
    contact_position = CASE
                           WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN 'Principal'
                           WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN 'Dean of Admissions'
                           WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN 'Director'
                           WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN 'Artistic Director'
        END,
    accreditation_expiry_date = CASE
                                    WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN '2026-12-31 23:59:59'
                                    WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN '2027-06-30 23:59:59'
                                    WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN '2025-12-31 23:59:59'
                                    WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN '2026-09-30 23:59:59'
        END,
    verification_date = CASE
                            WHEN id = 'a1b2c3d4-e5f6-7890-abcd-ef1234567890' THEN '2024-01-15 10:30:00'
                            WHEN id = 'b2c3d4e5-f6a7-8901-bcde-f23456789012' THEN '2024-02-20 14:45:00'
                            WHEN id = 'c3d4e5f6-a7b8-9012-cdef-345678901234' THEN '2024-03-10 09:15:00'
                            WHEN id = 'd4e5f6a7-b8c9-0123-def0-456789012345' THEN '2024-01-25 11:20:00'
        END;

-- Create indexes for performance
CREATE INDEX idx_schools_email ON schools (email);
CREATE INDEX idx_schools_status ON schools (status);
CREATE INDEX idx_schools_type ON schools (type);
CREATE INDEX idx_schools_city ON schools (city);
CREATE INDEX idx_schools_country ON schools (country);
CREATE INDEX idx_schools_is_featured ON schools (is_featured) WHERE is_featured = TRUE;
CREATE INDEX idx_schools_verified ON schools (verified) WHERE verified = TRUE;
CREATE INDEX idx_schools_created_by ON schools (created_by);
CREATE INDEX idx_schools_slug ON schools (slug);
CREATE INDEX idx_schools_is_active ON schools (is_active) WHERE is_active = TRUE;

-- Create a view for active schools summary
CREATE VIEW active_schools_summary AS
SELECT
    id,
    name,
    type,
    status,
    country,
    city,
    total_students,
    total_teachers,
    is_public,
    is_featured,
    verified,
    created_at
FROM schools
WHERE is_active = TRUE;

-- Create a view for school statistics
CREATE VIEW school_statistics AS
SELECT
    type,
    COUNT(*) as total_schools,
    SUM(total_students) as total_students,
    SUM(total_teachers) as total_teachers,
    AVG(total_students) as avg_students_per_school,
    MIN(founding_year) as oldest_founding_year,
    MAX(founding_year) as newest_founding_year
FROM schools
WHERE is_active = TRUE
GROUP BY type;

-- Grant privileges
GRANT USAGE ON SCHEMA school_schema TO school_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA school_schema TO school_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA school_schema TO school_admin;
GRANT SELECT ON active_schools_summary TO school_admin;
GRANT SELECT ON school_statistics TO school_admin;