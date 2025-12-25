-- Create schema
CREATE SCHEMA IF NOT EXISTS event_schema;

SET search_path TO event_schema;

-- Drop tables if they exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS event_subscriptions CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS event_categories CASCADE;

-- Create events table (corrected to match Entity)
CREATE TABLE events
(
    id                    UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title                 VARCHAR(200) NOT NULL,
    description           TEXT,
    slug                  VARCHAR(255) UNIQUE,
    location              VARCHAR(500) NOT NULL,
    venue_details         VARCHAR(1000),
    start_date            DATE         NOT NULL,
    start_time            TIME,
    end_date              DATE         NOT NULL,
    end_time              TIME,
    status                VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    max_participants      INTEGER,
    current_participants  INTEGER             DEFAULT 0,
    registration_fee      DECIMAL(19, 2)      DEFAULT 0.00,
    organizer_id          UUID         NOT NULL,
    organizer_type        VARCHAR(50),
    contact_email         VARCHAR(100),
    contact_phone         VARCHAR(20),
    image_url             VARCHAR(500),
    is_online             BOOLEAN             DEFAULT FALSE,
    online_link           VARCHAR(500),
    requires_approval     BOOLEAN             DEFAULT FALSE,
    registration_deadline TIMESTAMP,
    category              VARCHAR(50),
    tags                  VARCHAR(500),
    is_featured           BOOLEAN             DEFAULT FALSE,
    visibility            VARCHAR(20)         DEFAULT 'PUBLIC',
    created_at            TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP           DEFAULT CURRENT_TIMESTAMP,
    created_by            UUID,
    updated_by            UUID
);

-- Create event_categories table (for better organization)
CREATE TABLE event_categories
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    icon        VARCHAR(50),
    color       VARCHAR(20),
    is_active   BOOLEAN   DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create event_subscriptions table
CREATE TABLE event_subscriptions
(
    id                     UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_id               UUID      NOT NULL,
    user_id                UUID      NOT NULL,
    student_id             UUID      NOT NULL,
    status                 VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    registration_date      TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    approval_date          TIMESTAMP,
    cancellation_date      TIMESTAMP,
    attended               BOOLEAN              DEFAULT FALSE,
    attendance_date        TIMESTAMP,
    payment_status         VARCHAR(20)          DEFAULT 'PENDING',
    payment_reference      VARCHAR(100),
    amount_paid            DECIMAL(19, 2),
    notes                  TEXT,
    check_in_code          VARCHAR(50),
    check_in_time          TIMESTAMP,
    check_out_time         TIMESTAMP,
    certificate_issued     BOOLEAN              DEFAULT FALSE,
    certificate_issue_date TIMESTAMP,
    feedback_submitted     BOOLEAN              DEFAULT FALSE,
    rating                 INTEGER CHECK (rating >= 1 AND rating <= 5),
    feedback_comment       TEXT,
    is_active              BOOLEAN              DEFAULT TRUE,
    cancellation_reason    VARCHAR(500),
    created_at             TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    created_by             UUID,
    updated_by             UUID,

    -- Add foreign key constraint
    CONSTRAINT fk_event_subscription_event FOREIGN KEY (event_id)
        REFERENCES events (id) ON DELETE CASCADE,

    -- Unique constraint to prevent duplicate registrations per user/student
    UNIQUE (event_id, user_id),
    UNIQUE (event_id, student_id)
);

-- Generate real random UUIDs for categories
DO $$
DECLARE
cat_academic UUID := 'b1e3f4a7-8c2d-4e9f-a6b5-c7d8e9f0a1b2';
    cat_sports UUID := 'f2a3b4c5-6d7e-8f90-a1b2-c3d4e5f60789';
    cat_cultural UUID := 'c3d4e5f6-7a8b-9c0d-1e2f-3a4b5c6d7e8f';
    cat_community UUID := 'd4e5f607-89ab-c0d1-2e3f-4a5b6c7d8e9f';
    cat_professional UUID := 'e5f60789-abcd-0e1f-2a3b-4c5d6e7f8091';
    cat_entertainment UUID := 'f6078901-bcde-1f2a-3b4c-5d6e7f8091a2';
    cat_technology UUID := '7890a1b2-cdef-2a3b-4c5d-6e7f8091a2b3';
BEGIN
    -- Insert event categories with real UUIDs
INSERT INTO event_categories (id, name, description, icon, color)
VALUES
    (cat_academic,
     'Academic',
     'Academic conferences, seminars, and workshops',
     'graduation-cap',
     '#4CAF50'),

    (cat_sports,
     'Sports',
     'Sports competitions, tournaments, and games',
     'trophy',
     '#2196F3'),

    (cat_cultural,
     'Cultural',
     'Cultural festivals, performances, and exhibitions',
     'music',
     '#FF9800'),

    (cat_community,
     'Community',
     'Community service and outreach programs',
     'users',
     '#9C27B0'),

    (cat_professional,
     'Professional',
     'Professional development and networking events',
     'briefcase',
     '#607D8B'),

    (cat_entertainment,
     'Entertainment',
     'Entertainment and social gatherings',
     'film',
     '#F44336'),

    (cat_technology,
     'Technology',
     'Tech talks, hackathons, and workshops',
     'code',
     '#795548');
END $$;

-- Generate real random UUIDs for events
DO $$
DECLARE
    -- Event IDs
event_science UUID := 'a123b456-c789-d012-e345-f67890123456';
    event_sports UUID := 'b234c567-d890-e123-f456-789012345678';
    event_hackathon UUID := 'c345d678-e901-f234-5678-901234567890';
    event_conference UUID := 'd456e789-f012-3456-7890-123456789012';
    event_service UUID := 'e567f890-1234-5678-9012-345678901234';
    event_concert UUID := 'f6789012-3456-7890-1234-567890123456';

    -- Organizer IDs (assuming these are real user/school UUIDs from your system)
    school_organizer UUID := '3a4b5c6d-7e8f-90ab-cdef-123456789abc';
    community_organizer UUID := '4b5c6d7e-8f90-abcd-ef01-23456789abcd';

    -- Creator IDs (assuming these are real user UUIDs)
    creator_admin UUID := '5c6d7e8f-90ab-cdef-0123-456789abcdef';
    creator_user1 UUID := '6d7e8f90-abcd-ef01-2345-6789abcdef01';
    creator_user2 UUID := '7e8f90ab-cdef-0123-4567-89abcdef0123';
BEGIN
    -- Insert sample events with real UUIDs
INSERT INTO events (id, title, description, slug, location, venue_details,
                    start_date, start_time, end_date, end_time, status,
                    max_participants, organizer_id, organizer_type, contact_email,
                    contact_phone, category, tags, is_featured, created_by)
VALUES
    (event_science,
     'Annual Science Symposium 2024',
     'Join us for our annual science symposium featuring keynote speakers, research presentations, and interactive workshops.',
     'annual-science-symposium-2024',
     'University Auditorium, Main Campus',
     'Ground floor, Room A101. Parking available at North Parking Lot.',
     '2024-12-15', '09:00:00', '2024-12-15', '17:00:00', 'ACTIVE',
     200, school_organizer, 'SCHOOL', 'events@university.edu', '+1-555-0101',
     'Academic', 'science,symposium,research,workshop', TRUE,
     creator_admin),

    (event_sports,
     'Winter Sports Tournament',
     'Inter-school basketball and volleyball tournament for all grades.',
     'winter-sports-tournament-2024',
     'School Gymnasium',
     'Bring your sports gear. Locker rooms available.',
     '2024-12-10', '14:00:00', '2024-12-12', '18:00:00', 'ACTIVE',
     150, school_organizer, 'SCHOOL', 'sports@school.edu', '+1-555-0102',
     'Sports', 'basketball,volleyball,tournament,sports', TRUE,
     creator_admin),

    (event_hackathon,
     'Tech Hackathon 2024',
     '24-hour coding competition for students interested in technology and innovation.',
     'tech-hackathon-2024',
     'Computer Science Building',
     'Bring your laptops. Food and drinks provided.',
     '2024-12-20', '10:00:00', '2024-12-21', '10:00:00', 'ACTIVE',
     100, community_organizer, 'COMMUNITY', 'hackathon@techclub.org', '+1-555-0103',
     'Technology', 'hackathon,coding,programming,technology', TRUE,
     creator_user1),

    (event_conference,
     'Parent-Teacher Conference',
     'Quarterly meeting between parents and teachers to discuss student progress.',
     'parent-teacher-conference-dec-2024',
     'School Classrooms',
     'Please check the schedule for your assigned classroom.',
     '2024-12-05', '16:00:00', '2024-12-05', '20:00:00', 'ACTIVE',
     NULL, school_organizer, 'SCHOOL', 'info@school.edu', '+1-555-0104',
     'Academic', 'parent,teacher,conference,education', FALSE,
     creator_admin),

    (event_service,
     'Community Service Day',
     'Volunteer day for community cleanup and service projects.',
     'community-service-day-2024',
     'City Park',
     'Meet at the main entrance. Gloves and bags provided.',
     '2024-12-08', '08:00:00', '2024-12-08', '12:00:00', 'ACTIVE',
     50, community_organizer, 'COMMUNITY', 'volunteer@community.org', '+1-555-0105',
     'Community', 'volunteer,service,community,cleanup', FALSE,
     creator_user2),

    (event_concert,
     'Music Concert: Winter Melodies',
     'Annual winter concert featuring school bands and choir.',
     'winter-melodies-concert-2024',
     'City Concert Hall',
     'Doors open at 6:30 PM. Formal attire recommended.',
     '2024-12-18', '19:00:00', '2024-12-18', '21:30:00', 'ACTIVE',
     300, school_organizer, 'SCHOOL', 'music@school.edu', '+1-555-0106',
     'Cultural', 'music,concert,winter,performance', TRUE,
     creator_admin);
END $$;

-- Generate real random UUIDs for subscriptions
DO $$
DECLARE
    -- Subscription IDs
sub1 UUID := '9012a345-b678-c901-d234-e56789012345';
    sub2 UUID := 'a012b456-c789-d012-e345-f67890123456';
    sub3 UUID := 'b123c567-d890-e123-f456-789012345678';
    sub4 UUID := 'c234d678-e901-f234-5678-901234567890';
    sub5 UUID := 'd345e789-f012-3456-7890-123456789012';
    sub6 UUID := 'e456f890-1234-5678-9012-345678901234';
    sub7 UUID := 'f5678901-2345-6789-0123-456789012345';
    sub8 UUID := '0123a456-b789-c012-d345-e67890123456';
    sub9 UUID := '1234b567-c890-d123-e456-789012345678';
    sub10 UUID := '2345c678-d901-e234-f567-890123456789';
    sub11 UUID := '3456d789-e012-f345-6789-012345678901';
    sub12 UUID := '4567e890-f123-4567-8901-234567890123';

    -- Event IDs (from above)
    event_science UUID := 'a123b456-c789-d012-e345-f67890123456';
    event_sports UUID := 'b234c567-d890-e123-f456-789012345678';
    event_hackathon UUID := 'c345d678-e901-f234-5678-901234567890';
    event_service UUID := 'e567f890-1234-5678-9012-345678901234';

    -- User IDs (assuming these are real user UUIDs from your system)
    user1 UUID := '5678f901-2345-6789-0123-456789abcdef';
    user2 UUID := '6789a012-3456-7890-1234-56789abcdef0';
    user3 UUID := '7890b123-4567-8901-2345-6789abcdef01';
    user4 UUID := '8901c234-5678-9012-3456-789abcdef012';
    user5 UUID := '9012d345-6789-0123-4567-89abcdef0123';
    user6 UUID := 'a123e456-7890-1234-5678-9abcdef01234';

    -- Student IDs (assuming these are real student UUIDs from your system)
    student1 UUID := 'b234f567-8901-2345-6789-0abcdef12345';
    student2 UUID := 'c345a678-9012-3456-7890-1bcdef23456';
    student3 UUID := 'd456b789-0123-4567-8901-2cdef34567';
    student4 UUID := 'e567c890-1234-5678-9012-3def45678';
    student5 UUID := 'f678d901-2345-6789-0123-4ef56789';
    student6 UUID := '0123e901-2345-6789-0123-4567890abc';
BEGIN
    -- Insert event subscriptions with real UUIDs
INSERT INTO event_subscriptions (id, event_id, user_id, student_id, status, registration_date,
                                 payment_status, amount_paid, created_by)
VALUES
    -- Event 1 registrations (Science Symposium)
    (sub1, event_science, user1, student1, 'APPROVED', '2024-11-20 10:00:00', 'PAID', 0.00, user1),
    (sub2, event_science, user2, student2, 'APPROVED', '2024-11-21 11:30:00', 'PAID', 0.00, user2),
    (sub3, event_science, user3, student3, 'APPROVED', '2024-11-22 14:20:00', 'PAID', 0.00, user3),
    (sub4, event_science, user4, student4, 'PENDING', '2024-11-23 09:15:00', 'PENDING', NULL, user4),

    -- Event 2 registrations (Sports Tournament)
    (sub5, event_sports, user2, student2, 'APPROVED', '2024-11-19 13:45:00', 'PAID', 15.00, user2),
    (sub6, event_sports, user3, student3, 'APPROVED', '2024-11-20 16:30:00', 'PAID', 15.00, user3),
    (sub7, event_sports, user4, student4, 'WAITLISTED', '2024-11-21 10:10:00', 'PENDING', NULL, user4),

    -- Event 3 registrations (Hackathon)
    (sub8, event_hackathon, user1, student1, 'APPROVED', '2024-11-18 11:00:00', 'PAID', 25.00, user1),
    (sub9, event_hackathon, user2, student2, 'APPROVED', '2024-11-19 14:30:00', 'PAID', 25.00, user2),

    -- Event 5 registrations (Community Service)
    (sub10, event_service, user3, student3, 'APPROVED', '2024-11-22 08:45:00', 'PAID', 0.00, user3),
    (sub11, event_service, user4, student4, 'APPROVED', '2024-11-22 09:30:00', 'PAID', 0.00, user4),
    (sub12, event_service, user5, student5, 'APPROVED', '2024-11-23 10:15:00', 'PAID', 0.00, user5);
END $$;

-- Update current participants count in events table
UPDATE events
SET current_participants = (
    SELECT COUNT(*)
    FROM event_subscriptions
    WHERE event_id = events.id
      AND status IN ('APPROVED', 'ATTENDING')
);

-- Create indexes for performance
CREATE INDEX idx_events_status ON events (status);
CREATE INDEX idx_events_start_date ON events (start_date);
CREATE INDEX idx_events_end_date ON events (end_date);
CREATE INDEX idx_events_organizer ON events (organizer_id, organizer_type);
CREATE INDEX idx_events_category ON events (category);
CREATE INDEX idx_events_is_featured ON events (is_featured) WHERE is_featured = TRUE;
CREATE INDEX idx_events_slug ON events (slug);
CREATE INDEX idx_events_created_by ON events (created_by);

CREATE INDEX idx_event_subscriptions_event ON event_subscriptions (event_id);
CREATE INDEX idx_event_subscriptions_user ON event_subscriptions (user_id);
CREATE INDEX idx_event_subscriptions_student ON event_subscriptions (student_id);
CREATE INDEX idx_event_subscriptions_status ON event_subscriptions (status);
CREATE INDEX idx_event_subscriptions_payment ON event_subscriptions (payment_status);
CREATE INDEX idx_event_subscriptions_check_in_code ON event_subscriptions (check_in_code);
CREATE INDEX idx_event_subscriptions_created_by ON event_subscriptions (created_by);

-- Create views for common queries
CREATE VIEW event_summary_view AS
SELECT e.id,
       e.title,
       e.description,
       e.location,
       e.start_date,
       e.end_date,
       e.status,
       e.max_participants,
       e.current_participants,
       e.category,
       e.is_featured,
       e.visibility,
       COUNT(es.id)                                       as total_registrations,
       COUNT(CASE WHEN es.status = 'APPROVED' THEN 1 END) as approved_registrations,
       COUNT(CASE WHEN es.attended = TRUE THEN 1 END)     as attendees
FROM events e
         LEFT JOIN event_subscriptions es ON e.id = es.event_id
GROUP BY e.id;

-- Create view for registration status check
CREATE VIEW event_registration_status_view AS
SELECT e.id as event_id,
       e.title,
       e.status as event_status,
       e.max_participants,
       e.current_participants,
       e.registration_deadline,
       e.requires_approval,
       CASE
           WHEN e.status != 'ACTIVE' THEN 'EVENT_NOT_ACTIVE'
           WHEN e.max_participants IS NOT NULL AND e.current_participants >= e.max_participants THEN 'EVENT_FULL'
           WHEN e.registration_deadline IS NOT NULL AND CURRENT_TIMESTAMP > e.registration_deadline THEN 'REGISTRATION_CLOSED'
           ELSE 'CAN_REGISTER'
           END as registration_eligibility
FROM events e;

-- Grant privileges (adjust role name as needed)
GRANT USAGE ON SCHEMA event_schema TO event_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA event_schema TO event_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA event_schema TO event_admin;
GRANT SELECT ON event_summary_view TO event_admin;
GRANT SELECT ON event_registration_status_view TO event_admin;