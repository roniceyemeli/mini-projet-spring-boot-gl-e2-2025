-- Create schema
CREATE SCHEMA IF NOT EXISTS community_schema;

SET search_path TO community_schema;

-- Drop existing tables and views
DROP TABLE IF EXISTS communities CASCADE;

-- Create communities table (with all fields from your Entity)
CREATE TABLE communities (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             title VARCHAR(255) NOT NULL,
                             description TEXT,
                             slug VARCHAR(255) UNIQUE,
                             website VARCHAR(255),
                             contact_email VARCHAR(255),
                             contact_phone VARCHAR(255),
                             founding_year INT,
                             member_count INT DEFAULT 0,
                             is_active BOOLEAN DEFAULT TRUE,
                             logo_url VARCHAR(255),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             created_by UUID
);

-- Insert sample communities with UUIDs
INSERT INTO communities (id, title, description, slug, website, contact_email, contact_phone,
                         founding_year, member_count, is_active, created_by)
VALUES
    ('11111111-1111-1111-1111-111111111111',
     'Greenwood Parents Association',
     'A community for parents of Greenwood International School students to share information, organize events, and support school activities.',
     'greenwood-parents-association',
     'https://parents.greenwood.edu',
     'parents@greenwood.edu',
     '+1-555-0301',
     2000,
     250,
     TRUE,
     '00000000-0000-0000-0000-000000000006'),
    ('22222222-2222-2222-2222-222222222222',
     'Springfield Alumni Network',
     'Connect with fellow alumni from Springfield University. Network, share opportunities, and stay updated on university events.',
     'springfield-alumni-network',
     'https://alumni.springfield.edu',
     'alumni@springfield.edu',
     '+1-555-0302',
     1960,
     5000,
     TRUE,
     '00000000-0000-0000-0000-000000000002'),
    ('33333333-3333-3333-3333-333333333333',
     'Tech Enthusiasts Club',
     'A community for technology enthusiasts, programmers, and innovators. Regular meetups, hackathons, and tech talks.',
     'tech-enthusiasts-club',
     'https://techclub.org',
     'contact@techclub.org',
     '+1-555-0303',
     2015,
     150,
     TRUE,
     '00000000-0000-0000-0000-000000000003'),
    ('44444444-4444-4444-4444-444444444444',
     'Community Arts Collective',
     'Bringing together artists, musicians, and creatives from the local community. Exhibitions, workshops, and collaborative projects.',
     'community-arts-collective',
     'https://artscollective.org',
     'info@artscollective.org',
     '+1-555-0304',
     2010,
     120,
     TRUE,
     '00000000-0000-0000-0000-000000000004'),
    ('55555555-5555-5555-5555-555555555555',
     'Student Environmental Alliance',
     'Student-led initiative focused on environmental awareness, sustainability projects, and community cleanups.',
     'student-environmental-alliance',
     'https://sea.greenwood.edu',
     'sea@greenwood.edu',
     '+1-555-0305',
     2018,
     80,
     TRUE,
     '00000000-0000-0000-0000-000000000004');

-- Create indexes for performance
CREATE INDEX idx_communities_slug ON communities (slug);
CREATE INDEX idx_communities_is_active ON communities (is_active) WHERE is_active = TRUE;
CREATE INDEX idx_communities_member_count ON communities (member_count);
CREATE INDEX idx_communities_created_by ON communities (created_by);

-- Grant privileges
GRANT USAGE ON SCHEMA community_schema TO community_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA community_schema TO community_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA community_schema TO community_admin;