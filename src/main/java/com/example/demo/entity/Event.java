package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events",
        indexes = {
                @Index(name = "idx_event_start_time", columnList = "start_time"),
                @Index(name = "idx_event_category", columnList = "category_id"),
                @Index(name = "idx_event_location", columnList = "location_id")
        })
@Data                   // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Lombok: generates a no-args constructor
@AllArgsConstructor     // Lombok: generates a constructor with all fields
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer eventId;   // PK

    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_time", nullable = false)
    private java.time.LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private java.time.LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = true)
    private Location location;
}
