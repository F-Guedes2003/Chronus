package com.chronus.app.mark.services;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public int findMarkIndex(List<Mark> dayMarks, Mark mark) {
        int low = 0;
        int high = dayMarks.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            LocalTime midTime = dayMarks.get(mid).getMarkTime();

            if (mark.getMarkTime().isBefore(midTime)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return low;
    }

    public boolean isDateTimeAlreadyMarked(Mark mark) {

        return !repository.getMarkByMarkTimeAndMarkDate(mark.getMarkTime(), mark.getMarkDate()).isEmpty();
    }

    public boolean isValidMarkInterval(Mark mark) {
        List<Mark> dayMarks = repository.getMarksByMarkDate(mark.getMarkDate());

        if (dayMarks.isEmpty()) return true;

        Mark lastMark = dayMarks.stream()
                .sorted(Comparator.comparing(Mark::getMarkTime))
                .toList()
                .getLast();

        return Duration.between(lastMark.getMarkTime(), mark.getMarkTime()).toHours() < 12;
    }

    public boolean isValidMarkType(Mark mark) {
        var dayMarks = repository.getMarksByMarkDate(mark.getMarkDate());
        int markIndex = findMarkIndex(dayMarks, mark);

        if(markIndex == dayMarks.size()) return dayMarks.getLast().getType() != mark.getType();

        return (!dayMarks.get(markIndex).getValid() || dayMarks.get(markIndex).getType() != mark.getType())
                &&
                (!dayMarks.get(markIndex - 1).getValid() || dayMarks.get(markIndex - 1).getType() != mark.getType());
    }
}
