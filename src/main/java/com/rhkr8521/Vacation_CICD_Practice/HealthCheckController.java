package com.rhkr8521.Vacation_CICD_Practice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("health")
    public String healthCheck() {
        return "OK";
    }
}