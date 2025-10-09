package com.chronus.app.mark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> getMarkByMarkTimeAndMarkDate(LocalTime time, LocalDate date);

    List<Mark> getMarksByMarkDate(LocalDate date);
}
