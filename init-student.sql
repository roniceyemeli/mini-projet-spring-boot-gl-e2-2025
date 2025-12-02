-- Student database initialization
CREATE SCHEMA IF NOT EXISTS student_schema;

-- Create tables for student module
CREATE TABLE IF NOT EXISTS student_schema.students (
    id BIGSERIAL PRIMARY KEY,
    student_code VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20),
    date_of_birth DATE,
    enrollment_date DATE DEFAULT CURRENT_DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS student_schema.courses (
    id BIGSERIAL PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_name VARCHAR(100) NOT NULL,
    credits INTEGER NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS student_schema.enrollments (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES student_schema.students(id),
    course_id BIGINT REFERENCES student_schema.courses(id),
    enrollment_date DATE DEFAULT CURRENT_DATE,
    grade VARCHAR(2),
    UNIQUE(student_id, course_id)
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_students_email ON student_schema.students(email);
CREATE INDEX IF NOT EXISTS idx_enrollments_student ON student_schema.enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollments_course ON student_schema.enrollments(course_id);