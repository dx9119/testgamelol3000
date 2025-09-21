package com.example.alef.core.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/player")
    public String playerPage() {
        return "test19";
    }
}