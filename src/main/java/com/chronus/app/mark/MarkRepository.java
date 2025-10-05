package com.chronus.app.mark;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
    public List<Mark> getMarkByMarkTime(LocalDateTime dateTime);
}
