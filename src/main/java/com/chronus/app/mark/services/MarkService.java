package com.chronus.app.mark.services;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MarkService {
    protected MarkRepository repository;
    protected MarkValidator validator;

    public MarkService() {
    }

    @Autowired
    public MarkService(MarkRepository repository, MarkValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    public HttpResponse<Mark> addNewMark(Mark mark) {
        if(mark.getMarkTime() == null) {
            return new HttpResponse<Mark>(400, "Mark time field must not be empty!", null);
        }

        if(mark.getUser() == null) {
            return new HttpResponse<Mark>(400, "User field must not be empty!", null);
        }

        if(validator.isDateTimeAlreadyMarked(mark)){
            return new HttpResponse<Mark>(400, "Already exists a mark to this date!", null);
        }

        if(!validator.isValidMarkInterval(mark)) {
            return new HttpResponse<Mark>(400, "So much time between Marks!", null);
        }

        repository.save(mark);
        return new HttpResponse<Mark>(201, "Mark added with success!", mark);
    }

}
