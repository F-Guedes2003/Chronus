package com.chronus.app.mark.services;

import com.chronus.app.mark.MarkRepository;
import org.springframework.stereotype.Service;

@Service
public class MarkValidator {
    private MarkRepository repository;

    public MarkValidator(MarkRepository repository) {
        this.repository = repository;
    }
}
