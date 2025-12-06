package com.service.event.controller;


import com.service.event.entity.EventSubscription;
import com.service.event.service.IServiceEventSubscription;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/event-subscription/")
public class EventSubscriptionRestController {
    private IServiceEventSubscription serviceEventSubscription;
    @PostMapping("add")
    public EventSubscription add(@RequestBody EventSubscription eventSubscription) {
        return serviceEventSubscription.addEventSubscription(eventSubscription);
    }
    @GetMapping("all")
    public List<EventSubscription> all(){
        return serviceEventSubscription.allEventsSubscriptions();
    }
}
