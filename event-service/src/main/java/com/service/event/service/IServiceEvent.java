package com.service.event.service;


import com.service.event.entity.Event;

import java.util.List;

public interface IServiceEvent {
    public Event addEvent(Event event);
    public List<Event> allEvents();
}
