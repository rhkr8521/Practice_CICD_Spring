package com.rhkr8521.Vacation_CICD_Practice.api.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class mainController {

    @GetMapping("/hello")
    public ResponseEntity hello() {
        return ResponseEntity.ok("CICD 최종 테스트");
    }
}
