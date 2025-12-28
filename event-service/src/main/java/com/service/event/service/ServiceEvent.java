package com.service.event.service;

import com.service.event.dto.event.UpdateEventDTO;
import com.service.event.entity.Event;
import com.service.event.enums.EventStatus;
import com.service.event.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class ServiceEvent implements IServiceEvent {

    private final EventRepository eventRepository;

    @Override
    public Event createEvent(Event event) {
        log.info("Creating new event: {}", event.getTitle());

        // Validate slug uniqueness
        if (event.getSlug() != null && eventRepository.existsBySlug(event.getSlug())) {
            throw new RuntimeException("Event with slug '" + event.getSlug() + "' already exists");
        }

        // Set default values if not provided
        if (event.getCurrentParticipants() == null) {
            event.setCurrentParticipants(0);
        }

        if (event.getStatus() == null) {
            event.setStatus(EventStatus.DRAFT);
        }

        if (event.getIsFeatured() == null) {
            event.setIsFeatured(false);
        }

        Event savedEvent = eventRepository.save(event);
        log.info("Event created successfully with ID: {}", savedEvent.getId());

        return savedEvent;
    }

    @Override
    public Event updateEvent(UUID id, Event event) {
        log.info("Updating event with ID: {}", id);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // Check if slug changed and is still unique
        if (event.getSlug() != null &&
                !event.getSlug().equals(existingEvent.getSlug()) &&
                eventRepository.existsBySlug(event.getSlug())) {
            throw new RuntimeException("Event with slug '" + event.getSlug() + "' already exists");
        }

        // Update fields
        existingEvent.setTitle(event.getTitle());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setSlug(event.getSlug());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setVenueDetails(event.getVenueDetails());
        existingEvent.setStartDate(event.getStartDate());
        existingEvent.setStartTime(event.getStartTime());
        existingEvent.setEndDate(event.getEndDate());
        existingEvent.setEndTime(event.getEndTime());
        existingEvent.setMaxParticipants(event.getMaxParticipants());
        existingEvent.setRegistrationFee(event.getRegistrationFee());
        existingEvent.setContactEmail(event.getContactEmail());
        existingEvent.setContactPhone(event.getContactPhone());
        existingEvent.setImageUrl(event.getImageUrl());
        existingEvent.setIsOnline(event.getIsOnline());
        existingEvent.setOnlineLink(event.getOnlineLink());
        existingEvent.setRequiresApproval(event.getRequiresApproval());
        existingEvent.setRegistrationDeadline(event.getRegistrationDeadline());
        existingEvent.setCategory(event.getCategory());
        existingEvent.setTags(event.getTags());
        existingEvent.setVisibility(event.getVisibility());
        existingEvent.setUpdatedBy(event.getUpdatedBy());

        Event updatedEvent = eventRepository.save(existingEvent);
        log.info("Event updated successfully: {}", updatedEvent.getTitle());

        return updatedEvent;
    }

    @Override
    public Event updateEventFromDTO(UUID id, UpdateEventDTO updateDTO) {
        log.info("Updating event with ID: {} from DTO", id);

        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // Check if slug changed and is still unique
        if (updateDTO.getSlug() != null &&
                !updateDTO.getSlug().equals(existingEvent.getSlug()) &&
                eventRepository.existsBySlug(updateDTO.getSlug())) {
            throw new RuntimeException("Event with slug '" + updateDTO.getSlug() + "' already exists");
        }

        // Update only provided fields (partial update)
        if (updateDTO.getTitle() != null) existingEvent.setTitle(updateDTO.getTitle());
        if (updateDTO.getDescription() != null) existingEvent.setDescription(updateDTO.getDescription());
        if (updateDTO.getSlug() != null) existingEvent.setSlug(updateDTO.getSlug());
        if (updateDTO.getLocation() != null) existingEvent.setLocation(updateDTO.getLocation());
        if (updateDTO.getVenueDetails() != null) existingEvent.setVenueDetails(updateDTO.getVenueDetails());
        if (updateDTO.getStartDate() != null) existingEvent.setStartDate(updateDTO.getStartDate());
        if (updateDTO.getStartTime() != null) existingEvent.setStartTime(updateDTO.getStartTime());
        if (updateDTO.getEndDate() != null) existingEvent.setEndDate(updateDTO.getEndDate());
        if (updateDTO.getEndTime() != null) existingEvent.setEndTime(updateDTO.getEndTime());
        if (updateDTO.getStatus() != null) existingEvent.setStatus(updateDTO.getStatus());
        if (updateDTO.getMaxParticipants() != null) existingEvent.setMaxParticipants(updateDTO.getMaxParticipants());
        if (updateDTO.getRegistrationFee() != null) existingEvent.setRegistrationFee(updateDTO.getRegistrationFee());
        if (updateDTO.getOrganizerId() != null) existingEvent.setOrganizerId(updateDTO.getOrganizerId());
        if (updateDTO.getOrganizerType() != null) existingEvent.setOrganizerType(updateDTO.getOrganizerType());
        if (updateDTO.getContactEmail() != null) existingEvent.setContactEmail(updateDTO.getContactEmail());
        if (updateDTO.getContactPhone() != null) existingEvent.setContactPhone(updateDTO.getContactPhone());
        if (updateDTO.getImageUrl() != null) existingEvent.setImageUrl(updateDTO.getImageUrl());
        if (updateDTO.getIsOnline() != null) existingEvent.setIsOnline(updateDTO.getIsOnline());
        if (updateDTO.getOnlineLink() != null) existingEvent.setOnlineLink(updateDTO.getOnlineLink());
        if (updateDTO.getRequiresApproval() != null) existingEvent.setRequiresApproval(updateDTO.getRequiresApproval());
        if (updateDTO.getRegistrationDeadline() != null) existingEvent.setRegistrationDeadline(updateDTO.getRegistrationDeadline());
        if (updateDTO.getCategory() != null) existingEvent.setCategory(updateDTO.getCategory());
        if (updateDTO.getTags() != null) existingEvent.setTags(updateDTO.getTags());
        if (updateDTO.getIsFeatured() != null) existingEvent.setIsFeatured(updateDTO.getIsFeatured());
        if (updateDTO.getVisibility() != null) existingEvent.setVisibility(updateDTO.getVisibility());
        if (updateDTO.getUpdatedBy() != null) existingEvent.setUpdatedBy(updateDTO.getUpdatedBy());

        Event updatedEvent = eventRepository.save(existingEvent);
        log.info("Event updated successfully from DTO: {}", updatedEvent.getTitle());

        return updatedEvent;
    }

    @Override
    public void deleteEvent(UUID id) {
        log.info("Deleting event with ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));

        // Check if event has participants
        if (event.getCurrentParticipants() > 0) {
            throw new RuntimeException("Cannot delete event that has participants. Cancel it instead.");
        }

        eventRepository.delete(event);
        log.info("Event deleted successfully: {}", event.getTitle());
    }

    @Override
    public Event getEventById(UUID id) {
        log.debug("Fetching event by ID: {}", id);

        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    @Override
    public Optional<Event> getEventBySlug(String slug) {
        log.debug("Fetching event by slug: {}", slug);

        return eventRepository.findBySlug(slug);
    }

    @Override
    public List<Event> getAllEvents() {
        log.debug("Fetching all events");

        return eventRepository.findAll();
    }

    @Override
    public List<Event> getFeaturedEvents() {
        log.debug("Fetching featured events");

        return eventRepository.findByIsFeaturedTrueAndStatus(EventStatus.ACTIVE);
    }

    @Override
    public List<Event> getFeaturedEventsByCategory(String category) {
        log.debug("Fetching featured events by category: {}", category);

        return eventRepository.findByIsFeaturedTrueAndStatusAndCategory(
                EventStatus.ACTIVE,
                category
        );
    }

    @Override
    public Event setFeaturedStatus(UUID eventId, boolean isFeatured) {
        log.info("Setting featured status for event {} to {}", eventId, isFeatured);

        Event event = getEventById(eventId);

        // Only active events can be featured
        if (isFeatured && event.getStatus() != EventStatus.ACTIVE) {
            throw new RuntimeException("Only active events can be featured");
        }

        event.setIsFeatured(isFeatured);
        Event updatedEvent = eventRepository.save(event);

        log.info("Event {} featured status set to {}", eventId, isFeatured);

        return updatedEvent;
    }

    @Override
    public List<Event> getActiveEvents() {
        log.debug("Fetching active events");

        return eventRepository.findByStatus(EventStatus.ACTIVE);
    }

    @Override
    public List<Event> getUpcomingEvents() {
        log.debug("Fetching upcoming events");

        LocalDate today = LocalDate.now();
        return eventRepository.findUpcomingEvents(today);
    }

    @Override
    public List<Event> getPastEvents() {
        log.debug("Fetching past events");

        LocalDate today = LocalDate.now();
        return eventRepository.findPastEvents(today);
    }

    @Override
    public List<Event> getEventsByStatus(EventStatus status) {
        log.debug("Fetching events by status: {}", status);

        return eventRepository.findByStatus(status);
    }

    @Override
    public List<Event> getEventsByOrganizer(UUID organizerId, String organizerType) {
        log.debug("Fetching events by organizer: {} - {}", organizerType, organizerId);

        return eventRepository.findByOrganizerIdAndOrganizerType(organizerId, organizerType);
    }

    @Override
    public List<Event> getEventsByCategory(String category) {
        log.debug("Fetching events by category: {}", category);

        return eventRepository.findByCategory(category);
    }

    @Override
    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Fetching events by date range: {} to {}", startDate, endDate);

        return eventRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<Event> searchEvents(String keyword) {
        log.debug("Searching events with keyword: {}", keyword);

        return eventRepository.searchEvents(keyword);
    }

    @Override
    public List<Event> getOnlineEvents() {
        log.debug("Fetching online events");

        return eventRepository.findByIsOnlineTrueAndStatus(EventStatus.ACTIVE);
    }

    @Override
    public List<Event> getPhysicalEvents() {
        log.debug("Fetching physical events");

        return eventRepository.findByIsOnlineFalseAndStatus(EventStatus.ACTIVE);
    }

    @Override
    public Event changeEventStatus(UUID eventId, EventStatus status) {
        log.info("Changing event {} status to {}", eventId, status);

        Event event = getEventById(eventId);
        event.setStatus(status);

        // If event is being deactivated, remove from featured
        if (status != EventStatus.ACTIVE && event.getIsFeatured()) {
            event.setIsFeatured(false);
        }

        Event updatedEvent = eventRepository.save(event);
        log.info("Event {} status changed to {}", eventId, status);

        return updatedEvent;
    }

    @Override
    public Event updateEventCapacity(UUID eventId, Integer maxParticipants) {
        log.info("Updating event {} capacity to {}", eventId, maxParticipants);

        Event event = getEventById(eventId);

        // Validate that new capacity is not less than current participants
        if (maxParticipants != null && maxParticipants < event.getCurrentParticipants()) {
            throw new RuntimeException("New capacity cannot be less than current participants");
        }

        event.setMaxParticipants(maxParticipants);
        Event updatedEvent = eventRepository.save(event);

        log.info("Event {} capacity updated to {}", eventId, maxParticipants);

        return updatedEvent;
    }

    @Override
    public Event updateEventRegistrationDeadline(UUID eventId, LocalDateTime deadline) {
        log.info("Updating event {} registration deadline to {}", eventId, deadline);

        Event event = getEventById(eventId);
        event.setRegistrationDeadline(deadline);

        Event updatedEvent = eventRepository.save(event);

        log.info("Event {} registration deadline updated", eventId);

        return updatedEvent;
    }

    @Override
    public Event incrementParticipants(UUID eventId) {
        log.debug("Incrementing participants for event: {}", eventId);

        Event event = getEventById(eventId);

        // Check if event is full
        if (event.isFull()) {
            throw new RuntimeException("Event is already full");
        }

        event.incrementParticipants();
        Event updatedEvent = eventRepository.save(event);

        log.debug("Event {} participants incremented to {}", eventId, updatedEvent.getCurrentParticipants());

        return updatedEvent;
    }

    @Override
    public Event decrementParticipants(UUID eventId) {
        log.debug("Decrementing participants for event: {}", eventId);

        Event event = getEventById(eventId);
        event.decrementParticipants();

        Event updatedEvent = eventRepository.save(event);

        log.debug("Event {} participants decremented to {}", eventId, updatedEvent.getCurrentParticipants());

        return updatedEvent;
    }

    @Override
    public boolean canRegisterForEvent(UUID eventId) {
        Event event = getEventById(eventId);
        return event.canRegister();
    }

    @Override
    public Long getTotalEventsCount() {
        return eventRepository.count();
    }

    @Override
    public Long getActiveEventsCount() {
        return eventRepository.countByStatus(EventStatus.ACTIVE);
    }

    @Override
    public Long getFeaturedEventsCount() {
        return eventRepository.countByIsFeaturedTrueAndStatus(EventStatus.ACTIVE);
    }

    @Override
    public List<Object[]> getEventsCountByCategory() {
        return eventRepository.countEventsByCategory();
    }

    @Override
    public List<Object[]> getEventsCountByStatus() {
        return eventRepository.countEventsByStatus();
    }

    @Override
    public List<Event> getPublicEvents() {
        return eventRepository.findByVisibilityAndStatus("PUBLIC", EventStatus.ACTIVE);
    }

    @Override
    public List<Event> getEventsByVisibility(String visibility) {
        return eventRepository.findByVisibilityAndStatus(visibility, EventStatus.ACTIVE);
    }

    @Override
    public boolean isEventVisible(UUID eventId, UUID userId) {
        Event event = getEventById(eventId);

        // Public events are visible to everyone
        if ("PUBLIC".equals(event.getVisibility())) {
            return true;
        }

        // Private events - implement your logic here
        // For example, check if user is invited, is organizer, etc.
        // This is a placeholder implementation
        return event.getOrganizerId().equals(userId);
    }

    @Override
    public boolean eventExists(UUID eventId) {
        return eventRepository.existsById(eventId);
    }

    @Override
    public boolean slugExists(String slug) {
        return eventRepository.existsBySlug(slug);
    }

    @Override
    public void validateEventCapacity(UUID eventId) {
        Event event = getEventById(eventId);

        if (event.isFull()) {
            throw new RuntimeException("Event is full");
        }
    }
}