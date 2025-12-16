-- Create schema
CREATE SCHEMA IF NOT EXISTS student_schema;

-- Switch to student_schema
SET
search_path TO student_schema;

-- Drop tables if they exist
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS guardians CASCADE;
DROP TABLE IF EXISTS student_guardians CASCADE;
DROP TABLE IF EXISTS grades CASCADE;
DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS attendance CASCADE;

-- Create students table
CREATE TABLE students
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT NOT NULL UNIQUE, -- Reference to users table in user_db
    student_code      VARCHAR(20) UNIQUE,
    date_of_birth     DATE,
    gender            VARCHAR(10),
    address           TEXT,
    city              VARCHAR(100),
    country           VARCHAR(100),
    enrollment_date   DATE,
    graduation_date   DATE,
    current_grade     VARCHAR(20),
    status            VARCHAR(20) DEFAULT 'ACTIVE',
    emergency_contact VARCHAR(20),
    medical_notes     TEXT,
    created_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Create guardians table
CREATE TABLE guardians
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT, -- Reference to users table in user_db
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    email        VARCHAR(100),
    phone        VARCHAR(20),
    relationship VARCHAR(50),
    occupation   VARCHAR(100),
    address      TEXT,
    is_primary   BOOLEAN   DEFAULT FALSE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create student_guardians table (many-to-many relationship)
CREATE TABLE student_guardians
(
    student_id           BIGINT REFERENCES students (id) ON DELETE CASCADE,
    guardian_id          BIGINT REFERENCES guardians (id) ON DELETE CASCADE,
    relationship_type    VARCHAR(50),
    is_emergency_contact BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (student_id, guardian_id)
);

-- Create grades table
CREATE TABLE grades
(
    id               BIGSERIAL PRIMARY KEY,
    student_id       BIGINT REFERENCES students (id) ON DELETE CASCADE,
    subject          VARCHAR(100) NOT NULL,
    grade_value      VARCHAR(5),
    grade_percentage DECIMAL(5, 2),
    semester         VARCHAR(20),
    academic_year    VARCHAR(20),
    teacher_notes    TEXT,
    created_by       BIGINT,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create enrollments table
CREATE TABLE enrollments
(
    id              BIGSERIAL PRIMARY KEY,
    student_id      BIGINT REFERENCES students (id) ON DELETE CASCADE,
    course_id       BIGINT,
    section         VARCHAR(20),
    enrollment_date DATE        DEFAULT CURRENT_DATE,
    status          VARCHAR(20) DEFAULT 'ENROLLED',
    created_at      TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Create attendance table
CREATE TABLE attendance
(
    id              BIGSERIAL PRIMARY KEY,
    student_id      BIGINT REFERENCES students (id) ON DELETE CASCADE,
    attendance_date DATE        NOT NULL,
    status          VARCHAR(20) NOT NULL, -- PRESENT, ABSENT, LATE, EXCUSED
    reason          TEXT,
    recorded_by     BIGINT,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (student_id, attendance_date)
);

-- Insert test data
INSERT INTO students (user_id, student_code, date_of_birth, gender, address, city, country, enrollment_date,
                      current_grade, status)
VALUES (4, 'STU2024001', '2008-05-15', 'FEMALE', '123 Main St', 'New York', 'USA', '2024-09-01', '10th Grade',
        'ACTIVE'),
       (5, 'STU2024002', '2009-07-22', 'MALE', '456 Oak Ave', 'Los Angeles', 'USA', '2024-09-01', '9th Grade',
        'ACTIVE');

INSERT INTO guardians (user_id, first_name, last_name, email, phone, relationship, is_primary)
VALUES (6, 'Sarah', 'Miller', 'sarah.miller@email.com', '+1234567895', 'MOTHER', TRUE),
       (NULL, 'Robert', 'Miller', 'robert.miller@email.com', '+1234567896', 'FATHER', FALSE);

INSERT INTO student_guardians (student_id, guardian_id, relationship_type, is_emergency_contact)
VALUES (1, 1, 'MOTHER', TRUE),
       (1, 2, 'FATHER', FALSE),
       (2, 1, 'MOTHER', TRUE);

INSERT INTO grades (student_id, subject, grade_value, grade_percentage, semester, academic_year)
VALUES (1, 'Mathematics', 'A', 95.5, 'Fall 2024', '2024-2025'),
       (1, 'Science', 'B+', 88.0, 'Fall 2024', '2024-2025'),
       (2, 'Mathematics', 'B', 85.0, 'Fall 2024', '2024-2025');

INSERT INTO attendance (student_id, attendance_date, status)
VALUES (1, '2024-12-01', 'PRESENT'),
       (1, '2024-12-02', 'PRESENT'),
       (1, '2024-12-03', 'ABSENT'),
       (2, '2024-12-01', 'PRESENT'),
       (2, '2024-12-02', 'LATE'),
       (2, '2024-12-03', 'PRESENT');

-- Create indexes
CREATE INDEX idx_students_user_id ON students (user_id);
CREATE INDEX idx_students_student_code ON students (student_code);
CREATE INDEX idx_grades_student_id ON grades (student_id);
CREATE INDEX idx_attendance_student_date ON attendance (student_id, attendance_date);
CREATE INDEX idx_guardians_user_id ON guardians (user_id);

-- Grant privileges
GRANT
ALL
PRIVILEGES
ON
ALL
TABLES IN SCHEMA student_schema TO student_admin;
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA student_schema TO student_admin;
GRANT USAGE ON SCHEMA
student_schema TO student_admin;