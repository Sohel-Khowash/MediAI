package com.sohel.healthcare.controller;


import com.sohel.healthcare.dto.UserProfileDto;
import com.sohel.healthcare.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @Autowired
    private UserService userService;


    @GetMapping ("/api/v1/health")
    public String Health(){
        return "Healthcare is running";
    }


    @PostMapping("/api/v1/register")
    public ResponseEntity registerUser(@Valid @RequestBody UserProfileDto userProfileDto){
        String result =  userService.registerUser(userProfileDto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/v1/profile")
    public UserDetails profile(Authentication authentication) {

        return (UserDetails) authentication.getPrincipal();

    }


}
