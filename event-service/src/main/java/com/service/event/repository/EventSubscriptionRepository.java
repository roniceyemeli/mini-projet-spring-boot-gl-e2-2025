package com.service.event.repository;

import com.service.event.entity.EventSubscription;
import com.service.event.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventSubscriptionRepository extends JpaRepository<EventSubscription, Long> {

    List<EventSubscription> findByEventId(Long eventId);

    List<EventSubscription> findByUserId(Long userId);

    List<EventSubscription> findByStudentId(Long studentId);

    List<EventSubscription> findByStatus(SubscriptionStatus status);

    List<EventSubscription> findByEventIdAndStatus(Long eventId, SubscriptionStatus status);

    Optional<EventSubscription> findByEventIdAndUserId(Long eventId, Long userId);

    boolean existsByEventIdAndUserId(Long eventId, Long userId);

    Long countByEventId(Long eventId);

    Long countByEventIdAndStatus(Long eventId, SubscriptionStatus status);

    List<EventSubscription> findByPaymentStatus(String paymentStatus);

    List<EventSubscription> findByEventIdAndAttendedTrue(Long eventId);

    List<EventSubscription> findByEventIdAndFeedbackSubmittedTrue(Long eventId);

    List<EventSubscription> findByEventIdAndCertificateIssuedTrue(Long eventId);

    Optional<EventSubscription> findFirstByEventIdAndStatusOrderByCreatedAtAsc(Long eventId, SubscriptionStatus status);

    @Query("SELECT es FROM EventSubscription es WHERE es.status IN :statuses AND es.isActive = true")
    List<EventSubscription> findActiveSubscriptions(@Param("statuses") List<SubscriptionStatus> statuses);

    default List<EventSubscription> findActiveSubscriptions() {
        return findActiveSubscriptions(List.of(
                SubscriptionStatus.PENDING,
                SubscriptionStatus.APPROVED,
                SubscriptionStatus.WAITLISTED
        ));
    }

    @Query("SELECT es FROM EventSubscription es JOIN Event e ON es.eventId = e.id " +
            "WHERE e.endDate < CURRENT_DATE AND es.status = 'ATTENDED'")
    List<EventSubscription> findPastSubscriptions();

    @Query("SELECT es FROM EventSubscription es JOIN Event e ON es.eventId = e.id " +
            "WHERE e.startDate > CURRENT_DATE AND es.status = 'APPROVED'")
    List<EventSubscription> findUpcomingSubscriptions();

    @Query("SELECT e.id, e.title, COUNT(es) as total, " +
            "SUM(CASE WHEN es.status = 'APPROVED' THEN 1 ELSE 0 END) as approved, " +
            "SUM(CASE WHEN es.status = 'PENDING' THEN 1 ELSE 0 END) as pending, " +
            "SUM(CASE WHEN es.status = 'WAITLISTED' THEN 1 ELSE 0 END) as waitlisted " +
            "FROM EventSubscription es JOIN Event e ON es.eventId = e.id " +
            "GROUP BY e.id, e.title")
    List<Object[]> getSubscriptionStatsByEvent();

    @Query("SELECT es.status, COUNT(es) FROM EventSubscription es GROUP BY es.status")
    List<Object[]> getSubscriptionStatsByStatus();

    @Query("SELECT AVG(es.rating) FROM EventSubscription es WHERE es.eventId = :eventId AND es.rating IS NOT NULL")
    Double findAverageRatingByEventId(@Param("eventId") Long eventId);
}