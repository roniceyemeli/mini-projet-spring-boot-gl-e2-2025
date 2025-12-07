package com.service.event.dto.event;

import com.service.event.enums.EventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EventDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String slug;

    @NotBlank(message = "Location is required")
    private String location;

    private String venueDetails;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalTime startTime;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private LocalTime endTime;

    private EventStatus status;
    private Integer maxParticipants;
    private BigDecimal registrationFee;

    @NotNull(message = "Organizer ID is required")
    private Long organizerId;

    private String organizerType;
    private String contactEmail;
    private String contactPhone;
    private String imageUrl;
    private Boolean isOnline = false;
    private String onlineLink;
    private Boolean requiresApproval = false;
    private LocalDateTime registrationDeadline;
    private String category;
    private String tags;
    private Boolean isFeatured = false;
    private String visibility = "PUBLIC";
    private Long createdBy;
    private Long updatedBy;
}