package com.acme.salary.controller;

import com.acme.salary.dto.AnalyticsResponse;
import com.acme.salary.service.AnalyticsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://acme-salary-management-ui.onrender.com"
})
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService service;

    public AnalyticsController(AnalyticsService service) {
        this.service = service;
    }

    @GetMapping
    public AnalyticsResponse summary() {
        return service.summary();
    }
    @PostMapping("/refresh")
public void refresh() {
    service.refresh();
}
}