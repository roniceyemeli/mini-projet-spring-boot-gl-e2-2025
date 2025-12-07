package com.service.event.service;

import com.service.event.entity.EventSubscription;
import com.service.event.enums.SubscriptionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IServiceEventSubscription {

    // ==================== CRUD OPERATIONS ====================

    EventSubscription createSubscription(EventSubscription subscription);
    EventSubscription updateSubscription(Long id, EventSubscription subscription);
    void deleteSubscription(Long id);
    EventSubscription getSubscriptionById(Long id);
    List<EventSubscription> getAllSubscriptions();

    // ==================== SUBSCRIPTION MANAGEMENT ====================

    EventSubscription subscribeToEvent(Long eventId, Long userId, Long studentId);
    void unsubscribeFromEvent(Long subscriptionId);
    EventSubscription approveSubscription(Long subscriptionId);
    EventSubscription rejectSubscription(Long subscriptionId, String reason);
    EventSubscription cancelSubscription(Long subscriptionId, String reason);
    EventSubscription moveToWaitlist(Long subscriptionId);

    // ==================== STATUS MANAGEMENT ====================

    EventSubscription changeSubscriptionStatus(Long subscriptionId, SubscriptionStatus status);
    List<EventSubscription> getSubscriptionsByStatus(SubscriptionStatus status);
    List<EventSubscription> getSubscriptionsByEventAndStatus(Long eventId, SubscriptionStatus status);

    // ==================== QUERIES & FILTERS ====================

    List<EventSubscription> getSubscriptionsByEvent(Long eventId);
    List<EventSubscription> getSubscriptionsByUser(Long userId);
    List<EventSubscription> getSubscriptionsByStudent(Long studentId);
    Optional<EventSubscription> getSubscriptionByEventAndUser(Long eventId, Long userId);
    List<EventSubscription> getActiveSubscriptions();
    List<EventSubscription> getPastSubscriptions();
    List<EventSubscription> getUpcomingSubscriptions();

    // ==================== ATTENDANCE & CHECK-IN ====================

    EventSubscription checkIn(Long subscriptionId, String checkInCode);
    EventSubscription checkOut(Long subscriptionId);
    List<EventSubscription> getAttendeesForEvent(Long eventId);
    List<EventSubscription> getNoShowsForEvent(Long eventId);

    // ==================== PAYMENT MANAGEMENT ====================

    EventSubscription updatePaymentStatus(Long subscriptionId, String paymentStatus, String paymentReference);
    EventSubscription markAsPaid(Long subscriptionId, Double amount, String reference);
    List<EventSubscription> getSubscriptionsByPaymentStatus(String paymentStatus);

    // ==================== FEEDBACK & RATINGS ====================

    EventSubscription submitFeedback(Long subscriptionId, Integer rating, String comment);
    List<EventSubscription> getSubscriptionsWithFeedback(Long eventId);
    Double getAverageRatingForEvent(Long eventId);

    // ==================== CERTIFICATES ====================

    EventSubscription issueCertificate(Long subscriptionId);
    List<EventSubscription> getCertificatesIssued(Long eventId);

    // ==================== VALIDATION & UTILITY ====================

    boolean isUserSubscribedToEvent(Long eventId, Long userId);
    boolean canSubscribeToEvent(Long eventId, Long userId);
    boolean subscriptionExists(Long eventId, Long userId);
    void validateSubscription(Long subscriptionId);

    // ==================== STATISTICS ====================

    Long getTotalSubscriptions();
    Long getTotalSubscriptionsForEvent(Long eventId);
    Long getApprovedSubscriptionsForEvent(Long eventId);
    Long getWaitlistedSubscriptionsForEvent(Long eventId);
    List<Object[]> getSubscriptionStatsByEvent();
    List<Object[]> getSubscriptionStatsByStatus();

    // ==================== BULK OPERATIONS ====================

    void approveAllPendingForEvent(Long eventId);
    void moveAllWaitlistedToApproved(Long eventId);
    void cancelAllForEvent(Long eventId, String reason);
}