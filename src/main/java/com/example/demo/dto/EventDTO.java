package com.example.demo.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDTO {
    private Integer eventId;
    private String title;
    private String description;
    private String categoryName;
    private String locationName;
    private String startTimeFormatted;
    private String endTimeFormatted;

    public EventDTO(Integer eventId, String title, String description,
                    LocalDateTime startTime, LocalDateTime endTime,
                    String categoryName, String locationName) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.categoryName = categoryName;
        this.locationName = locationName;
        this.startTimeFormatted = format(startTime);
        this.endTimeFormatted = format(endTime);
    }

    private String format(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ofPattern("MM-dd-yyyy 'at' HH:mm"));
    }

    // Getters only (no setters needed for read-only DTO)
    public Integer getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategoryName() { return categoryName; }
    public String getLocationName() { return locationName; }
    public String getStartTimeFormatted() { return startTimeFormatted; }
    public String getEndTimeFormatted() { return endTimeFormatted; }
}
