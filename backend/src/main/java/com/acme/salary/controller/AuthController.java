package com.acme.salary.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
        "http://localhost:4200",
        "https://acme-salary-management-ui.onrender.com"
})
public class AuthController {

    private static final String USER_ID = "HRadmin";
    private static final String PASSWORD = "HRadmin";

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        if (USER_ID.equals(request.username()) && PASSWORD.equals(request.password())) {
            return ResponseEntity.ok(new LoginResponse(true, "Login successful"));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse(false, "Invalid user ID or password"));
    }

    public record LoginRequest(String username, String password) {
    }

    public record LoginResponse(boolean success, String message) {
    }
}
