package com.salon.userservice.controller;

import com.salon.userservice.payload.dto.LoginDTO;
import com.salon.userservice.payload.dto.SignupDTO;
import com.salon.userservice.response.AuthResponse;
import com.salon.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping
    public String HomeControllerHandler() {

        return "welcome to kam property booking system, user api";
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(
            @RequestBody SignupDTO req) throws Exception {

        System.out.println("signup dto "+req);
        AuthResponse response = authService.signup(req);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(
            @RequestBody LoginDTO req) throws Exception {

        AuthResponse response=authService.login(req.getEmail(), req.getPassword());

        return ResponseEntity.ok(
                response);
    }

    @GetMapping("/access-token/refresh-token/{refreshToken}")
    public ResponseEntity<AuthResponse> getAccessTokenHandler(
            @PathVariable String refreshToken) throws Exception {

        AuthResponse response = authService.getAccessTokenFromRefreshToken(refreshToken);

        return ResponseEntity.ok(
                response
        );
    }
}
