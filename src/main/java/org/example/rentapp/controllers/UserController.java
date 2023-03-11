package org.example.rentapp.controllers;

import org.example.rentapp.dtos.UserCreationDto;
import org.example.rentapp.dtos.UserDto;
import org.example.rentapp.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final String ROLE_LANDLORD = "ROLE_LANDLORD";

    private final String ROLE_RENTER = "ROLE_RENTER";

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public UserDto save(@RequestBody UserCreationDto userCreationDto) {
        return userService.save(userCreationDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({ROLE_ADMIN})
    public void delete(@PathVariable(name = "id") Long id) {
        UserDto userDto = userService.loadById(id);
        userService.delete(userDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public void update(@RequestBody UserDto userDto) {
        userService.update(userDto);
    }

    @GetMapping("/{id}")
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public UserDto loadById(@PathVariable(name = "id") Long id) {
        return userService.loadById(id);
    }
}
