package com.chronus.app.mark;

import com.chronus.app.MarkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Integer> {

    List<Mark> getMarkByMarkTimeAndMarkDate(LocalTime markTime, LocalDate markDate);

    boolean existsByTypeAndMarkDate(MarkType type, LocalDate markDate);

    List<Mark> getMarksByMarkDate(LocalDate markDate);

    boolean findMarkById(int id);

    Mark getMarkById(int id);
}

