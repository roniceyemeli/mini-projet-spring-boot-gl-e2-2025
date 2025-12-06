package com.service.event.controller;


import com.service.event.entity.Event;
import com.service.event.service.IServiceEvent;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/event/")
public class EventRestController {
    private IServiceEvent serviceEvent;
    @PostMapping("add")
    public Event add(@RequestBody Event event) {
        return serviceEvent.addEvent(event);
    }
    @GetMapping("all")
    public List<Event> all(){
        return serviceEvent.allEvents();
    }
}
