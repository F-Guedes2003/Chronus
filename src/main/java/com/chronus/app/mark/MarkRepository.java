package com.chronus.app.mark;

import com.chronus.app.MarkType;
import com.chronus.app.user.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Integer> {
    List<Mark> getMarkByMarkTimeAndMarkDate(LocalTime time, LocalDate date);
    boolean existsByTypeAndMarkDate(MarkType type, LocalDate date);
    List<Mark> getMarksByMarkDate(LocalDate date);
    Mark getMarkById(int id);
    boolean findMarkById(int id);
}
