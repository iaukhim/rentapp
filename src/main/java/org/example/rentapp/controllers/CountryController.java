package org.example.rentapp.controllers;

import org.example.rentapp.dtos.CountryDto;
import org.example.rentapp.services.interfaces.CountryService;
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
@RequestMapping("/api/countries")
public class CountryController {

    private final String ROLE_LANDLORD = "ROLE_LANDLORD";

    private final String ROLE_RENTER = "ROLE_RENTER";

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public CountryDto save(@RequestBody CountryDto countryDto) {
        return countryService.save(countryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({ROLE_ADMIN})
    public void delete(@PathVariable(name = "id") Long id) {
        CountryDto countryDto = countryService.loadById(id);
        countryService.delete(countryDto);
    }

    @PutMapping("/{id}")
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody CountryDto country) {
        countryService.update(country);
    }

    @GetMapping("/{id}")
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public CountryDto loadById(@PathVariable(name = "id") Long id) {
        return countryService.loadById(id);
    }
}
