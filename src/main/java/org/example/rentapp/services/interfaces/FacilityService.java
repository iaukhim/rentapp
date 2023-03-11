package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.FacilityDto;

public interface FacilityService {

    FacilityDto save(FacilityDto facilityDto);

    void update(FacilityDto facilityDto);

    void delete(FacilityDto facilityDto);

    FacilityDto loadById(Long id);

    FacilityDto findLargestFacility();

    FacilityDto loadByIdEager(Long id);
}
