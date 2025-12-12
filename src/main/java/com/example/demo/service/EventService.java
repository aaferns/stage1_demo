package com.example.demo.service;

import com.example.demo.dto.EventRequest;
import com.example.demo.entity.Category;
import com.example.demo.entity.Event;
import com.example.demo.entity.Location;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Isolation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Event createEvent(EventRequest req) {
        DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        Event event = new Event();
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

        return eventRepository.save(event);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Event updateEvent(Integer id, EventRequest req) {
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

                    return eventRepository.save(event);
                })
                .orElseThrow(() -> new RuntimeException("Event not found with id " + id));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteEvent(Integer id) {
        if (!eventRepository.existsById(id)) {
            throw new RuntimeException("Event not found with id " + id);
        }
        eventRepository.deleteById(id);
    }
}
