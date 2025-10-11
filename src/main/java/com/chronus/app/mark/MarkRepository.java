package com.chronus.app.mark;

import com.chronus.app.MarkType;
import com.chronus.app.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> getMarkByMarkTimeAndMarkDate(LocalTime time, LocalDate date);
    boolean existsByTypeAndDate(MarkType type, LocalDate date);
    List<Mark> getMarksByMarkDate(LocalDate date);

    List<Mark> findByUserAndDate(User user, LocalDate date);
}
