package com.acme.salary.controller; 
import org.springframework.web.bind.annotation.CrossOrigin;
import com.acme.salary.dto.AnalyticsResponse; 
import com.acme.salary.service.AnalyticsService; 
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {private final AnalyticsService s; public AnalyticsController(AnalyticsService s){this.s=s;} @GetMapping("/summary") public AnalyticsResponse summary(){return s.summary();}}
