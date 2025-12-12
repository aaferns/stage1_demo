package com.example.demo.controller;

import com.example.demo.dto.EventDTO;
import com.example.demo.dto.EventRequest;
import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    // CREATE
    @PostMapping
    public EventDTO createEvent(@RequestBody EventRequest req) {
        Event saved = eventService.createEvent(req);
        return new EventDTO(
                saved.getEventId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getCategory() != null ? saved.getCategory().getName() : null,
                saved.getLocation() != null ? saved.getLocation().getName() : null
        );
    }

    // READ (all)
    @GetMapping
    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> new EventDTO(
                        event.getEventId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getCategory() != null ? event.getCategory().getName() : null,
                        event.getLocation() != null ? event.getLocation().getName() : null
                ))
                .toList();
    }

    // READ (by id)
    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable Integer id) {
        return eventRepository.findById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public EventDTO updateEvent(@PathVariable Integer id, @RequestBody EventRequest req) {
        Event saved = eventService.updateEvent(id, req);
        return new EventDTO(
                saved.getEventId(),
                saved.getTitle(),
                saved.getDescription(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getCategory() != null ? saved.getCategory().getName() : null,
                saved.getLocation() != null ? saved.getLocation().getName() : null
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // FILTER (uses indexed query in EventRepository)
    @GetMapping("/filter")
    public List<EventDTO> filterEvents(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long locationId
    ) {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        LocalDateTime startTime = (start != null) ? LocalDateTime.parse(start, isoFormatter) : null;
        LocalDateTime endTime = (end != null) ? LocalDateTime.parse(end, isoFormatter) : null;

        return eventRepository.filterEvents(startTime, endTime, categoryId, locationId).stream()
                .map(event -> new EventDTO(
                        event.getEventId(),
                        event.getTitle(),
                        event.getDescription(),
                        event.getStartTime(),
                        event.getEndTime(),
                        event.getCategory() != null ? event.getCategory().getName() : null,
                        event.getLocation() != null ? event.getLocation().getName() : null
                ))
                .toList();
    }
}