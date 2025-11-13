package com.example.demo.controller;

import com.example.demo.dto.EventDTO;
import com.example.demo.dto.EventRequest;
import com.example.demo.entity.Event;
import com.example.demo.entity.Category;
import com.example.demo.entity.Location;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.repository.CategoryRepository;
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
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @PostMapping
    public EventDTO createEvent(@RequestBody EventRequest req) {
        Event event = new Event();
        event.setTitle(req.getTitle());
        event.setDescription(req.getDescription());

        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        event.setStartTime(LocalDateTime.parse(req.getStartTime(), isoFormatter));
        event.setEndTime(LocalDateTime.parse(req.getEndTime(), isoFormatter));

        if (req.getCategoryId() != null) {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            event.setCategory(category);
        }

        if (req.getLocationId() != null) {
            Location location = locationRepository.findById(req.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            event.setLocation(location);
        }

        Event saved = eventRepository.save(event);
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
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return eventRepository.findById(id)
                .map(event -> {
                    event.setTitle(req.getTitle());
                    event.setDescription(req.getDescription());
                    event.setStartTime(LocalDateTime.parse(req.getStartTime(), isoFormatter));
                    event.setEndTime(LocalDateTime.parse(req.getEndTime(), isoFormatter));

                    if (req.getCategoryId() != null) {
                        Category category = categoryRepository.findById(req.getCategoryId())
                                .orElseThrow(() -> new RuntimeException("Category not found"));
                        event.setCategory(category);
                    }

                    if (req.getLocationId() != null) {
                        Location location = locationRepository.findById(req.getLocationId())
                                .orElseThrow(() -> new RuntimeException("Location not found"));
                        event.setLocation(location);
                    }

                    Event saved = eventRepository.save(event);
                    return new EventDTO(
                            saved.getEventId(),
                            saved.getTitle(),
                            saved.getDescription(),
                            saved.getStartTime(),
                            saved.getEndTime(),
                            saved.getCategory() != null ? saved.getCategory().getName() : null,
                            saved.getLocation() != null ? saved.getLocation().getName() : null
                    );
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }


    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer id) {
        if (!eventRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/filter")
    public List<EventDTO> filterEvents(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long locationId
    ) {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        return eventRepository.findAll().stream()
                .filter(event -> {
                    boolean matches = true;

                    if (start != null) {
                        LocalDateTime startTime = LocalDateTime.parse(start, isoFormatter);
                        matches &= !event.getStartTime().isBefore(startTime);
                    }
                    if (end != null) {
                        LocalDateTime endTime = LocalDateTime.parse(end, isoFormatter);
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