package com.service.event.controller;

import com.service.event.dto.event.EventDTO;
import com.service.event.entity.Event;
import com.service.event.enums.EventStatus;
import com.service.event.service.IServiceEvent;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
public class EventRestController {

    private final IServiceEvent serviceEvent;
    private final ModelMapper modelMapper;

    // CRUD Operations
    @PostMapping
    public ResponseEntity<Event.EventResponseDTO> createEvent(@Valid @RequestBody EventDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        Event createdEvent = serviceEvent.createEvent(event);
        Event.EventResponseDTO responseDTO = modelMapper.map(createdEvent, Event.EventResponseDTO.class);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event.EventResponseDTO> getEventById(@PathVariable UUID id) {
        Event event = serviceEvent.getEventById(id);
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Event.EventResponseDTO> getEventBySlug(@PathVariable String slug) {
        Event event = serviceEvent.getEventBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Event not found with slug: " + slug));
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<Event.EventResponseDTO>> getAllEvents(
            @RequestParam(required = false) EventStatus status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Boolean featured) {

        List<Event> events;

        if (status != null) {
            events = serviceEvent.getEventsByStatus(status);
        } else if (category != null) {
            events = serviceEvent.getEventsByCategory(category);
        } else if (featured != null && featured) {
            events = serviceEvent.getFeaturedEvents();
        } else {
            events = serviceEvent.getAllEvents();
        }

        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event.EventResponseDTO> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody com.service.event.dto.event.EventDTO eventDTO) {
        Event event = modelMapper.map(eventDTO, Event.class);
        Event updatedEvent = serviceEvent.updateEvent(id, event);
        Event.EventResponseDTO responseDTO = modelMapper.map(updatedEvent, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        serviceEvent.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // Featured Events
    @GetMapping("/featured")
    public ResponseEntity<List<Event.EventResponseDTO>> getFeaturedEvents() {
        List<Event> events = serviceEvent.getFeaturedEvents();
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @PatchMapping("/{id}/feature")
    public ResponseEntity<Event.EventResponseDTO> setFeaturedStatus(
            @PathVariable UUID id,
            @RequestParam boolean featured) {
        Event event = serviceEvent.setFeaturedStatus(id, featured);
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    // Event Status Management
    @PatchMapping("/{id}/status")
    public ResponseEntity<Event.EventResponseDTO> changeEventStatus(
            @PathVariable UUID id,
            @RequestParam EventStatus status) {
        Event event = serviceEvent.changeEventStatus(id, status);
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    // Event Filtering
    @GetMapping("/active")
    public ResponseEntity<List<Event.EventResponseDTO>> getActiveEvents() {
        List<Event> events = serviceEvent.getActiveEvents();
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Event.EventResponseDTO>> getUpcomingEvents() {
        List<Event> events = serviceEvent.getUpcomingEvents();
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/past")
    public ResponseEntity<List<Event.EventResponseDTO>> getPastEvents() {
        List<Event> events = serviceEvent.getPastEvents();
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/online")
    public ResponseEntity<List<Event.EventResponseDTO>> getOnlineEvents() {
        List<Event> events = serviceEvent.getOnlineEvents();
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @GetMapping("/physical")
    public ResponseEntity<List<Event.EventResponseDTO>> getPhysicalEvents() {
        List<Event> events = serviceEvent.getPhysicalEvents();
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // Search
    @GetMapping("/search")
    public ResponseEntity<List<Event.EventResponseDTO>> searchEvents(
            @RequestParam String keyword) {
        List<Event> events = serviceEvent.searchEvents(keyword);
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // Date Range Filter
    @GetMapping("/date-range")
    public ResponseEntity<List<Event.EventResponseDTO>> getEventsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Event> events = serviceEvent.getEventsByDateRange(startDate, endDate);
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // Organizer Events
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Event.EventResponseDTO>> getEventsByOrganizer(
            @PathVariable UUID organizerId,
            @RequestParam String organizerType) {
        List<Event> events = serviceEvent.getEventsByOrganizer(organizerId, organizerType);
        List<Event.EventResponseDTO> responseDTOs = events.stream()
                .map(event -> modelMapper.map(event, Event.EventResponseDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    // Event Capacity Management
    @PatchMapping("/{id}/capacity")
    public ResponseEntity<Event.EventResponseDTO> updateEventCapacity(
            @PathVariable UUID id,
            @RequestParam Integer maxParticipants) {
        Event event = serviceEvent.updateEventCapacity(id, maxParticipants);
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}/registration-deadline")
    public ResponseEntity<Event.EventResponseDTO> updateRegistrationDeadline(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline) {
        Event event = serviceEvent.updateEventRegistrationDeadline(id, deadline);
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    // Participant Management
    @PostMapping("/{id}/register")
    public ResponseEntity<Event.EventResponseDTO> registerForEvent(@PathVariable UUID id) {
        // This would typically be handled by a separate RegistrationService
        // For now, just increment participants
        Event event = serviceEvent.incrementParticipants(id);
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{id}/unregister")
    public ResponseEntity<Event.EventResponseDTO> unregisterFromEvent(@PathVariable UUID id) {
        Event event = serviceEvent.decrementParticipants(id);
        Event.EventResponseDTO responseDTO = modelMapper.map(event, Event.EventResponseDTO.class);
        return ResponseEntity.ok(responseDTO);
    }

    // Statistics
    @GetMapping("/stats/total")
    public ResponseEntity<Long> getTotalEventsCount() {
        Long count = serviceEvent.getTotalEventsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/active")
    public ResponseEntity<Long> getActiveEventsCount() {
        Long count = serviceEvent.getActiveEventsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/featured")
    public ResponseEntity<Long> getFeaturedEventsCount() {
        Long count = serviceEvent.getFeaturedEventsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/categories")
    public ResponseEntity<List<Object[]>> getEventsCountByCategory() {
        List<Object[]> stats = serviceEvent.getEventsCountByCategory();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/statuses")
    public ResponseEntity<List<Object[]>> getEventsCountByStatus() {
        List<Object[]> stats = serviceEvent.getEventsCountByStatus();
        return ResponseEntity.ok(stats);
    }

    // Validation
    @GetMapping("/{id}/can-register")
    public ResponseEntity<Boolean> canRegisterForEvent(@PathVariable UUID id) {
        boolean canRegister = serviceEvent.canRegisterForEvent(id);
        return ResponseEntity.ok(canRegister);
    }

    @GetMapping("/{id}/is-visible")
    public ResponseEntity<Boolean> isEventVisible(
            @PathVariable UUID id,
            @RequestParam UUID userId) {
        boolean isVisible = serviceEvent.isEventVisible(id, userId);
        return ResponseEntity.ok(isVisible);
    }
}