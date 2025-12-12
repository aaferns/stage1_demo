package com.example.demo.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import com.example.demo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("SELECT e FROM Event e " +
            "WHERE (:start IS NULL OR e.startTime >= :start) " +
            "AND (:end IS NULL OR e.endTime <= :end) " +
            "AND (:categoryId IS NULL OR e.category.categoryId = :categoryId) " +
            "AND (:locationId IS NULL OR e.location.locationId = :locationId)")
    List<Event> filterEvents(@Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             @Param("categoryId") Long categoryId,
                             @Param("locationId") Long locationId);
}
