package com.service.event.dto.eventSubscription;


import com.service.event.enums.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSubscriptionDTO {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    private SubscriptionStatus status;
    private String notes;
    private String paymentStatus;
    private String paymentReference;
    private Double amountPaid;
    private Long createdBy;
}