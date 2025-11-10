package com.chronus.app.mark.controller;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.MonthlyReport;
import com.chronus.app.mark.services.MarkService;
import com.chronus.app.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/v1/marks")
public class MarkController {

    @Autowired
    protected MarkService service;

    @GetMapping("/test")
    public String helloController() {
        return "hello";
    }

    @GetMapping
    public HttpResponse<List<Mark>> getMarksByMonthAndYear(@RequestParam int month, @RequestParam int year) {
        return service.getMarksByMonthAndYear(LocalDate.of(year,month,1));
    }

    @GetMapping("/report")
    public HttpResponse<MonthlyReport> getMonthlyReport(@RequestParam int month, @RequestParam int year) {
        return service.getMonthlyReport(LocalDate.of(year,month,1));
    }

    @PutMapping("/mark/{id}")
    public HttpResponse<Mark> editMark(@PathVariable long id, @RequestBody Mark mark){
        return service.editMark(id,mark);
    }

    @PostMapping("/mark")
    public HttpResponse<Mark> markRoute(@RequestBody Mark requestBody) {
        return service.addNewMark(requestBody);
    }

    @DeleteMapping("/mark/{id}")
    public HttpResponse<String> deleteMarkById(@PathVariable long id) { return service.deleteById(id); }
}
