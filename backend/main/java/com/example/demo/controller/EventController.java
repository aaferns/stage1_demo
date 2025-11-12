package com.example.demo.controller;

import com.example.demo.dto.EventDTO;
import com.example.demo.entity.Event;
import com.example.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    // CREATE
    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

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
    public Event updateEvent(@PathVariable Integer id, @RequestBody Event updatedEvent) {
        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(updatedEvent.getTitle());
                    event.setDescription(updatedEvent.getDescription());
                    event.setStartTime(updatedEvent.getStartTime());
                    event.setEndTime(updatedEvent.getEndTime());
                    event.setCategory(updatedEvent.getCategory());
                    event.setLocation(updatedEvent.getLocation());
                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteEvent(@PathVariable Integer id) {
        eventRepository.deleteById(id);
        return "Event with id " + id + " deleted successfully.";
    }

    @GetMapping("/search")
    public List<EventDTO> searchEvents(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long locationId
    ) {
        return eventRepository.findAll().stream()
                .filter(event -> {
                    boolean matches = true;
                    if (start != null) {
                        java.time.LocalDateTime startTime = java.time.LocalDateTime.parse(start);
                        matches &= !event.getStartTime().isBefore(startTime);
                    }
                    if (end != null) {
                        java.time.LocalDateTime endTime = java.time.LocalDateTime.parse(end);
                        matches &= !event.getEndTime().isAfter(endTime);
                    }
                    if (categoryId != null && event.getCategory() != null) {
                        matches &= event.getCategory().getCategoryId().equals(categoryId);
                    }
                    if (locationId != null && event.getLocation() != null) {
                        matches &= event.getLocation().getLocationId().equals(locationId);
                    }
                    return matches;
                })
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