package com.sohel.healthcare.controller;


import com.sohel.healthcare.dto.AuthenticationResponseDto;
import com.sohel.healthcare.dto.LoginRequestDto;
import com.sohel.healthcare.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@EnableMethodSecurity
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthenticationResponseDto login(
            @RequestBody LoginRequestDto request){

        return authService.login(request);

    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {

        return "Admin endpoint";

    }
}
