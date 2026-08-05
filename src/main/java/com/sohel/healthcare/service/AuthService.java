package com.sohel.healthcare.service;


import com.sohel.healthcare.dto.AuthenticationResponseDto;
import com.sohel.healthcare.dto.LoginRequestDto;
import com.sohel.healthcare.entity.User;
import com.sohel.healthcare.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserRepository userRepository;

    public AuthenticationResponseDto login(LoginRequestDto request) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()

                )

        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return new AuthenticationResponseDto(token);
    }
}
