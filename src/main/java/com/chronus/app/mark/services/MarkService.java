package com.chronus.app.mark.services;

import com.chronus.app.MarkType;
import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.UserRepository;
import com.chronus.app.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MarkService {
    protected MarkRepository repository;
    protected MarkValidator validator;
    protected UserRepository userRepository;

    public MarkService() {
    }

    @Autowired
    public MarkService(MarkRepository repository, MarkValidator validator, UserRepository userRepository) {
        this.repository = repository;
        this.validator = validator;
        this.userRepository = userRepository;
    }

    public HttpResponse<List<Mark>> getMarksByMonthAndYear(LocalDate date) {
        if (date == null) return new HttpResponse<>(400, "Date must not be null", List.of());

        var year = date.getYear();
        var month = date.getMonth().getValue();
        List<Mark> marks = repository.findAllByYearAndMonth(year, month);

        if (marks.isEmpty()) return new HttpResponse<>(200, "There is no mark for this month", List.of());

        return new HttpResponse<>(200, "Marks fetched successfully", marks);
    }

    public HttpResponse<Mark> addNewMark(Mark mark) {

        if(mark.getUser() == null) {
            return new HttpResponse<Mark>(400, "User field must not be empty!", null);
        }

        var userId = mark.getUser().getId();

        if (userId == 0) {
            return new HttpResponse<Mark>(400, "user is Empty", null);
        }

        mark.setUser(userRepository.findUserById(userId).get());

        if(mark.getMarkTime() == null) {
            return new HttpResponse<Mark>(400, "Mark time field must not be empty!", null);
        }

        if(validator.isDateTimeAlreadyMarked(mark)){
            return new HttpResponse<Mark>(400, "Already exists a mark to this date!", null);
        }

        if(!validator.isValidMarkInterval(mark)) {
            return new HttpResponse<Mark>(400, "So much time between Marks!", null);
        }

        if(validator.isFutureMark(LocalDate.now(), mark)) {
            return new HttpResponse<Mark>(400, "Cannot insert marks to future dates!", null);
        }

        if(!validator.isValidMarkType(mark)) {
            return new HttpResponse<Mark>(400, "Invalid Mark Type!", null);
        }

        if(validator.isExitMarkWithoutEntry(mark)) {
            repository.save(mark);
            return new HttpResponse<Mark>(201, "Mark added with success, but there is needed to add an entry mark!", mark);
        }

        repository.save(mark);
        return new HttpResponse<Mark>(201, "Mark added with success!", mark);
    }

    public HttpResponse<Mark> editMark(Mark mark) {
        Mark markToEdit = repository.getMarkById(mark.getId());

        if (!repository.findMarkById(mark.getId()))
            return new HttpResponse<Mark>(404, "Inexistent mark for this user.", null);

        if(repository.existsByTypeAndMarkDate(mark.getType(),mark.getMarkDate()))
            return new HttpResponse<Mark>(400,"Already has the mark type for this day",null);

        markToEdit.setMarkTime(mark.getMarkTime());
        markToEdit.setType(mark.getType());
        repository.save(markToEdit);
        return new HttpResponse<Mark>(200,"Mark successfully edited",mark);
    }

    public Duration calculateWorkShift(List<Mark> workShift){
        LocalTime entry = workShift.getFirst().getMarkTime();
        LocalTime exit = workShift.getLast().getMarkTime();
        return Duration.between(entry,exit);
    }


    public HttpResponse<String> deleteMarkById(Integer id) {

        if(id == null || id <= 0 ) return new HttpResponse<String>(400, "Invalid id provided", null);

        if(!repository.findMarkById(id)) return new HttpResponse<String>(404, "Mark Not Found!", null);

        return new HttpResponse<String>(400, "Invalid id provided", null);
    }
}