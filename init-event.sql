-- Create schema
CREATE SCHEMA IF NOT EXISTS event_schema;

SET search_path TO event_schema;

-- Drop tables if they exist (in correct order due to foreign keys)
DROP TABLE IF EXISTS event_subscriptions CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS event_categories CASCADE;

-- Create events table (with all fields from your Entity)
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
    max_participants      INT,
    current_participants  INT                   DEFAULT 0,
    registration_fee      DECIMAL(10, 2)        DEFAULT 0.00,
    organizer_id          BIGINT       NOT NULL,
    organizer_type        VARCHAR(50),
    contact_email         VARCHAR(100),
    contact_phone         VARCHAR(20),
    image_url             VARCHAR(500),
    is_online             BOOLEAN               DEFAULT FALSE,
    online_link           VARCHAR(500),
    requires_approval     BOOLEAN               DEFAULT FALSE,
    registration_deadline TIMESTAMP,
    category              VARCHAR(50),
    tags                  VARCHAR(500),
    is_featured           BOOLEAN               DEFAULT FALSE,
    visibility            VARCHAR(20)           DEFAULT 'PUBLIC',
    created_at            TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
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
    amount_paid            DECIMAL(10, 2),
    notes                  TEXT,
    check_in_code          VARCHAR(50),
    check_in_time          TIMESTAMP,
    check_out_time         TIMESTAMP,
    certificate_issued     BOOLEAN              DEFAULT FALSE,
    certificate_issue_date TIMESTAMP,
    feedback_submitted     BOOLEAN              DEFAULT FALSE,
    rating                 INT CHECK (rating >= 1 AND rating <= 5),
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

-- Insert event categories with UUIDs
INSERT INTO event_categories (id, name, description, icon, color)
VALUES
    ('11111111-1111-1111-1111-111111111111',
     'Academic',
     'Academic conferences, seminars, and workshops',
     'graduation-cap',
     '#4CAF50'),

    ('22222222-2222-2222-2222-222222222222',
     'Sports',
     'Sports competitions, tournaments, and games',
     'trophy',
     '#2196F3'),

    ('33333333-3333-3333-3333-333333333333',
     'Cultural',
     'Cultural festivals, performances, and exhibitions',
     'music',
     '#FF9800'),

    ('44444444-4444-4444-4444-444444444444',
     'Community',
     'Community service and outreach programs',
     'users',
     '#9C27B0'),

    ('55555555-5555-5555-5555-555555555555',
     'Professional',
     'Professional development and networking events',
     'briefcase',
     '#607D8B'),

    ('66666666-6666-6666-6666-666666666666',
     'Entertainment',
     'Entertainment and social gatherings',
     'film',
     '#F44336'),

    ('77777777-7777-7777-7777-777777777777',
     'Technology',
     'Tech talks, hackathons, and workshops',
     'code',
     '#795548');

-- Insert sample events with UUIDs
INSERT INTO events (id, title, description, slug, location, venue_details,
                    start_date, start_time, end_date, end_time, status,
                    max_participants, organizer_id, organizer_type, contact_email,
                    contact_phone, category, tags, is_featured, created_by)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
     'Annual Science Symposium 2024',
     'Join us for our annual science symposium featuring keynote speakers, research presentations, and interactive workshops.',
     'annual-science-symposium-2024',
     'University Auditorium, Main Campus',
     'Ground floor, Room A101. Parking available at North Parking Lot.',
     '2024-12-15', '09:00:00', '2024-12-15', '17:00:00', 'ACTIVE',
     200, 2, 'SCHOOL', 'events@university.edu', '+1-555-0101',
     'Academic', 'science,symposium,research,workshop', TRUE,
     '00000000-0000-0000-0000-000000000002'),

    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
     'Winter Sports Tournament',
     'Inter-school basketball and volleyball tournament for all grades.',
     'winter-sports-tournament-2024',
     'School Gymnasium',
     'Bring your sports gear. Locker rooms available.',
     '2024-12-10', '14:00:00', '2024-12-12', '18:00:00', 'ACTIVE',
     150, 2, 'SCHOOL', 'sports@school.edu', '+1-555-0102',
     'Sports', 'basketball,volleyball,tournament,sports', TRUE,
     '00000000-0000-0000-0000-000000000002'),

    ('cccccccc-cccc-cccc-cccc-ccccccccccc3',
     'Tech Hackathon 2024',
     '24-hour coding competition for students interested in technology and innovation.',
     'tech-hackathon-2024',
     'Computer Science Building',
     'Bring your laptops. Food and drinks provided.',
     '2024-12-20', '10:00:00', '2024-12-21', '10:00:00', 'ACTIVE',
     100, 3, 'COMMUNITY', 'hackathon@techclub.org', '+1-555-0103',
     'Technology', 'hackathon,coding,programming,technology', TRUE,
     '00000000-0000-0000-0000-000000000003'),

    ('dddddddd-dddd-dddd-dddd-ddddddddddd4',
     'Parent-Teacher Conference',
     'Quarterly meeting between parents and teachers to discuss student progress.',
     'parent-teacher-conference-dec-2024',
     'School Classrooms',
     'Please check the schedule for your assigned classroom.',
     '2024-12-05', '16:00:00', '2024-12-05', '20:00:00', 'ACTIVE',
     NULL, 2, 'SCHOOL', 'info@school.edu', '+1-555-0104',
     'Academic', 'parent,teacher,conference,education', FALSE,
     '00000000-0000-0000-0000-000000000002'),

    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee5',
     'Community Service Day',
     'Volunteer day for community cleanup and service projects.',
     'community-service-day-2024',
     'City Park',
     'Meet at the main entrance. Gloves and bags provided.',
     '2024-12-08', '08:00:00', '2024-12-08', '12:00:00', 'ACTIVE',
     50, 4, 'COMMUNITY', 'volunteer@community.org', '+1-555-0105',
     'Community', 'volunteer,service,community,cleanup', FALSE,
     '00000000-0000-0000-0000-000000000004'),

    ('ffffffff-ffff-ffff-ffff-fffffffffff6',
     'Music Concert: Winter Melodies',
     'Annual winter concert featuring school bands and choir.',
     'winter-melodies-concert-2024',
     'City Concert Hall',
     'Doors open at 6:30 PM. Formal attire recommended.',
     '2024-12-18', '19:00:00', '2024-12-18', '21:30:00', 'ACTIVE',
     300, 2, 'SCHOOL', 'music@school.edu', '+1-555-0106',
     'Cultural', 'music,concert,winter,performance', TRUE,
     '00000000-0000-0000-0000-000000000002');

-- Insert event subscriptions (registrations) with UUIDs
INSERT INTO event_subscriptions (id, event_id, user_id, student_id, status, registration_date,
                                 payment_status, amount_paid, created_by)
VALUES
-- Event 1 registrations
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
 '00000000-0000-0000-0000-000000000002',
 '00000000-0000-0000-0000-000000000004',
 'APPROVED', '2024-11-20 10:00:00', 'PAID', 0.00,
 '00000000-0000-0000-0000-000000000002'),

('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2',
 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
 '00000000-0000-0000-0000-000000000003',
 '00000000-0000-0000-0000-000000000005',
 'APPROVED', '2024-11-21 11:30:00', 'PAID', 0.00,
 '00000000-0000-0000-0000-000000000003'),

('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3',
 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
 '00000000-0000-0000-0000-000000000004',
 '00000000-0000-0000-0000-000000000001',
 'APPROVED', '2024-11-22 14:20:00', 'PAID', 0.00,
 '00000000-0000-0000-0000-000000000004'),

('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4',
 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
 '00000000-0000-0000-0000-000000000005',
 '00000000-0000-0000-0000-000000000002',
 'PENDING', '2024-11-23 09:15:00', 'PENDING', NULL,
 '00000000-0000-0000-0000-000000000005'),

-- Event 2 registrations
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb1',
 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
 '00000000-0000-0000-0000-000000000003',
 '00000000-0000-0000-0000-000000000005',
 'APPROVED', '2024-11-19 13:45:00', 'PAID', 15.00,
 '00000000-0000-0000-0000-000000000003'),

('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
 '00000000-0000-0000-0000-000000000004',
 '00000000-0000-0000-0000-000000000001',
 'APPROVED', '2024-11-20 16:30:00', 'PAID', 15.00,
 '00000000-0000-0000-0000-000000000004'),

('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb3',
 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
 '00000000-0000-0000-0000-000000000005',
 '00000000-0000-0000-0000-000000000002',
 'WAITLISTED', '2024-11-21 10:10:00', 'PENDING', NULL,
 '00000000-0000-0000-0000-000000000005'),

-- Event 3 registrations
('cccccccc-cccc-cccc-cccc-ccccccccccc1',
 'cccccccc-cccc-cccc-cccc-ccccccccccc3',
 '00000000-0000-0000-0000-000000000002',
 '00000000-0000-0000-0000-000000000004',
 'APPROVED', '2024-11-18 11:00:00', 'PAID', 25.00,
 '00000000-0000-0000-0000-000000000002'),

('cccccccc-cccc-cccc-cccc-ccccccccccc2',
 'cccccccc-cccc-cccc-cccc-ccccccccccc3',
 '00000000-0000-0000-0000-000000000003',
 '00000000-0000-0000-0000-000000000005',
 'APPROVED', '2024-11-19 14:30:00', 'PAID', 25.00,
 '00000000-0000-0000-0000-000000000003'),

-- Event 5 registrations
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee1',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee5',
 '00000000-0000-0000-0000-000000000004',
 '00000000-0000-0000-0000-000000000001',
 'APPROVED', '2024-11-22 08:45:00', 'PAID', 0.00,
 '00000000-0000-0000-0000-000000000004'),

('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee2',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee5',
 '00000000-0000-0000-0000-000000000005',
 '00000000-0000-0000-0000-000000000002',
 'APPROVED', '2024-11-22 09:30:00', 'PAID', 0.00,
 '00000000-0000-0000-0000-000000000005'),

('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee3',
 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee5',
 '00000000-0000-0000-0000-000000000006',
 '00000000-0000-0000-0000-000000000003',
 'APPROVED', '2024-11-23 10:15:00', 'PAID', 0.00,
 '00000000-0000-0000-0000-000000000006');

-- Update current participants count in events table
UPDATE events
SET current_participants = (SELECT COUNT(*)
                            FROM event_subscriptions
                            WHERE event_id = events.id
                              AND status IN ('APPROVED', 'ATTENDING'))
WHERE id IN ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1',
             'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbb2',
             'cccccccc-cccc-cccc-cccc-ccccccccccc3',
             'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeee5');

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

-- Grant privileges
GRANT USAGE ON SCHEMA event_schema TO event_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA event_schema TO event_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA event_schema TO event_admin;
GRANT SELECT ON event_summary_view TO event_admin;
GRANT SELECT ON event_registration_status_view TO event_admin;