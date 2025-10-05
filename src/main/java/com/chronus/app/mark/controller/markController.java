package com.chronus.app.mark.controller;

import com.chronus.app.mark.Mark;
import com.chronus.app.mark.services.MarkService;
import com.chronus.app.utils.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/marks")
public class markController {

    @Autowired
    protected MarkService service;

    @GetMapping("/test")
    public String helloController() {
        return "hello";
    }

    @PostMapping("/mark")
    public HttpResponse<Mark> markRoute(@RequestBody Mark requestBody) {
        return service.addNewMark(requestBody);
    }
}
