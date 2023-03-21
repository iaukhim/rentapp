package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.FacilityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface FacilityService {

    FacilityDto save(FacilityDto facilityDto);

    void update(FacilityDto facilityDto);

    void delete(FacilityDto facilityDto);

    FacilityDto loadById(Long id);

    FacilityDto findLargestFacility();

    FacilityDto loadByIdEager(Long id);

    Page<FacilityDto> loadAll(PageRequest pageRequest);
}
