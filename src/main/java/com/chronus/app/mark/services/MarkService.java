package com.chronus.app.mark.services;

import com.chronus.app.MarkType;
import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MarkRepository;
import com.chronus.app.user.User;
import com.chronus.app.user.UserRepository;
import com.chronus.app.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.round;

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

        if(userRepository.findUserById(mark.getUser().getId()).isEmpty()){
            return new HttpResponse<Mark>(404, "User is not found!", null);

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

    public HttpResponse<Mark> editMark(long id, Mark editMark) {

        Optional<Mark> markOpt = repository.findById(id);
        Mark mark = markOpt.get();

        if(userRepository.findUserById(mark.getUser().getId()).isEmpty()){
            return new HttpResponse<Mark>(404, "User is not found!", null);

        }

        if(repository.getMarkByMarkTimeAndMarkDate(editMark.getMarkTime(),editMark.getMarkDate()) != null){
            return new HttpResponse<Mark>(400,"Already have a mark for this time and date",null);
        }

        if (!repository.existsById(mark.getId()))
            return new HttpResponse<Mark>(404, "Inexistent mark for this user.", null);


        repository.update(editMark.getMarkDate(),editMark.getMarkTime(),editMark.getType(), id);
        return new HttpResponse<Mark>(200,"Mark successfully edited",mark);
    }

    public Duration calculateWorkShift(List<Mark> workShift){
        LocalTime entry = workShift.getFirst().getMarkTime();
        LocalTime exit = workShift.getLast().getMarkTime();
        return Duration.between(entry,exit);
    }

    public HttpResponse<String> deleteById(long id) {
        return new HttpResponse<>(204, "", null);
    }

    public double calculateSalary(User user, LocalDate date) {
        List<Mark> monthlyMarks = repository.findAllByYearAndMonth(date.getYear(),date.getMonthValue());
        Duration monthlyWorkShift = Duration.ZERO;
        int workingDays = 22;
        int workingHours = 10;

        for(int i = 0;i < monthlyMarks.size();i++){
            LocalDate day = monthlyMarks.get(i).getMarkDate();

            if(i+1 > monthlyMarks.size() - 1) break;

            if(monthlyMarks.get(i+1).getMarkDate().equals(day)){
                monthlyWorkShift = calculateWorkShift(List.of(monthlyMarks.get(i),monthlyMarks.get(i+1)));
            }
        }

        double dailySalary = user.getSalary() / workingDays;
        double hourSalary = dailySalary / workingHours;
        double result = new BigDecimal(hourSalary * monthlyWorkShift.toHours()).setScale(2, RoundingMode.FLOOR).doubleValue();

        return result;
    }
}