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

-- Insert sample communities with proper UUIDs
DO $$
DECLARE
    -- Community IDs
community1_id UUID := 'a7b8c9d0-e1f2-3456-7890-abcd12345678';
    community2_id UUID := 'b8c9d0e1-f2a3-4567-8901-bcde23456789';
    community3_id UUID := 'c9d0e1f2-a3b4-5678-9012-cdef34567890';
    community4_id UUID := 'd0e1f2a3-b4c5-6789-0123-def456789012';
    community5_id UUID := 'e1f2a3b4-c5d6-7890-1234-ef5678901234';

    -- Creator IDs (assuming these are real user UUIDs from your system)
    creator_user1 UUID := '5a6b7c8d-9e0f-1234-5678-9abcdef01234';
    creator_user2 UUID := '6b7c8d9e-0f12-3456-789a-bcdef0123456';
    creator_user3 UUID := '7c8d9e0f-1234-5678-9abc-def012345678';
    creator_user4 UUID := '8d9e0f12-3456-789a-bcde-f0123456789a';
    creator_user5 UUID := '9e0f1234-5678-9abc-def0-123456789abc';
BEGIN
    -- Insert sample communities with real UUIDs
INSERT INTO communities (id, title, description, slug, website, contact_email, contact_phone,
                         founding_year, member_count, is_active, logo_url, created_by)
VALUES
    (community1_id,
     'Greenwood Parents Association',
     'A community for parents of Greenwood International School students to share information, organize events, and support school activities. Regular meetings, workshops, and social events help parents stay connected and involved in their children''s education.',
     'greenwood-parents-association',
     'https://parents.greenwood.edu',
     'parents@greenwood.edu',
     '+1-555-0301',
     2000,
     250,
     TRUE,
     'https://storage.example.com/logos/greenwood-parents-logo.png',
     creator_user1),

    (community2_id,
     'Springfield Alumni Network',
     'Connect with fellow alumni from Springfield University across all graduating classes. Network professionally, share career opportunities, mentor current students, and stay updated on university events and initiatives. Annual reunions and regional chapters available.',
     'springfield-alumni-network',
     'https://alumni.springfield.edu',
     'alumni@springfield.edu',
     '+1-555-0302',
     1960,
     5000,
     TRUE,
     'https://storage.example.com/logos/springfield-alumni-logo.png',
     creator_user2),

    (community3_id,
     'Tech Enthusiasts Club',
     'A vibrant community for technology enthusiasts, programmers, developers, and innovators. Regular meetups, hackathons, tech talks, and coding workshops. Focus areas include AI/ML, web development, cybersecurity, and open-source projects.',
     'tech-enthusiasts-club',
     'https://techclub.org',
     'contact@techclub.org',
     '+1-555-0303',
     2015,
     350,
     TRUE,
     'https://storage.example.com/logos/tech-enthusiasts-logo.png',
     creator_user3),

    (community4_id,
     'Community Arts Collective',
     'Bringing together artists, musicians, performers, and creatives from the local community. Monthly exhibitions, workshops, collaborative projects, and cultural events. Supporting emerging artists and promoting arts education.',
     'community-arts-collective',
     'https://artscollective.org',
     'info@artscollective.org',
     '+1-555-0304',
     2010,
     220,
     TRUE,
     'https://storage.example.com/logos/arts-collective-logo.png',
     creator_user4),

    (community5_id,
     'Student Environmental Alliance',
     'Student-led initiative focused on environmental awareness, sustainability projects, and community cleanups. Active in campus recycling programs, tree planting, environmental education, and advocacy for sustainable practices.',
     'student-environmental-alliance',
     'https://sea.greenwood.edu',
     'sea@greenwood.edu',
     '+1-555-0305',
     2018,
     180,
     TRUE,
     'https://storage.example.com/logos/sea-logo.png',
     creator_user5);
END $$;

-- Update updated_at timestamps with more realistic values
UPDATE communities
SET updated_at = CASE id
                     WHEN 'a7b8c9d0-e1f2-3456-7890-abcd12345678' THEN '2024-12-01 14:30:00'
                     WHEN 'b8c9d0e1-f2a3-4567-8901-bcde23456789' THEN '2024-11-15 10:45:00'
                     WHEN 'c9d0e1f2-a3b4-5678-9012-cdef34567890' THEN '2024-12-10 16:20:00'
                     WHEN 'd0e1f2a3-b4c5-6789-0123-def456789012' THEN '2024-11-28 09:15:00'
                     WHEN 'e1f2a3b4-c5d6-7890-1234-ef5678901234' THEN '2024-12-05 11:30:00'
    END;

-- Create indexes for performance
CREATE INDEX idx_communities_slug ON communities (slug);
CREATE INDEX idx_communities_is_active ON communities (is_active) WHERE is_active = TRUE;
CREATE INDEX idx_communities_member_count ON communities (member_count);
CREATE INDEX idx_communities_created_by ON communities (created_by);
CREATE INDEX idx_communities_founding_year ON communities (founding_year);
CREATE INDEX idx_communities_title ON communities (title);

-- Create a view for active communities
CREATE OR REPLACE VIEW active_communities AS
SELECT
    id,
    title,
    slug,
    description,
    member_count,
    founding_year,
    created_at,
    updated_at
FROM communities
WHERE is_active = TRUE
ORDER BY member_count DESC;

-- Create a view for community statistics
CREATE OR REPLACE VIEW community_statistics AS
SELECT
    COUNT(*) as total_communities,
    SUM(member_count) as total_members,
    AVG(member_count) as avg_members_per_community,
    MIN(founding_year) as oldest_community_year,
    MAX(founding_year) as newest_community_year,
    SUM(CASE WHEN founding_year >= 2010 THEN 1 ELSE 0 END) as communities_formed_since_2010
FROM communities
WHERE is_active = TRUE;

-- Create a view for community growth
CREATE OR REPLACE VIEW community_growth_analysis AS
SELECT
    CASE
        WHEN member_count <= 100 THEN 'Small (1-100)'
        WHEN member_count <= 500 THEN 'Medium (101-500)'
        WHEN member_count <= 1000 THEN 'Large (501-1000)'
        ELSE 'Very Large (1000+)'
        END as size_category,
    COUNT(*) as community_count,
    SUM(member_count) as total_members,
    ROUND(AVG(member_count), 0) as avg_members
FROM communities
WHERE is_active = TRUE
GROUP BY size_category
ORDER BY community_count DESC;

-- Create a view for community age analysis
CREATE OR REPLACE VIEW community_age_analysis AS
SELECT
    CASE
        WHEN EXTRACT(YEAR FROM age(CURRENT_DATE, make_date(founding_year, 1, 1))) <= 5 THEN 'New (0-5 years)'
        WHEN EXTRACT(YEAR FROM age(CURRENT_DATE, make_date(founding_year, 1, 1))) <= 15 THEN 'Established (6-15 years)'
        WHEN EXTRACT(YEAR FROM age(CURRENT_DATE, make_date(founding_year, 1, 1))) <= 30 THEN 'Mature (16-30 years)'
        ELSE 'Historical (30+ years)'
        END as age_category,
    COUNT(*) as community_count,
    ROUND(AVG(member_count), 0) as avg_members,
    MIN(founding_year) as oldest_in_category,
    MAX(founding_year) as newest_in_category
FROM communities
WHERE is_active = TRUE
GROUP BY age_category
ORDER BY age_category;

-- Sample queries for verification
SELECT 'Total Communities' as metric, COUNT(*) as value FROM communities
UNION ALL
SELECT 'Active Communities', COUNT(*) FROM communities WHERE is_active = TRUE
UNION ALL
SELECT 'Total Members', SUM(member_count) FROM communities WHERE is_active = TRUE
UNION ALL
SELECT 'Average Members', ROUND(AVG(member_count), 0) FROM communities WHERE is_active = TRUE
UNION ALL
SELECT 'Oldest Community', MIN(founding_year) FROM communities WHERE is_active = TRUE;

-- Display inserted communities
SELECT
    title,
    slug,
    founding_year,
    member_count,
    is_active,
    created_at,
    updated_at
FROM communities
ORDER BY member_count DESC;

-- Grant privileges
GRANT USAGE ON SCHEMA community_schema TO community_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA community_schema TO community_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA community_schema TO community_admin;
GRANT SELECT ON active_communities TO community_admin;
GRANT SELECT ON community_statistics TO community_admin;
GRANT SELECT ON community_growth_analysis TO community_admin;
GRANT SELECT ON community_age_analysis TO community_admin;