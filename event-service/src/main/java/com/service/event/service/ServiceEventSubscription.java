package com.service.event.service;

import com.service.event.entity.Event;
import com.service.event.entity.EventSubscription;
import com.service.event.enums.SubscriptionStatus;
import com.service.event.repository.EventRepository;
import com.service.event.repository.EventSubscriptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ServiceEventSubscription implements IServiceEventSubscription {

    private final EventSubscriptionRepository subscriptionRepository;
    private final EventRepository eventRepository;
    private final IServiceEvent eventService;

    @Override
    public EventSubscription createSubscription(EventSubscription subscription) {
        log.info("Creating subscription for event: {}, user: {}",
                subscription.getEventId(), subscription.getUserId());

        // Validate event exists and is available for subscription
        Event event = eventRepository.findById(subscription.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + subscription.getEventId()));

        if (!event.canRegister()) {
            throw new RuntimeException("Event is not available for registration");
        }

        // Check if user is already subscribed
        if (subscriptionRepository.existsByEventIdAndUserId(subscription.getEventId(), subscription.getUserId())) {
            throw new RuntimeException("User is already subscribed to this event");
        }

        // Check event capacity
        if (event.isFull()) {
            // Auto move to waitlist if event is full
            subscription.setStatus(SubscriptionStatus.WAITLISTED);
            log.info("Event is full, subscription moved to waitlist");
        }

        EventSubscription savedSubscription = subscriptionRepository.save(subscription);

        // Update event participant count if approved
        if (savedSubscription.getStatus() == SubscriptionStatus.APPROVED) {
            eventService.incrementParticipants(event.getId());
        }

        log.info("Subscription created successfully with ID: {}", savedSubscription.getId());
        return savedSubscription;
    }

    @Override
    public EventSubscription subscribeToEvent(Long eventId, Long userId, Long studentId) {
        log.info("Subscribing user {} to event {}", userId, eventId);

        EventSubscription subscription = new EventSubscription();
        subscription.setEventId(eventId);
        subscription.setUserId(userId);
        subscription.setStudentId(studentId);
        subscription.setRegistrationDate(LocalDateTime.now());
        subscription.setStatus(SubscriptionStatus.PENDING);

        return createSubscription(subscription);
    }

    @Override
    public EventSubscription approveSubscription(Long subscriptionId) {
        log.info("Approving subscription: {}", subscriptionId);

        EventSubscription subscription = getSubscriptionById(subscriptionId);

        // Check if event still has capacity
        Event event = eventRepository.findById(subscription.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.isFull()) {
            throw new RuntimeException("Event is full, cannot approve subscription");
        }

        subscription.approve();
        EventSubscription updatedSubscription = subscriptionRepository.save(subscription);

        // Update event participant count
        eventService.incrementParticipants(event.getId());

        log.info("Subscription {} approved", subscriptionId);
        return updatedSubscription;
    }

    @Override
    public EventSubscription rejectSubscription(Long subscriptionId, String reason) {
        log.info("Rejecting subscription {} with reason: {}", subscriptionId, reason);

        EventSubscription subscription = getSubscriptionById(subscriptionId);
        subscription.setStatus(SubscriptionStatus.REJECTED);
        subscription.setNotes(reason);

        return subscriptionRepository.save(subscription);
    }

    @Override
    public EventSubscription cancelSubscription(Long subscriptionId, String reason) {
        log.info("Cancelling subscription {} with reason: {}", subscriptionId, reason);

        EventSubscription subscription = getSubscriptionById(subscriptionId);

        if (!subscription.canCancel()) {
            throw new RuntimeException("Subscription cannot be cancelled in current status");
        }

        subscription.cancel(reason);
        EventSubscription updatedSubscription = subscriptionRepository.save(subscription);

        // Decrement event participant count if subscription was approved
        if (subscription.getStatus() == SubscriptionStatus.APPROVED) {
            eventService.decrementParticipants(subscription.getEventId());

            // Check if there are waitlisted users to approve
            approveFirstWaitlisted(subscription.getEventId());
        }

        return updatedSubscription;
    }

    @Override
    public EventSubscription checkIn(Long subscriptionId, String checkInCode) {
        log.info("Checking in subscription: {}", subscriptionId);

        EventSubscription subscription = getSubscriptionById(subscriptionId);

        if (!subscription.canCheckIn()) {
            throw new RuntimeException("Subscription cannot be checked in");
        }

        // Validate check-in code
        if (!subscription.getCheckInCode().equals(checkInCode)) {
            throw new RuntimeException("Invalid check-in code");
        }

        subscription.checkIn();
        return subscriptionRepository.save(subscription);
    }

    @Override
    public EventSubscription submitFeedback(Long subscriptionId, Integer rating, String comment) {
        log.info("Submitting feedback for subscription: {}", subscriptionId);

        EventSubscription subscription = getSubscriptionById(subscriptionId);

        if (!subscription.canSubmitFeedback()) {
            throw new RuntimeException("Feedback cannot be submitted for this subscription");
        }

        subscription.submitFeedback(rating, comment);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public EventSubscription issueCertificate(Long subscriptionId) {
        log.info("Issuing certificate for subscription: {}", subscriptionId);

        EventSubscription subscription = getSubscriptionById(subscriptionId);

        if (!subscription.getAttended()) {
            throw new RuntimeException("Cannot issue certificate for non-attended subscription");
        }

        subscription.issueCertificate();
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<EventSubscription> getSubscriptionsByEvent(Long eventId) {
        log.debug("Fetching subscriptions for event: {}", eventId);
        return subscriptionRepository.findByEventId(eventId);
    }

    @Override
    public List<EventSubscription> getSubscriptionsByUser(Long userId) {
        log.debug("Fetching subscriptions for user: {}", userId);
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public Optional<EventSubscription> getSubscriptionByEventAndUser(Long eventId, Long userId) {
        log.debug("Fetching subscription for event {} and user {}", eventId, userId);
        return subscriptionRepository.findByEventIdAndUserId(eventId, userId);
    }

    @Override
    public boolean isUserSubscribedToEvent(Long eventId, Long userId) {
        return subscriptionRepository.existsByEventIdAndUserId(eventId, userId);
    }

    @Override
    public Long getTotalSubscriptionsForEvent(Long eventId) {
        return subscriptionRepository.countByEventId(eventId);
    }

    @Override
    public Long getApprovedSubscriptionsForEvent(Long eventId) {
        return subscriptionRepository.countByEventIdAndStatus(eventId, SubscriptionStatus.APPROVED);
    }

    @Override
    public Long getWaitlistedSubscriptionsForEvent(Long eventId) {
        return subscriptionRepository.countByEventIdAndStatus(eventId, SubscriptionStatus.WAITLISTED);
    }

    @Override
    public List<Object[]> getSubscriptionStatsByEvent() {
        return subscriptionRepository.getSubscriptionStatsByEvent();
    }

    // Helper method to approve first waitlisted user when a spot opens
    private void approveFirstWaitlisted(Long eventId) {
        Optional<EventSubscription> firstWaitlisted = subscriptionRepository
                .findFirstByEventIdAndStatusOrderByCreatedAtAsc(eventId, SubscriptionStatus.WAITLISTED);

        if (firstWaitlisted.isPresent()) {
            EventSubscription waitlisted = firstWaitlisted.get();
            waitlisted.setStatus(SubscriptionStatus.APPROVED);
            waitlisted.setApprovalDate(LocalDateTime.now());
            subscriptionRepository.save(waitlisted);

            log.info("Auto-approved waitlisted subscription {} for event {}",
                    waitlisted.getId(), eventId);
        }
    }

    // Implementing other methods from interface...
    @Override
    public EventSubscription updateSubscription(Long id, EventSubscription subscription) {
        EventSubscription existing = getSubscriptionById(id);
        // Update logic here
        return subscriptionRepository.save(existing);
    }

    @Override
    public void deleteSubscription(Long id) {
        EventSubscription subscription = getSubscriptionById(id);
        subscriptionRepository.delete(subscription);
    }

    @Override
    public EventSubscription getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
    }

    @Override
    public List<EventSubscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public void unsubscribeFromEvent(Long subscriptionId) {
        cancelSubscription(subscriptionId, "User unsubscribed");
    }

    @Override
    public EventSubscription moveToWaitlist(Long subscriptionId) {
        EventSubscription subscription = getSubscriptionById(subscriptionId);
        subscription.setStatus(SubscriptionStatus.WAITLISTED);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public EventSubscription changeSubscriptionStatus(Long subscriptionId, SubscriptionStatus status) {
        EventSubscription subscription = getSubscriptionById(subscriptionId);
        subscription.setStatus(status);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<EventSubscription> getSubscriptionsByStatus(SubscriptionStatus status) {
        return subscriptionRepository.findByStatus(status);
    }

    @Override
    public List<EventSubscription> getSubscriptionsByEventAndStatus(Long eventId, SubscriptionStatus status) {
        return subscriptionRepository.findByEventIdAndStatus(eventId, status);
    }

    @Override
    public List<EventSubscription> getSubscriptionsByStudent(Long studentId) {
        return subscriptionRepository.findByStudentId(studentId);
    }

    @Override
    public List<EventSubscription> getActiveSubscriptions() {
        return subscriptionRepository.findActiveSubscriptions();
    }

    @Override
    public List<EventSubscription> getPastSubscriptions() {
        return subscriptionRepository.findPastSubscriptions();
    }

    @Override
    public List<EventSubscription> getUpcomingSubscriptions() {
        return subscriptionRepository.findUpcomingSubscriptions();
    }

    @Override
    public EventSubscription checkOut(Long subscriptionId) {
        EventSubscription subscription = getSubscriptionById(subscriptionId);
        subscription.checkOut();
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<EventSubscription> getAttendeesForEvent(Long eventId) {
        return subscriptionRepository.findByEventIdAndAttendedTrue(eventId);
    }

    @Override
    public List<EventSubscription> getNoShowsForEvent(Long eventId) {
        return subscriptionRepository.findByEventIdAndStatus(eventId, SubscriptionStatus.NO_SHOW);
    }

    @Override
    public EventSubscription updatePaymentStatus(Long subscriptionId, String paymentStatus, String paymentReference) {
        EventSubscription subscription = getSubscriptionById(subscriptionId);
        subscription.setPaymentStatus(paymentStatus);
        subscription.setPaymentReference(paymentReference);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public EventSubscription markAsPaid(Long subscriptionId, Double amount, String reference) {
        EventSubscription subscription = getSubscriptionById(subscriptionId);
        subscription.setPaymentStatus("PAID");
        subscription.setAmountPaid(amount);
        subscription.setPaymentReference(reference);
        return subscriptionRepository.save(subscription);
    }

    @Override
    public List<EventSubscription> getSubscriptionsByPaymentStatus(String paymentStatus) {
        return subscriptionRepository.findByPaymentStatus(paymentStatus);
    }

    @Override
    public List<EventSubscription> getSubscriptionsWithFeedback(Long eventId) {
        return subscriptionRepository.findByEventIdAndFeedbackSubmittedTrue(eventId);
    }

    @Override
    public Double getAverageRatingForEvent(Long eventId) {
        return subscriptionRepository.findAverageRatingByEventId(eventId);
    }

    @Override
    public List<EventSubscription> getCertificatesIssued(Long eventId) {
        return subscriptionRepository.findByEventIdAndCertificateIssuedTrue(eventId);
    }

    @Override
    public boolean canSubscribeToEvent(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return event.canRegister() &&
                !isUserSubscribedToEvent(eventId, userId);
    }

    @Override
    public boolean subscriptionExists(Long eventId, Long userId) {
        return subscriptionRepository.existsByEventIdAndUserId(eventId, userId);
    }

    @Override
    public void validateSubscription(Long subscriptionId) {
        EventSubscription subscription = getSubscriptionById(subscriptionId);
        // Add validation logic
    }

    @Override
    public Long getTotalSubscriptions() {
        return subscriptionRepository.count();
    }

    @Override
    public List<Object[]> getSubscriptionStatsByStatus() {
        return subscriptionRepository.getSubscriptionStatsByStatus();
    }

    @Override
    public void approveAllPendingForEvent(Long eventId) {
        List<EventSubscription> pending = subscriptionRepository
                .findByEventIdAndStatus(eventId, SubscriptionStatus.PENDING);

        pending.forEach(subscription -> {
            subscription.setStatus(SubscriptionStatus.APPROVED);
            subscription.setApprovalDate(LocalDateTime.now());
        });

        subscriptionRepository.saveAll(pending);
    }

    @Override
    public void moveAllWaitlistedToApproved(Long eventId) {
        List<EventSubscription> waitlisted = subscriptionRepository
                .findByEventIdAndStatus(eventId, SubscriptionStatus.WAITLISTED);

        waitlisted.forEach(subscription -> {
            subscription.setStatus(SubscriptionStatus.APPROVED);
            subscription.setApprovalDate(LocalDateTime.now());
        });

        subscriptionRepository.saveAll(waitlisted);
    }

    @Override
    public void cancelAllForEvent(Long eventId, String reason) {
        List<EventSubscription> subscriptions = subscriptionRepository.findByEventId(eventId);

        subscriptions.forEach(subscription -> {
            subscription.setStatus(SubscriptionStatus.CANCELLED);
            subscription.setCancellationReason(reason);
            subscription.setCancellationDate(LocalDateTime.now());
            subscription.setIsActive(false);
        });

        subscriptionRepository.saveAll(subscriptions);
    }
}