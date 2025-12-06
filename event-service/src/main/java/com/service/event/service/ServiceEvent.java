package com.service.event.service;


import com.service.event.entity.Event;
import com.service.event.repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceEvent implements IServiceEvent {
    EventRepository eventRepository;
    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> allEvents() {
        return eventRepository.findAll();
    }
}
