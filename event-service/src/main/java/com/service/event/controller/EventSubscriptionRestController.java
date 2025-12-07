package com.service.event.controller;

import com.service.event.dto.eventSubscription.EventSubscriptionDTO;
import com.service.event.dto.eventSubscription.EventSubscriptionResponseDTO;
import com.service.event.entity.EventSubscription;
import com.service.event.enums.SubscriptionStatus;
import com.service.event.service.IServiceEventSubscription;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/event-subscriptions")
@CrossOrigin(origins = "*")
public class EventSubscriptionRestController {

    private final IServiceEventSubscription subscriptionService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<EventSubscriptionResponseDTO> createSubscription(
            @Valid @RequestBody EventSubscriptionDTO subscriptionDTO) {
        EventSubscription subscription = modelMapper.map(subscriptionDTO, EventSubscription.class);
        EventSubscription created = subscriptionService.createSubscription(subscription);
        EventSubscriptionResponseDTO response = modelMapper.map(created, EventSubscriptionResponseDTO.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/subscribe")
    public ResponseEntity<EventSubscriptionResponseDTO> subscribeToEvent(
            @RequestParam Long eventId,
            @RequestParam Long userId,
            @RequestParam Long studentId) {
        EventSubscription subscription = subscriptionService.subscribeToEvent(eventId, userId, studentId);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventSubscriptionResponseDTO> getSubscriptionById(@PathVariable Long id) {
        EventSubscription subscription = subscriptionService.getSubscriptionById(id);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EventSubscriptionResponseDTO>> getAllSubscriptions(
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) SubscriptionStatus status) {

        List<EventSubscription> subscriptions;

        if (eventId != null && status != null) {
            subscriptions = subscriptionService.getSubscriptionsByEventAndStatus(eventId, status);
        } else if (eventId != null) {
            subscriptions = subscriptionService.getSubscriptionsByEvent(eventId);
        } else if (userId != null) {
            subscriptions = subscriptionService.getSubscriptionsByUser(userId);
        } else if (status != null) {
            subscriptions = subscriptionService.getSubscriptionsByStatus(status);
        } else {
            subscriptions = subscriptionService.getAllSubscriptions();
        }

        List<EventSubscriptionResponseDTO> responses = subscriptions.stream()
                .map(sub -> modelMapper.map(sub, EventSubscriptionResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<EventSubscriptionResponseDTO> approveSubscription(@PathVariable Long id) {
        EventSubscription subscription = subscriptionService.approveSubscription(id);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<EventSubscriptionResponseDTO> rejectSubscription(
            @PathVariable Long id,
            @RequestParam String reason) {
        EventSubscription subscription = subscriptionService.rejectSubscription(id, reason);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<EventSubscriptionResponseDTO> cancelSubscription(
            @PathVariable Long id,
            @RequestParam String reason) {
        EventSubscription subscription = subscriptionService.cancelSubscription(id, reason);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/check-in")
    public ResponseEntity<EventSubscriptionResponseDTO> checkIn(
            @PathVariable Long id,
            @RequestParam String checkInCode) {
        EventSubscription subscription = subscriptionService.checkIn(id, checkInCode);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/feedback")
    public ResponseEntity<EventSubscriptionResponseDTO> submitFeedback(
            @PathVariable Long id,
            @RequestParam Integer rating,
            @RequestParam String comment) {
        EventSubscription subscription = subscriptionService.submitFeedback(id, rating, comment);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/issue-certificate")
    public ResponseEntity<EventSubscriptionResponseDTO> issueCertificate(@PathVariable Long id) {
        EventSubscription subscription = subscriptionService.issueCertificate(id);
        EventSubscriptionResponseDTO response = modelMapper.map(subscription, EventSubscriptionResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event/{eventId}/attendees")
    public ResponseEntity<List<EventSubscriptionResponseDTO>> getAttendees(@PathVariable Long eventId) {
        List<EventSubscription> attendees = subscriptionService.getAttendeesForEvent(eventId);
        List<EventSubscriptionResponseDTO> responses = attendees.stream()
                .map(sub -> modelMapper.map(sub, EventSubscriptionResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/event/{eventId}/stats")
    public ResponseEntity<EventStatsResponse> getEventStats(@PathVariable Long eventId) {
        Long total = subscriptionService.getTotalSubscriptionsForEvent(eventId);
        Long approved = subscriptionService.getApprovedSubscriptionsForEvent(eventId);
        Long waitlisted = subscriptionService.getWaitlistedSubscriptionsForEvent(eventId);

        EventStatsResponse stats = new EventStatsResponse(total, approved, waitlisted);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/user/{userId}/events")
    public ResponseEntity<List<EventSubscriptionResponseDTO>> getUserSubscriptions(@PathVariable Long userId) {
        List<EventSubscription> subscriptions = subscriptionService.getSubscriptionsByUser(userId);
        List<EventSubscriptionResponseDTO> responses = subscriptions.stream()
                .map(sub -> modelMapper.map(sub, EventSubscriptionResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/check-subscription")
    public ResponseEntity<Boolean> checkUserSubscription(
            @RequestParam Long eventId,
            @RequestParam Long userId) {
        boolean isSubscribed = subscriptionService.isUserSubscribedToEvent(eventId, userId);
        return ResponseEntity.ok(isSubscribed);
    }

    @GetMapping("/can-subscribe")
    public ResponseEntity<Boolean> canSubscribe(
            @RequestParam Long eventId,
            @RequestParam Long userId) {
        boolean canSubscribe = subscriptionService.canSubscribeToEvent(eventId, userId);
        return ResponseEntity.ok(canSubscribe);
    }

    // Helper class for stats response
    private static class EventStatsResponse {
        private Long totalSubscriptions;
        private Long approvedSubscriptions;
        private Long waitlistedSubscriptions;

        public EventStatsResponse(Long total, Long approved, Long waitlisted) {
            this.totalSubscriptions = total;
            this.approvedSubscriptions = approved;
            this.waitlistedSubscriptions = waitlisted;
        }

        // Getters
        public Long getTotalSubscriptions() { return totalSubscriptions; }
        public Long getApprovedSubscriptions() { return approvedSubscriptions; }
        public Long getWaitlistedSubscriptions() { return waitlistedSubscriptions; }
    }
}