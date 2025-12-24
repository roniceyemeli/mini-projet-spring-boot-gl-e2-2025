package com.service.event.repository;

import com.service.event.entity.Event;
import com.service.event.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

    Optional<Event> findBySlug(String slug);

    List<Event> findByStatus(EventStatus status);

    List<Event> findByIsFeaturedTrueAndStatus(EventStatus status);

    List<Event> findByIsFeaturedTrueAndStatusAndCategory(EventStatus status, String category);

    List<Event> findByOrganizerIdAndOrganizerType(UUID organizerId, String organizerType);

    List<Event> findByCategory(String category);

    List<Event> findByIsOnlineTrueAndStatus(EventStatus status);

    List<Event> findByIsOnlineFalseAndStatus(EventStatus status);

    List<Event> findByVisibilityAndStatus(String visibility, EventStatus status);

    @Query("SELECT e FROM Event e WHERE e.status = :status AND e.endDate >= :today ORDER BY e.startDate ASC")
    List<Event> findUpcomingEvents(@Param("today") LocalDate today);

    @Query("SELECT e FROM Event e WHERE e.endDate < :today ORDER BY e.endDate DESC")
    List<Event> findPastEvents(@Param("today") LocalDate today);

    @Query("SELECT e FROM Event e WHERE e.startDate BETWEEN :startDate AND :endDate ORDER BY e.startDate ASC")
    List<Event> findByDateRange(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    @Query("SELECT e FROM Event e WHERE " +
            "(LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.tags) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND e.status = 'ACTIVE' " +
            "ORDER BY e.isFeatured DESC, e.startDate ASC")
    List<Event> searchEvents(@Param("keyword") String keyword);

    @Query("SELECT e.category, COUNT(e) FROM Event e WHERE e.status = 'ACTIVE' GROUP BY e.category")
    List<Object[]> countEventsByCategory();

    @Query("SELECT e.status, COUNT(e) FROM Event e GROUP BY e.status")
    List<Object[]> countEventsByStatus();

    Long countByStatus(EventStatus status);

    Long countByIsFeaturedTrueAndStatus(EventStatus status);

    boolean existsBySlug(String slug);
}