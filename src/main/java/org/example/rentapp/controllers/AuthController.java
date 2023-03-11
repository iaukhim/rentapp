package org.example.rentapp.controllers;


import lombok.RequiredArgsConstructor;
import org.example.rentapp.dtos.UserCreationDto;
import org.example.rentapp.dtos.UserDto;
import org.example.rentapp.security.JwtService;
import org.example.rentapp.services.interfaces.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtService jwtService;

    @PostMapping("/login")
    public String authenticate(@RequestBody UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        UserDto loadedUser = userService.loadByEmail(userDto.getEmail());
        return jwtService.createToken(loadedUser.getEmail());
    }

    @PostMapping("/register")
    public String register(@RequestBody UserCreationDto userCreationDto) {
        UserDto savedUser = userService.save(userCreationDto);
        return jwtService.createToken(savedUser.getEmail());
    }
}
