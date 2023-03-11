package org.example.rentapp.controllers;

import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.services.interfaces.FacilityService;
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
@RequestMapping("/api/facilities")
public class FacilityController {

    private final String ROLE_LANDLORD = "ROLE_LANDLORD";

    private final String ROLE_RENTER = "ROLE_RENTER";

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @PostMapping
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public FacilityDto save(@RequestBody FacilityDto facilityDto) {
        return facilityService.save(facilityDto);
    }

    @DeleteMapping("/{id}")
    @Secured({ROLE_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") Long id) {
        FacilityDto facilityDto = facilityService.loadById(id);
        facilityService.delete(facilityDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public void update(@RequestBody FacilityDto facilityDto) {
        facilityService.update(facilityDto);
    }

    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    @GetMapping("/{id}")
    public FacilityDto loadById(@PathVariable(name = "id") Long id) {
        return facilityService.loadById(id);
    }
}
