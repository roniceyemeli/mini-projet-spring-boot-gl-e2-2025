package com.service.event.dto.eventSubscription;


import com.service.event.enums.SubscriptionStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventSubscriptionDTO {

    @NotNull(message = "Event ID is required")
    private UUID eventId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Student ID is required")
    private UUID studentId;

    private SubscriptionStatus status;
    private String notes;
    private String paymentStatus;
    private String paymentReference;
    private Double amountPaid;
    private UUID createdBy;
}