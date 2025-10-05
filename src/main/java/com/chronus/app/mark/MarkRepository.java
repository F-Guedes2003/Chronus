package com.chronus.app.mark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> getMarkByMarkTime(LocalDateTime dateTime);

    List<Mark> findByMarkTimeBetween(LocalDateTime start, LocalDateTime end);

    default List<Mark> getMarkByDate(LocalDateTime dateTime) {
        var date = dateTime.toLocalDate();
        var start = LocalDateTime.of(date, LocalTime.of(0, 0, 0));
        var end = LocalDateTime.of(date, LocalTime.of(23, 59, 59));

        return findByMarkTimeBetween(start, end);
    };
}
