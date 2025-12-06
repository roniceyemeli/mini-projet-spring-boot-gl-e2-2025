package com.service.event.service;


import com.service.event.entity.Event;
import com.service.event.entity.EventSubscription;

import java.util.List;

public interface IServiceEventSubscription {
    public EventSubscription addEventSubscription(EventSubscription eventSubscription);
    public List<EventSubscription> allEventsSubscriptions();
}
