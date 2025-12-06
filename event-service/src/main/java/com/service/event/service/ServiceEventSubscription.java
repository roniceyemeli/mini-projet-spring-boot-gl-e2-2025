package com.service.event.service;


import com.service.event.entity.Event;
import com.service.event.entity.EventSubscription;
import com.service.event.repository.EventRepository;
import com.service.event.repository.EventSubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ServiceEventSubscription implements IServiceEventSubscription {
    EventSubscriptionRepository eventSubscriptionRepository;
    @Override
    public EventSubscription addEventSubscription(EventSubscription eventSubscription) {
        return eventSubscriptionRepository.save(eventSubscription);
    }

    @Override
    public List<EventSubscription> allEventsSubscriptions() {
        return eventSubscriptionRepository.findAll();
    }
}
