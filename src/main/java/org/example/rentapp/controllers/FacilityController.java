package org.example.rentapp.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.GreaterThan;
import net.kaczmarzyk.spring.data.jpa.domain.LessThan;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Join;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.example.rentapp.additional.sorts.FacilitySort;
import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Address_;
import org.example.rentapp.entities.City_;
import org.example.rentapp.entities.Country_;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.entities.Facility_;
import org.example.rentapp.entities.Order_;
import org.example.rentapp.services.interfaces.FacilityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Set;

@Validated
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
    @Secured({ROLE_LANDLORD, ROLE_ADMIN})
    public FacilityDto save(@Valid @RequestBody FacilityDto facilityDto) {
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
    @Secured({ROLE_ADMIN})
    public void update(@Valid @RequestBody FacilityDto facilityDto) {
        facilityService.update(facilityDto);
    }

    @Secured({ROLE_ADMIN, ROLE_RENTER, ROLE_LANDLORD})
    @GetMapping("/{id}")
    public FacilityDto loadById(@PathVariable(name = "id") Long id) {
        return facilityService.loadById(id);
    }

    @Secured({ROLE_ADMIN, ROLE_RENTER, ROLE_LANDLORD})
    @GetMapping
    public Page<FacilityDto> loadAll(
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(50) Integer limit,
            @RequestParam(name = "sort", defaultValue = "ID_ASC") FacilitySort sort,

            @Join(path = "address", alias = "address")
            @Join(path = "address.city", alias = "city")
            @Join(path = "city.country", alias = "country")
            @Join(path = "orders", alias = "orders")

            @And({
                    @Spec(path = Facility_.NAME, params = "nameLike", spec = LikeIgnoreCase.class),
                    @Spec(path = Facility_.SPACE, params = {"spaceLessThan"}, spec = LessThan.class),
                    @Spec(path = Facility_.SPACE, params = {"spaceGreaterThan"}, spec = GreaterThan.class),
                    @Spec(path = Facility_.PRICE, params = {"priceLessThan"}, spec = LessThan.class),
                    @Spec(path = Facility_.PRICE, params = {"priceGreaterThan"}, spec = GreaterThan.class),
                    @Spec(path = "address." + Address_.DISTRICT, params = "district", spec = EqualIgnoreCase.class),
                    @Spec(path = "city." + City_.NAME, params = "city", spec = EqualIgnoreCase.class),
                    @Spec(path = "country." + Country_.CODE, params = "countryCode", spec = EqualIgnoreCase.class),
                    @Spec(path = "orders." + Order_.PLANNED_DATE, params = "noOrdersAfter", spec = LessThan.class)
            })

            Specification<Facility> spec) {
        return facilityService.loadAll(PageRequest.of(offset, limit, sort.getSortValue()), spec);
    }

    @Secured({ROLE_ADMIN, ROLE_LANDLORD})
    @GetMapping("/my-facilities")
    public Page<FacilityDto> loadAllCurrentUser(
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(50) Integer limit,
            @RequestParam(name = "sort", defaultValue = "ID_ASC") FacilitySort sort
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return facilityService.loadAllByOwnerEmail(PageRequest.of(offset, limit, sort.getSortValue()), email);
    }

    @Secured({ROLE_ADMIN, ROLE_LANDLORD})
    @GetMapping("/my-facilities/{id}")
    public FacilityDto loadCurrentUsersFacilityById(@PathVariable(name = "id") Long id) {
        FacilityDto facilityDto = facilityService.loadByIdEager(id);
        String ownerEmail = facilityDto.getOwner().getEmail();
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!ownerEmail.equals(currentUserEmail)) {
            throw new AccessDeniedException("You cannot load another user`s facilities");
        }
        return facilityDto;
    }

    @PutMapping("/my-facilities/")
    @Secured({ROLE_LANDLORD, ROLE_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUserFacility(@Valid @RequestBody FacilityDto facilityDto) {
        String ownerEmail = facilityService.loadByIdEager(facilityDto.getId()).getOwner().getEmail();
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!ownerEmail.equals(currentUserEmail)) {
            throw new AccessDeniedException("You cannot update another user`s facility");
        }
        facilityService.update(facilityDto);
    }

    @GetMapping("{id}/occupied-dates")
    @Secured({ROLE_ADMIN, ROLE_RENTER, ROLE_LANDLORD})
    public Set<LocalDate> loadOccupiedDates(@PathVariable(name = "id") Long id) {
        return facilityService.loadOccupiedDates(id);
    }
}
