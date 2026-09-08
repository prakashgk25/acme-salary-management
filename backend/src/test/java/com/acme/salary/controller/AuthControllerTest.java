package com.acme.salary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthControllerTest {

    private final AuthController controller = new AuthController();

    @Test
    void acceptsHrAdminCredentials() {
        var response = controller.login(
                new AuthController.LoginRequest("HRadmin", "HRadmin"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().success());
    }

    @Test
    void rejectsInvalidCredentials() {
        var response = controller.login(
                new AuthController.LoginRequest("HRadmin", "wrong"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(false, response.getBody().success());
    }
}
