package com.service.event.dto.event;

import com.service.event.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventResponseDTO {

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