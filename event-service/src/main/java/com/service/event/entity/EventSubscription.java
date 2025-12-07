package com.service.event.entity;

import com.service.event.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_subscriptions", schema = "event_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", insertable = false, updatable = false)
    private Event event;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubscriptionStatus status = SubscriptionStatus.PENDING;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "approval_date")
    private LocalDateTime approvalDate;

    @Column(name = "cancellation_date")
    private LocalDateTime cancellationDate;

    @Column(name = "attended")
    private Boolean attended = false;

    @Column(name = "attendance_date")
    private LocalDateTime attendanceDate;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "PENDING"; // PENDING, PAID, FAILED, REFUNDED

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Column(name = "amount_paid", precision = 10, scale = 2)
    private Double amountPaid;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "check_in_code", length = 50)
    private String checkInCode;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "certificate_issued")
    private Boolean certificateIssued = false;

    @Column(name = "certificate_issue_date")
    private LocalDateTime certificateIssueDate;

    @Column(name = "feedback_submitted")
    private Boolean feedbackSubmitted = false;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "feedback_comment", columnDefinition = "TEXT")
    private String feedbackComment;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "cancellation_reason", length = 500)
    private String cancellationReason;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
        if (status == null) {
            status = SubscriptionStatus.PENDING;
        }
        if (isActive == null) {
            isActive = true;
        }

        // Generate check-in code if not provided
        if (checkInCode == null || checkInCode.trim().isEmpty()) {
            checkInCode = generateCheckInCode();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Update dates based on status changes
        if (status == SubscriptionStatus.APPROVED && approvalDate == null) {
            approvalDate = LocalDateTime.now();
        }
        if (status == SubscriptionStatus.CANCELLED && cancellationDate == null) {
            cancellationDate = LocalDateTime.now();
        }
        if (attended != null && attended && attendanceDate == null) {
            attendanceDate = LocalDateTime.now();
        }
    }

    // Business logic methods
    public boolean canCancel() {
        return status == SubscriptionStatus.PENDING ||
                status == SubscriptionStatus.APPROVED ||
                status == SubscriptionStatus.WAITLISTED;
    }

    public boolean canCheckIn() {
        return status == SubscriptionStatus.APPROVED &&
                !attended &&
                event != null &&
                event.canRegister();
    }

    public boolean canSubmitFeedback() {
        return attended && !feedbackSubmitted;
    }

    public void approve() {
        this.status = SubscriptionStatus.APPROVED;
        this.approvalDate = LocalDateTime.now();
    }

    public void cancel(String reason) {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancellationDate = LocalDateTime.now();
        this.cancellationReason = reason;
        this.isActive = false;
    }

    public void checkIn() {
        this.attended = true;
        this.attendanceDate = LocalDateTime.now();
        this.checkInTime = LocalDateTime.now();
    }

    public void checkOut() {
        this.checkOutTime = LocalDateTime.now();
    }

    public void submitFeedback(Integer rating, String comment) {
        this.rating = rating;
        this.feedbackComment = comment;
        this.feedbackSubmitted = true;
    }

    public void issueCertificate() {
        this.certificateIssued = true;
        this.certificateIssueDate = LocalDateTime.now();
    }

    private String generateCheckInCode() {
        // Generate a unique check-in code (simplified version)
        return "EVT" + System.currentTimeMillis() % 10000 +
                String.format("%04d", (int)(Math.random() * 10000));
    }
}