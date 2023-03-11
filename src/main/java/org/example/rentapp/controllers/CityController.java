package org.example.rentapp.controllers;

import org.example.rentapp.dtos.CityDto;
import org.example.rentapp.services.interfaces.CityService;
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
@RequestMapping("/api/cities")
public class CityController {

    private final String ROLE_LANDLORD = "ROLE_LANDLORD";

    private final String ROLE_RENTER = "ROLE_RENTER";

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    @PostMapping
    public CityDto save(@RequestBody CityDto cityDto) {
        return cityService.save(cityDto);
    }

    @Secured({ROLE_ADMIN})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Long id) {
        CityDto cityDto = cityService.loadById(id);
        cityService.delete(cityDto);
    }

    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody CityDto cityDto) {
        cityService.update(cityDto);
    }

    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    @GetMapping("/{id}")
    public CityDto loadById(@PathVariable(name = "id") Long id) {
        return cityService.loadById(id);
    }
}
