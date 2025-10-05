package com.chronus.app.mark.services;

import com.chronus.app.mark.MarkRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MarkValidator {
    private MarkRepository repository;

    public MarkValidator() {};

    public MarkValidator(MarkRepository repository) {
        this.repository = repository;
    }

    public boolean isDateTimeAlreadyMarked(LocalDateTime dateTime) {
        return false;
    }
}
