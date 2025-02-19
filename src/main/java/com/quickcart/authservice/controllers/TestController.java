package com.quickcart.authservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Controller", description = "A simple controller for testing")
public class TestController {

    @GetMapping
    @Operation(summary = "Get a test message")
    public String test() {
        return "Hello, Swagger!";
    }
}
