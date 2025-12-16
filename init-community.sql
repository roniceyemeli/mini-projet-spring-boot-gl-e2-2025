-- Create schema
CREATE SCHEMA IF NOT EXISTS community_schema;

SET
search_path TO community_schema;

-- Drop tables if they exist
DROP TABLE IF EXISTS communities CASCADE;
DROP TABLE IF EXISTS community_members CASCADE;
DROP TABLE IF EXISTS community_events CASCADE;
DROP TABLE IF EXISTS community_posts CASCADE;
DROP TABLE IF EXISTS community_comments CASCADE;

-- Create communities table (with all fields from your Entity)
CREATE TABLE communities
(
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    description   TEXT,
    slug          VARCHAR(255) UNIQUE,
    website       VARCHAR(200),
    contact_email VARCHAR(100),
    contact_phone VARCHAR(20),
    founding_year INT,
    member_count  INT       DEFAULT 0,
    is_active     BOOLEAN   DEFAULT TRUE,
    logo_url      VARCHAR(500),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by    INT
);

-- Create community_members table
CREATE TABLE community_members
(
    id                BIGSERIAL PRIMARY KEY,
    community_id      BIGINT NOT NULL REFERENCES communities (id) ON DELETE CASCADE,
    user_id           BIGINT NOT NULL,
    join_date         TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    member_role       VARCHAR(50) DEFAULT 'MEMBER', -- ADMIN, MODERATOR, MEMBER
    is_active         BOOLEAN     DEFAULT TRUE,
    membership_status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, PENDING, SUSPENDED
    last_active       TIMESTAMP,
    created_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (community_id, user_id)
);

-- Create community_events table (links communities to events in event_db)
CREATE TABLE community_events
(
    id             BIGSERIAL PRIMARY KEY,
    community_id   BIGINT NOT NULL REFERENCES communities (id) ON DELETE CASCADE,
    event_id       BIGINT NOT NULL, -- Reference to event in event_db
    is_featured    BOOLEAN   DEFAULT FALSE,
    promoted_by    BIGINT,
    promotion_date TIMESTAMP,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create community_posts table (forum/discussion posts)
CREATE TABLE community_posts
(
    id            BIGSERIAL PRIMARY KEY,
    community_id  BIGINT       NOT NULL REFERENCES communities (id) ON DELETE CASCADE,
    user_id       BIGINT       NOT NULL,
    title         VARCHAR(200) NOT NULL,
    content       TEXT         NOT NULL,
    post_type     VARCHAR(20) DEFAULT 'DISCUSSION', -- DISCUSSION, ANNOUNCEMENT, QUESTION, NEWS
    category      VARCHAR(50),
    view_count    INT         DEFAULT 0,
    like_count    INT         DEFAULT 0,
    comment_count INT         DEFAULT 0,
    is_pinned     BOOLEAN     DEFAULT FALSE,
    is_locked     BOOLEAN     DEFAULT FALSE,
    is_approved   BOOLEAN     DEFAULT TRUE,
    created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);

-- Create community_comments table
CREATE TABLE community_comments
(
    id                BIGSERIAL PRIMARY KEY,
    post_id           BIGINT NOT NULL REFERENCES community_posts (id) ON DELETE CASCADE,
    user_id           BIGINT NOT NULL,
    parent_comment_id BIGINT REFERENCES community_comments (id) ON DELETE CASCADE,
    content           TEXT   NOT NULL,
    like_count        INT       DEFAULT 0,
    is_approved       BOOLEAN   DEFAULT TRUE,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample communities
INSERT INTO communities (title, description, slug, website, contact_email, contact_phone,
                         founding_year, member_count, is_active, created_by)
VALUES ('Greenwood Parents Association',
        'A community for parents of Greenwood International School students to share information, organize events, and support school activities.',
        'greenwood-parents-association',
        'https://parents.greenwood.edu',
        'parents@greenwood.edu',
        '+1-555-0301',
        2000,
        250,
        TRUE,
        6),
       ('Springfield Alumni Network',
        'Connect with fellow alumni from Springfield University. Network, share opportunities, and stay updated on university events.',
        'springfield-alumni-network',
        'https://alumni.springfield.edu',
        'alumni@springfield.edu',
        '+1-555-0302',
        1960,
        5000,
        TRUE,
        2),
       ('Tech Enthusiasts Club',
        'A community for technology enthusiasts, programmers, and innovators. Regular meetups, hackathons, and tech talks.',
        'tech-enthusiasts-club',
        'https://techclub.org',
        'contact@techclub.org',
        '+1-555-0303',
        2015,
        150,
        TRUE,
        3),
       ('Community Arts Collective',
        'Bringing together artists, musicians, and creatives from the local community. Exhibitions, workshops, and collaborative projects.',
        'community-arts-collective',
        'https://artscollective.org',
        'info@artscollective.org',
        '+1-555-0304',
        2010,
        120,
        TRUE,
        4),
       ('Student Environmental Alliance',
        'Student-led initiative focused on environmental awareness, sustainability projects, and community cleanups.',
        'student-environmental-alliance',
        'https://sea.greenwood.edu',
        'sea@greenwood.edu',
        '+1-555-0305',
        2018,
        80,
        TRUE,
        4);

-- Insert community members
INSERT INTO community_members (community_id, user_id, member_role, membership_status)
VALUES
-- Greenwood Parents Association members
(1, 6, 'ADMIN', 'ACTIVE'),
(1, 2, 'MEMBER', 'ACTIVE'),
(1, 3, 'MEMBER', 'ACTIVE'),
(1, 4, 'MEMBER', 'ACTIVE'),
(1, 5, 'MEMBER', 'ACTIVE'),

-- Springfield Alumni Network members
(2, 2, 'ADMIN', 'ACTIVE'),
(2, 3, 'MEMBER', 'ACTIVE'),
(2, 4, 'MEMBER', 'ACTIVE'),
(2, 5, 'MEMBER', 'ACTIVE'),
(2, 6, 'MEMBER', 'ACTIVE'),

-- Tech Enthusiasts Club members
(3, 3, 'ADMIN', 'ACTIVE'),
(3, 2, 'MEMBER', 'ACTIVE'),
(3, 4, 'MEMBER', 'ACTIVE'),
(3, 5, 'MEMBER', 'ACTIVE'),

-- Community Arts Collective members
(4, 4, 'ADMIN', 'ACTIVE'),
(4, 3, 'MEMBER', 'ACTIVE'),
(4, 5, 'MEMBER', 'ACTIVE'),
(4, 6, 'MEMBER', 'ACTIVE'),

-- Student Environmental Alliance members
(5, 4, 'ADMIN', 'ACTIVE'),
(5, 5, 'MEMBER', 'ACTIVE'),
(5, 2, 'MEMBER', 'ACTIVE');

-- Insert community events (linking to events in event_db)
INSERT INTO community_events (community_id, event_id, is_featured)
VALUES (1, 1, TRUE),  -- Greenwood Parents Association - Science Symposium
       (1, 4, FALSE), -- Greenwood Parents Association - Parent-Teacher Conference
       (3, 3, TRUE),  -- Tech Enthusiasts Club - Tech Hackathon
       (4, 6, TRUE),  -- Community Arts Collective - Winter Concert
       (5, 5, TRUE);
-- Student Environmental Alliance - Community Service Day

-- Insert community posts
INSERT INTO community_posts (community_id, user_id, title, content, post_type, category,
                             view_count, like_count, comment_count)
VALUES (1, 6, 'Upcoming Parent Meeting',
        'Dear parents, our next monthly meeting will be on December 10th at 6 PM in the school auditorium. We will be discussing the upcoming winter festival.',
        'ANNOUNCEMENT', 'MEETING',
        120, 35, 12),
       (1, 2, 'Homework Help Request',
        'Can any parents help with the science project for 8th grade? My child is struggling with the experiment setup.',
        'QUESTION', 'ACADEMIC',
        85, 15, 8),
       (2, 2, 'Alumni Reunion 2024',
        'Save the date! Our annual alumni reunion will be held on December 28th. Registration details to follow.',
        'ANNOUNCEMENT', 'EVENT',
        300, 120, 25),
       (3, 3, 'Weekly Coding Challenge',
        'This week''s challenge: Build a simple calculator using JavaScript. Post your solutions here!',
        'DISCUSSION', 'PROGRAMMING',
        150, 45, 18),
       (4, 4, 'Art Exhibition Volunteers Needed',
        'We need volunteers for the upcoming community art exhibition on December 15th. Please sign up if available.',
        'ANNOUNCEMENT', 'VOLUNTEER',
        90, 30, 10),
       (5, 4, 'Plastic Recycling Drive',
        'Our next plastic recycling collection will be on December 12th. Bring your clean plastic waste to the school parking lot.',
        'ANNOUNCEMENT', 'ENVIRONMENT',
        110, 40, 15);

-- Insert community comments
INSERT INTO community_comments (post_id, user_id, content, like_count)
VALUES (1, 2, 'Looking forward to the meeting! I''ll bring some refreshments.', 5),
       (1, 3, 'Can we add the winter festival budget to the agenda?', 3),
       (1, 4, 'I can help with setting up the venue.', 2),

       (2, 3, 'I can help! My daughter did that project last year. What specific help is needed?', 4),
       (2, 6, 'Check the school website for the project guidelines. There are helpful videos there.', 2),

       (3, 3, 'Excited for the reunion! Can''t wait to see everyone.', 8),
       (3, 4, 'Will there be a virtual option for alumni who can''t attend in person?', 5),

       (4, 2, 'Great challenge! Here''s my solution: [code snippet]', 10),
       (4, 4, 'I''ll give it a try this weekend. Thanks for organizing these challenges!', 3),

       (5, 3, 'I can volunteer for setup on the 14th.', 2),
       (5, 5, 'Count me in for the exhibition day.', 1),

       (6, 5, 'Great initiative! I''ll spread the word to my neighborhood.', 6),
       (6, 2, 'What types of plastic are accepted?', 3);

-- Update member counts in communities table
UPDATE communities
SET member_count = (SELECT COUNT(*)
                    FROM community_members
                    WHERE community_id = communities.id
                      AND membership_status = 'ACTIVE');

-- Update comment counts in posts table
UPDATE community_posts
SET comment_count = (SELECT COUNT(*)
                     FROM community_comments
                     WHERE post_id = community_posts.id);

-- Create indexes for performance
CREATE INDEX idx_communities_slug ON communities (slug);
CREATE INDEX idx_communities_is_active ON communities (is_active) WHERE is_active = TRUE;
CREATE INDEX idx_communities_member_count ON communities (member_count);

CREATE INDEX idx_community_members_community ON community_members (community_id);
CREATE INDEX idx_community_members_user ON community_members (user_id);
CREATE INDEX idx_community_members_role ON community_members (member_role);
CREATE INDEX idx_community_members_status ON community_members (membership_status) WHERE membership_status = 'ACTIVE';

CREATE INDEX idx_community_events_community ON community_events (community_id);
CREATE INDEX idx_community_events_event ON community_events (event_id);
CREATE INDEX idx_community_events_is_featured ON community_events (is_featured) WHERE is_featured = TRUE;

CREATE INDEX idx_community_posts_community ON community_posts (community_id);
CREATE INDEX idx_community_posts_user ON community_posts (user_id);
CREATE INDEX idx_community_posts_post_type ON community_posts (post_type);
CREATE INDEX idx_community_posts_category ON community_posts (category);
CREATE INDEX idx_community_posts_created_at ON community_posts (created_at);
CREATE INDEX idx_community_posts_is_pinned ON community_posts (is_pinned) WHERE is_pinned = TRUE;

CREATE INDEX idx_community_comments_post ON community_comments (post_id);
CREATE INDEX idx_community_comments_user ON community_comments (user_id);
CREATE INDEX idx_community_comments_parent ON community_comments (parent_comment_id);
CREATE INDEX idx_community_comments_created_at ON community_comments (created_at);

-- Create views for common queries
CREATE VIEW community_summary_view AS
SELECT c.id,
       c.title,
       c.slug,
       c.member_count,
       c.founding_year,
       c.is_active,
       COUNT(DISTINCT cm.user_id)  as active_members,
       COUNT(DISTINCT ce.event_id) as upcoming_events,
       COUNT(DISTINCT cp.id)       as recent_posts,
       MAX(cp.created_at)          as latest_post_date
FROM communities c
         LEFT JOIN community_members cm ON c.id = cm.community_id AND cm.membership_status = 'ACTIVE'
         LEFT JOIN community_events ce ON c.id = ce.community_id
         LEFT JOIN community_posts cp ON c.id = cp.community_id AND cp.created_at > CURRENT_DATE -
        INTERVAL '30 days'
        GROUP BY c.id;

-- Grant privileges
GRANT
ALL
PRIVILEGES
ON
ALL
TABLES IN SCHEMA community_schema TO community_admin;
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA community_schema TO community_admin;
GRANT USAGE ON SCHEMA
community_schema TO community_admin;
GRANT SELECT ON community_summary_view TO community_admin;