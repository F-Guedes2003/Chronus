package com.chronus.app.mark;

import com.chronus.app.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MarkService {
    protected MarkRepository repository;

    public MarkService() {
    }

    @Autowired
    public MarkService(MarkRepository repository) {
        this.repository = repository;
    }

    public HttpResponse<Mark> addNewMark(Mark mark) {
        if(mark.getMarkTime() == null) {
            return new HttpResponse<Mark>(400, "Mark time field must not be empty!", null);
        }

        if(mark.getUser() == null) {
            return new HttpResponse<Mark>(400, "User field must not be empty!", null);
        }

        repository.save(mark);
        return new HttpResponse<Mark>(201, "Mark added with success!", mark);
    }

}
