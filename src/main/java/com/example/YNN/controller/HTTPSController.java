package com.example.YNN.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HTTPSController {

    @GetMapping("/https")
    public String handleHttpsRequest() {
        return "success";
    }
}
