package com.chronus.app.mark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
    public Optional<Mark> getMarkByMarkTime(LocalDateTime dateTime);
}
