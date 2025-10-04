package com.chronus.app.mark.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/marks")
public class markController {

    @GetMapping("/test")
    public String helloController() {
        return "hello";
    }
}
