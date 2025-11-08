package com.chronus.app.mark;

import com.chronus.app.MarkType;
import com.chronus.app.user.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> getMarkByMarkTimeAndMarkDate(LocalTime time, LocalDate date);

    boolean existsByTypeAndMarkDate(MarkType type, LocalDate date);

    List<Mark> getMarksByMarkDate(LocalDate date);

    Optional<Mark> findById(long id);

    boolean existsById(long id);

    @Query("SELECT m FROM Mark m WHERE YEAR(m.markDate) = :year AND MONTH(m.markDate) = :month")
    List<Mark> findAllByYearAndMonth(@Param("year") int year, @Param("month") int month);

    @Transactional
    @Modifying
    @Query("UPDATE Mark m set m.markDate = :date, m.markTime = :time, m.type = :type WHERE m.id = :id")
    int update(@Param("date") LocalDate date, @Param("time") LocalTime time, @Param("type") MarkType type, @Param("id") long id);
}
