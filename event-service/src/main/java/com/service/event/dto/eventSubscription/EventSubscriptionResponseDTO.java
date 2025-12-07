package com.service.event.dto.eventSubscription;

import com.service.event.dto.event.EventResponseDTO;
import com.service.event.enums.SubscriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSubscriptionResponseDTO {

    private Long id;
    private Long eventId;
    private EventResponseDTO eventDetails;
    private Long userId;
    private Long studentId;
    private SubscriptionStatus status;
    private LocalDateTime registrationDate;
    private LocalDateTime approvalDate;
    private LocalDateTime cancellationDate;
    private Boolean attended;
    private LocalDateTime attendanceDate;
    private String paymentStatus;
    private String paymentReference;
    private Double amountPaid;
    private String notes;
    private String checkInCode;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private Boolean certificateIssued;
    private LocalDateTime certificateIssueDate;
    private Boolean feedbackSubmitted;
    private Integer rating;
    private String feedbackComment;
    private Boolean isActive;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;

    // Calculated fields
    private boolean canCancel;
    private boolean canCheckIn;
    private boolean canSubmitFeedback;
}