-- Create schema
CREATE SCHEMA IF NOT EXISTS school_schema;

SET
search_path TO school_schema;

-- Drop tables if they exist
DROP TABLE IF EXISTS schools CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS classrooms CASCADE;
DROP TABLE IF EXISTS school_facilities CASCADE;

-- Create schools table (with all fields from your Entity)
CREATE TABLE schools
(
    id                        BIGSERIAL PRIMARY KEY,
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
    created_by                BIGINT,
    updated_by                BIGINT,

    -- Add computed columns for business logic
    is_operational            BOOLEAN GENERATED ALWAYS AS (
        status = 'ACTIVE' AND is_active = TRUE
        ) STORED,
    is_accredited             BOOLEAN GENERATED ALWAYS AS (
        accreditation_status = 'ACCREDITED' AND
        (accreditation_expiry_date IS NULL OR accreditation_expiry_date > CURRENT_TIMESTAMP)
        ) STORED,
    can_admit_students        BOOLEAN GENERATED ALWAYS AS (
        is_operational AND is_accredited
        ) STORED
);

-- Insert sample schools
INSERT INTO schools (name, title, description, slug, email, website, phone_number,
                     founding_year, type, accreditation_number, country, city,
                     total_students, total_teachers, is_public, motto, vision, mission,
                     is_featured, verified, created_by)
VALUES ('Greenwood International School',
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
        2),
       ('Springfield University',
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
        2),
       ('Tech Valley High School',
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
        3),
       ('Community Arts Academy',
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
        4);

-- Create indexes for performance
CREATE INDEX idx_schools_email ON schools (email);
CREATE INDEX idx_schools_status ON schools (status);
CREATE INDEX idx_schools_type ON schools (type);
CREATE INDEX idx_schools_city ON schools (city);
CREATE INDEX idx_schools_country ON schools (country);
CREATE INDEX idx_schools_is_featured ON schools (is_featured) WHERE is_featured = TRUE;
CREATE INDEX idx_schools_verified ON schools (verified) WHERE verified = TRUE;


-- Grant privileges
GRANT
ALL
PRIVILEGES
ON
ALL
TABLES IN SCHEMA school_schema TO school_admin;
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA school_schema TO school_admin;
GRANT USAGE ON SCHEMA
school_schema TO school_admin;
