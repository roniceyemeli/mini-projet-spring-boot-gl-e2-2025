-- Create schema
CREATE SCHEMA IF NOT EXISTS event_schema;

SET
search_path TO event_schema;

-- Drop tables if they exist
DROP TABLE IF EXISTS event_subscriptions CASCADE;
DROP TABLE IF EXISTS events CASCADE;

-- Create events table (with all fields from your Entity)
CREATE TABLE events
(
    id                    BIGSERIAL PRIMARY KEY,
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
    created_by            BIGINT,
    updated_by            BIGINT,

    -- Add computed columns for business logic
    is_registration_open  BOOLEAN GENERATED ALWAYS AS (
        CASE
            WHEN registration_deadline IS NULL THEN TRUE
            ELSE CURRENT_TIMESTAMP < registration_deadline
            END
        ) STORED,
    is_full               BOOLEAN GENERATED ALWAYS AS (
        CASE
            WHEN max_participants IS NULL THEN FALSE
            ELSE current_participants >= max_participants
            END
        ) STORED,
    can_register          BOOLEAN GENERATED ALWAYS AS (
        status = 'ACTIVE' AND NOT is_full AND is_registration_open
        ) STORED
);

-- Create event_subscriptions table
CREATE TABLE event_subscriptions
(
    id                     BIGSERIAL PRIMARY KEY,
    event_id               BIGINT      NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    user_id                BIGINT      NOT NULL,
    student_id             BIGINT      NOT NULL,
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
    created_by             BIGINT,
    updated_by             BIGINT,

    -- Unique constraint to prevent duplicate registrations
    UNIQUE (event_id, user_id),
    UNIQUE (event_id, student_id),

    -- Add computed columns for business logic
    can_cancel             BOOLEAN GENERATED ALWAYS AS (
        status IN ('PENDING', 'APPROVED', 'WAITLISTED')
        ) STORED
);

-- Create event_categories table (for better organization)
CREATE TABLE event_categories
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    icon        VARCHAR(50),
    color       VARCHAR(20),
    is_active   BOOLEAN   DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert event categories
INSERT INTO event_categories (name, description, icon, color)
VALUES ('Academic', 'Academic conferences, seminars, and workshops', 'graduation-cap', '#4CAF50'),
       ('Sports', 'Sports competitions, tournaments, and games', 'trophy', '#2196F3'),
       ('Cultural', 'Cultural festivals, performances, and exhibitions', 'music', '#FF9800'),
       ('Community', 'Community service and outreach programs', 'users', '#9C27B0'),
       ('Professional', 'Professional development and networking events', 'briefcase', '#607D8B'),
       ('Entertainment', 'Entertainment and social gatherings', 'film', '#F44336'),
       ('Technology', 'Tech talks, hackathons, and workshops', 'code', '#795548');

-- Insert sample events
INSERT INTO events (title, description, slug, location, venue_details,
                    start_date, start_time, end_date, end_time, status,
                    max_participants, organizer_id, organizer_type, contact_email,
                    contact_phone, category, tags, is_featured, created_by)
VALUES ('Annual Science Symposium 2024',
        'Join us for our annual science symposium featuring keynote speakers, research presentations, and interactive workshops.',
        'annual-science-symposium-2024',
        'University Auditorium, Main Campus',
        'Ground floor, Room A101. Parking available at North Parking Lot.',
        '2024-12-15', '09:00:00', '2024-12-15', '17:00:00', 'ACTIVE',
        200, 2, 'SCHOOL', 'events@university.edu', '+1-555-0101',
        'Academic', 'science,symposium,research,workshop', TRUE, 2),
       ('Winter Sports Tournament',
        'Inter-school basketball and volleyball tournament for all grades.',
        'winter-sports-tournament-2024',
        'School Gymnasium',
        'Bring your sports gear. Locker rooms available.',
        '2024-12-10', '14:00:00', '2024-12-12', '18:00:00', 'ACTIVE',
        150, 2, 'SCHOOL', 'sports@school.edu', '+1-555-0102',
        'Sports', 'basketball,volleyball,tournament,sports', TRUE, 2),
       ('Tech Hackathon 2024',
        '24-hour coding competition for students interested in technology and innovation.',
        'tech-hackathon-2024',
        'Computer Science Building',
        'Bring your laptops. Food and drinks provided.',
        '2024-12-20', '10:00:00', '2024-12-21', '10:00:00', 'ACTIVE',
        100, 3, 'COMMUNITY', 'hackathon@techclub.org', '+1-555-0103',
        'Technology', 'hackathon,coding,programming,technology', TRUE, 3),
       ('Parent-Teacher Conference',
        'Quarterly meeting between parents and teachers to discuss student progress.',
        'parent-teacher-conference-dec-2024',
        'School Classrooms',
        'Please check the schedule for your assigned classroom.',
        '2024-12-05', '16:00:00', '2024-12-05', '20:00:00', 'ACTIVE',
        NULL, 2, 'SCHOOL', 'info@school.edu', '+1-555-0104',
        'Academic', 'parent,teacher,conference,education', FALSE, 2),
       ('Community Service Day',
        'Volunteer day for community cleanup and service projects.',
        'community-service-day-2024',
        'City Park',
        'Meet at the main entrance. Gloves and bags provided.',
        '2024-12-08', '08:00:00', '2024-12-08', '12:00:00', 'ACTIVE',
        50, 4, 'COMMUNITY', 'volunteer@community.org', '+1-555-0105',
        'Community', 'volunteer,service,community,cleanup', FALSE, 4),
       ('Music Concert: Winter Melodies',
        'Annual winter concert featuring school bands and choir.',
        'winter-melodies-concert-2024',
        'City Concert Hall',
        'Doors open at 6:30 PM. Formal attire recommended.',
        '2024-12-18', '19:00:00', '2024-12-18', '21:30:00', 'ACTIVE',
        300, 2, 'SCHOOL', 'music@school.edu', '+1-555-0106',
        'Cultural', 'music,concert,winter,performance', TRUE, 2);

-- Insert event subscriptions (registrations)
INSERT INTO event_subscriptions (event_id, user_id, student_id, status, registration_date,
                                 payment_status, amount_paid, created_by)
VALUES
-- Event 1 registrations
(1, 2, 4, 'APPROVED', '2024-11-20 10:00:00', 'PAID', 0.00, 2),
(1, 3, 5, 'APPROVED', '2024-11-21 11:30:00', 'PAID', 0.00, 3),
(1, 4, 1, 'APPROVED', '2024-11-22 14:20:00', 'PAID', 0.00, 4),
(1, 5, 2, 'PENDING', '2024-11-23 09:15:00', 'PENDING', NULL, 5),

-- Event 2 registrations
(2, 3, 5, 'APPROVED', '2024-11-19 13:45:00', 'PAID', 15.00, 3),
(2, 4, 1, 'APPROVED', '2024-11-20 16:30:00', 'PAID', 15.00, 4),
(2, 5, 2, 'WAITLISTED', '2024-11-21 10:10:00', 'PENDING', NULL, 5),

-- Event 3 registrations
(3, 2, 4, 'APPROVED', '2024-11-18 11:00:00', 'PAID', 25.00, 2),
(3, 3, 5, 'APPROVED', '2024-11-19 14:30:00', 'PAID', 25.00, 3),

-- Event 5 registrations
(5, 4, 1, 'APPROVED', '2024-11-22 08:45:00', 'PAID', 0.00, 4),
(5, 5, 2, 'APPROVED', '2024-11-22 09:30:00', 'PAID', 0.00, 5),
(5, 6, 3, 'APPROVED', '2024-11-23 10:15:00', 'PAID', 0.00, 6);

-- Update current participants count in events table
UPDATE events
SET current_participants = (SELECT COUNT(*)
                            FROM event_subscriptions
                            WHERE event_id = events.id
                              AND status IN ('APPROVED', 'ATTENDING'))
WHERE id IN (1, 2, 3, 5);

-- Create indexes for performance
CREATE INDEX idx_events_status ON events (status);
CREATE INDEX idx_events_start_date ON events (start_date);
CREATE INDEX idx_events_end_date ON events (end_date);
CREATE INDEX idx_events_organizer ON events (organizer_id, organizer_type);
CREATE INDEX idx_events_category ON events (category);
CREATE INDEX idx_events_is_featured ON events (is_featured) WHERE is_featured = TRUE;
CREATE INDEX idx_events_can_register ON events (can_register) WHERE can_register = TRUE;

CREATE INDEX idx_event_subscriptions_event ON event_subscriptions (event_id);
CREATE INDEX idx_event_subscriptions_user ON event_subscriptions (user_id);
CREATE INDEX idx_event_subscriptions_student ON event_subscriptions (student_id);
CREATE INDEX idx_event_subscriptions_status ON event_subscriptions (status);
CREATE INDEX idx_event_subscriptions_payment ON event_subscriptions (payment_status);
CREATE INDEX idx_event_subscriptions_check_in_code ON event_subscriptions (check_in_code);

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

-- Grant privileges
GRANT
ALL
PRIVILEGES
ON
ALL
TABLES IN SCHEMA event_schema TO event_admin;
GRANT ALL PRIVILEGES ON ALL
SEQUENCES IN SCHEMA event_schema TO event_admin;
GRANT USAGE ON SCHEMA
event_schema TO event_admin;
GRANT SELECT ON event_summary_view TO event_admin;