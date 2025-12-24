package com.service.event.entity;

import com.service.event.enums.EventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "events", schema = "event_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(unique = true, length = 255)
    private String slug;

    @Column(nullable = false, length = 500)
    private String location;

    @Column(name = "venue_details", length = 1000)
    private String venueDetails;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus status = EventStatus.DRAFT;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "current_participants")
    private Integer currentParticipants = 0;

    @Column(name = "registration_fee")
    private BigDecimal registrationFee = BigDecimal.ZERO;

    @Column(name = "organizer_id",columnDefinition = "uuid", nullable = false)
    private UUID organizerId;

    @Column(name = "organizer_type", length = 50)
    private String organizerType; // SCHOOL, COMMUNITY, USER

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_online")
    private Boolean isOnline = false;

    @Column(name = "online_link", length = 500)
    private String onlineLink;

    @Column(name = "requires_approval")
    private Boolean requiresApproval = false;

    @Column(name = "registration_deadline")
    private LocalDateTime registrationDeadline;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "tags", length = 500)
    private String tags; // Comma-separated tags

    @Column(name = "is_featured")
    private Boolean isFeatured = false; // if elelemt will be selected as one of top events to display on specific cases

    @Column(name = "visibility", length = 20)
    private String visibility = "PUBLIC"; // PUBLIC, PRIVATE, RESTRICTED

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by",columnDefinition = "uuid")
    private UUID createdBy;

    @Column(name = "updated_by",columnDefinition = "uuid")
    private UUID updatedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (currentParticipants == null) {
            currentParticipants = 0;
        }
        if (status == null) {
            status = EventStatus.DRAFT;
        }

        // Generate slug from title if not provided
        if (slug == null || slug.trim().isEmpty()) {
            slug = generateSlug(title);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private String generateSlug(String title) {
        if (title == null) return "";
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }

    // Business logic methods
    public boolean isRegistrationOpen() {
        if (registrationDeadline == null) return true;
        return LocalDateTime.now().isBefore(registrationDeadline);
    }

    public boolean isFull() {
        return maxParticipants != null && currentParticipants >= maxParticipants;
    }

    public boolean canRegister() {
        return status == EventStatus.ACTIVE &&
                !isFull() &&
                isRegistrationOpen();
    }

    public void incrementParticipants() {
        if (currentParticipants == null) {
            currentParticipants = 0;
        }
        currentParticipants++;
    }

    public void decrementParticipants() {
        if (currentParticipants == null || currentParticipants <= 0) {
            currentParticipants = 0;
        } else {
            currentParticipants--;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventResponseDTO {

        private Long id;
        private String title;
        private String description;
        private String slug;
        private String location;
        private String venueDetails;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private EventStatus status;
        private Integer maxParticipants;
        private Integer currentParticipants;
        private BigDecimal registrationFee;
        private Long organizerId;
        private String organizerType;
        private String contactEmail;
        private String contactPhone;
        private String imageUrl;
        private Boolean isOnline;
        private String onlineLink;
        private Boolean requiresApproval;
        private LocalDateTime registrationDeadline;
        private String category;
        private String tags;
        private Boolean isFeatured;
        private String visibility;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long createdBy;
        private Long updatedBy;

        // Calculated fields
        private boolean registrationOpen;
        private boolean isFull;
        private boolean canRegister;
    }
}