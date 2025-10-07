package com.chronus.app.mark.services;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MarkValidator {
    protected MarkRepository repository;

    public MarkValidator() {};

    public MarkValidator(MarkRepository repository) {
        this.repository = repository;
    }

    public boolean isDateTimeAlreadyMarked(LocalDateTime dateTime) {
        return !repository.getMarkByMarkTime(dateTime).isEmpty();
    }

    public boolean isValidMarkInterval(Mark mark) {
        List<Mark> dayMarks = repository.getMarkByDate(mark);

        Mark lastMark = dayMarks.stream()
                .sorted(Comparator.comparing(Mark::getMarkTime))
                .toList()
                .getLast();

        return Duration.between(lastMark.getMarkTime(), mark.getMarkTime()).toHours() < 12;
    }
}
