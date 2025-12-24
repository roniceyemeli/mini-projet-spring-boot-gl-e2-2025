package com.service.event.service;

import com.service.event.entity.EventSubscription;
import com.service.event.enums.SubscriptionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IServiceEventSubscription {

    // ==================== CRUD OPERATIONS ====================

    EventSubscription createSubscription(EventSubscription subscription);
    EventSubscription updateSubscription(UUID id, EventSubscription subscription);
    void deleteSubscription(UUID id);
    EventSubscription getSubscriptionById(UUID id);
    List<EventSubscription> getAllSubscriptions();

    // ==================== SUBSCRIPTION MANAGEMENT ====================

    EventSubscription subscribeToEvent(UUID eventId, UUID userId, UUID studentId);
    void unsubscribeFromEvent(UUID subscriptionId);
    EventSubscription approveSubscription(UUID subscriptionId);
    EventSubscription rejectSubscription(UUID subscriptionId, String reason);
    EventSubscription cancelSubscription(UUID subscriptionId, String reason);
    EventSubscription moveToWaitlist(UUID subscriptionId);

    // ==================== STATUS MANAGEMENT ====================

    EventSubscription changeSubscriptionStatus(UUID subscriptionId, SubscriptionStatus status);
    List<EventSubscription> getSubscriptionsByStatus(SubscriptionStatus status);
    List<EventSubscription> getSubscriptionsByEventAndStatus(UUID eventId, SubscriptionStatus status);

    // ==================== QUERIES & FILTERS ====================

    List<EventSubscription> getSubscriptionsByEvent(UUID eventId);
    List<EventSubscription> getSubscriptionsByUser(UUID userId);
    List<EventSubscription> getSubscriptionsByStudent(UUID studentId);
    Optional<EventSubscription> getSubscriptionByEventAndUser(UUID eventId, UUID userId);
    List<EventSubscription> getActiveSubscriptions();
    List<EventSubscription> getPastSubscriptions();
    List<EventSubscription> getUpcomingSubscriptions();

    // ==================== ATTENDANCE & CHECK-IN ====================

    EventSubscription checkIn(UUID subscriptionId, String checkInCode);
    EventSubscription checkOut(UUID subscriptionId);
    List<EventSubscription> getAttendeesForEvent(UUID eventId);
    List<EventSubscription> getNoShowsForEvent(UUID eventId);

    // ==================== PAYMENT MANAGEMENT ====================

    EventSubscription updatePaymentStatus(UUID subscriptionId, String paymentStatus, String paymentReference);
    EventSubscription markAsPaid(UUID subscriptionId, Double amount, String reference);
    List<EventSubscription> getSubscriptionsByPaymentStatus(String paymentStatus);

    // ==================== FEEDBACK & RATINGS ====================

    EventSubscription submitFeedback(UUID subscriptionId, Integer rating, String comment);
    List<EventSubscription> getSubscriptionsWithFeedback(UUID eventId);
    Double getAverageRatingForEvent(UUID eventId);

    // ==================== CERTIFICATES ====================

    EventSubscription issueCertificate(UUID subscriptionId);
    List<EventSubscription> getCertificatesIssued(UUID eventId);

    // ==================== VALIDATION & UTILITY ====================

    boolean isUserSubscribedToEvent(UUID eventId, UUID userId);
    boolean canSubscribeToEvent(UUID eventId, UUID userId);
    boolean subscriptionExists(UUID eventId, UUID userId);
    void validateSubscription(UUID subscriptionId);

    // ==================== STATISTICS ====================

    Long getTotalSubscriptions();
    Long getTotalSubscriptionsForEvent(UUID eventId);
    Long getApprovedSubscriptionsForEvent(UUID eventId);
    Long getWaitlistedSubscriptionsForEvent(UUID eventId);
    List<Object[]> getSubscriptionStatsByEvent();
    List<Object[]> getSubscriptionStatsByStatus();

    // ==================== BULK OPERATIONS ====================

    void approveAllPendingForEvent(UUID eventId);
    void moveAllWaitlistedToApproved(UUID eventId);
    void cancelAllForEvent(UUID eventId, String reason);
}