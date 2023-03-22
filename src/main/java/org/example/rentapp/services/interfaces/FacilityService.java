package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Set;

public interface FacilityService {

    FacilityDto save(FacilityDto facilityDto);

    void update(FacilityDto facilityDto);

    void delete(FacilityDto facilityDto);

    FacilityDto loadById(Long id);

    FacilityDto findLargestFacility();

    FacilityDto loadByIdEager(Long id);

    Page<FacilityDto> loadAll(PageRequest pageRequest);

    Page<FacilityDto> loadAll(PageRequest pageRequest, Specification<Facility> spec);

    Page<FacilityDto> loadAllByOwnerEmail(PageRequest pageRequest, String email);

    Set<LocalDate> loadOccupiedDates(Long id);
}
