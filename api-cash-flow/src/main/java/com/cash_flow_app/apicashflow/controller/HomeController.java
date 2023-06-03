package com.cash_flow_app.apicashflow.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/demo")
    public String showHome() {
        return "Demo Hello World!";
    }

    @PostMapping("/demo")
    public String postDemo() {
        return "Demo post Hello World!";
    }

    @GetMapping("/home")
    public String home() {
        return "Home Hello World 2!";
    }

    @PostMapping("/home")
    public String homePost() {
        return "Home post Hello World!";
    }

}
