package org.example.rentapp.controllers;


import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.services.interfaces.AddressService;
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
@RequestMapping("/api/addresses")
public class AddressController {

    private final String ROLE_LANDLORD = "ROLE_LANDLORD";

    private final String ROLE_RENTER = "ROLE_RENTER";

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public AddressDto save(@RequestBody AddressDto addressDto) {
        return addressService.save(addressDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({ROLE_ADMIN})
    public void delete(@PathVariable(name = "id") Long id) {
        AddressDto addressDto = addressService.loadById(id);
        addressService.delete(addressDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public void update(@RequestBody AddressDto addressDto) {
        addressService.update(addressDto);
    }

    @GetMapping("/{id}")
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public AddressDto loadById(@PathVariable(name = "id") Long id) {
        return addressService.loadById(id);
    }
}
