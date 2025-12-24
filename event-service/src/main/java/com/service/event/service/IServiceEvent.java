package com.service.event.service;

import com.service.event.entity.Event;
import com.service.event.enums.EventStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IServiceEvent {

    // ==================== CRUD OPERATIONS ====================

    /**
     * Create a new event
     * @param event Event entity to create
     * @return Created event
     */
    Event createEvent(Event event);

    /**
     * Update an existing event
     * @param id Event ID
     * @param event Updated event data
     * @return Updated event
     */
    Event updateEvent(UUID id, Event event);

    /**
     * Delete an event by ID
     * @param id Event ID
     */
    void deleteEvent(UUID id);

    /**
     * Get event by ID
     * @param id Event ID
     * @return Event entity
     */
    Event getEventById(UUID id);

    /**
     * Get event by slug
     * @param slug Event slug
     * @return Optional of Event
     */
    Optional<Event> getEventBySlug(String slug);

    /**
     * Get all events
     * @return List of all events
     */
    List<Event> getAllEvents();


    // ==================== FEATURED EVENTS MANAGEMENT ====================

    /**
     * Get all featured events
     * @return List of featured events
     */
    List<Event> getFeaturedEvents();

    /**
     * Set featured status for an event
     * @param eventId Event ID
     * @param isFeatured Featured status
     * @return Updated event
     */
    Event setFeaturedStatus(UUID eventId, boolean isFeatured);

    /**
     * Get featured events by category
     * @param category Event category
     * @return List of featured events in category
     */
    List<Event> getFeaturedEventsByCategory(String category);


    // ==================== EVENT FILTERING & SEARCH ====================

    /**
     * Get active events
     * @return List of active events
     */
    List<Event> getActiveEvents();

    /**
     * Get upcoming events
     * @return List of upcoming events
     */
    List<Event> getUpcomingEvents();

    /**
     * Get past events
     * @return List of past events
     */
    List<Event> getPastEvents();

    /**
     * Get events by status
     * @param status Event status
     * @return List of events with given status
     */
    List<Event> getEventsByStatus(EventStatus status);

    /**
     * Get events by organizer
     * @param organizerId Organizer ID
     * @param organizerType Organizer type (SCHOOL, COMMUNITY, USER)
     * @return List of organizer's events
     */
    List<Event> getEventsByOrganizer(UUID organizerId, String organizerType);

    /**
     * Get events by category
     * @param category Event category
     * @return List of events in category
     */
    List<Event> getEventsByCategory(String category);

    /**
     * Search events by keyword
     * @param keyword Search keyword
     * @return List of matching events
     */
    List<Event> searchEvents(String keyword);

    /**
     * Get events by date range
     * @param startDate Start date
     * @param endDate End date
     * @return List of events in date range
     */
    List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate);


    // ==================== EVENT TYPE MANAGEMENT ====================

    /**
     * Get online events
     * @return List of online events
     */
    List<Event> getOnlineEvents();

    /**
     * Get physical events
     * @return List of physical events
     */
    List<Event> getPhysicalEvents();


    // ==================== EVENT STATUS & CAPACITY MANAGEMENT ====================

    /**
     * Change event status
     * @param eventId Event ID
     * @param status New status
     * @return Updated event
     */
    Event changeEventStatus(UUID eventId, EventStatus status);

    /**
     * Update event capacity
     * @param eventId Event ID
     * @param maxParticipants New max participants
     * @return Updated event
     */
    Event updateEventCapacity(UUID eventId, Integer maxParticipants);

    /**
     * Update event registration deadline
     * @param eventId Event ID
     * @param deadline New registration deadline
     * @return Updated event
     */
    Event updateEventRegistrationDeadline(UUID eventId, LocalDateTime deadline);


    // ==================== PARTICIPANT MANAGEMENT ====================

    /**
     * Increment event participants count
     * @param eventId Event ID
     * @return Updated event
     */
    Event incrementParticipants(UUID eventId);

    /**
     * Decrement event participants count
     * @param eventId Event ID
     * @return Updated event
     */
    Event decrementParticipants(UUID eventId);

    /**
     * Check if registration is possible for an event
     * @param eventId Event ID
     * @return true if registration is possible
     */
    boolean canRegisterForEvent(UUID eventId);


    // ==================== STATISTICS & ANALYTICS ====================

    /**
     * Get total events count
     * @return Total events count
     */
    Long getTotalEventsCount();

    /**
     * Get active events count
     * @return Active events count
     */
    Long getActiveEventsCount();

    /**
     * Get featured events count
     * @return Featured events count
     */
    Long getFeaturedEventsCount();

    /**
     * Get events count by category
     * @return List of [category, count] pairs
     */
    List<Object[]> getEventsCountByCategory();

    /**
     * Get events count by status
     * @return List of [status, count] pairs
     */
    List<Object[]> getEventsCountByStatus();


    // ==================== VALIDATION & UTILITY METHODS ====================

    List<Event> getPublicEvents();

    List<Event> getEventsByVisibility(String visibility);

    boolean isEventVisible(UUID eventId, UUID userId);

    /**
     * Check if event exists
     * @param eventId Event ID
     * @return true if event exists
     */
    boolean eventExists(UUID eventId);

    /**
     * Check if slug exists
     * @param slug Event slug
     * @return true if slug exists
     */
    boolean slugExists(String slug);

    /**
     * Validate event capacity
     * @param eventId Event ID
     * @throws RuntimeException if event is full
     */
    void validateEventCapacity(UUID eventId);


    // ==================== ALIAS METHODS (for backward compatibility) ====================

    /**
     * Alias for createEvent - for backward compatibility
     * @param event Event to add
     * @return Created event
     * @deprecated Use createEvent instead
     */
    @Deprecated
    default Event addEvent(Event event) {
        return createEvent(event);
    }

    /**
     * Alias for getAllEvents - for backward compatibility
     * @return List of all events
     * @deprecated Use getAllEvents instead
     */
    @Deprecated
    default List<Event> allEvents() {
        return getAllEvents();
    }
}