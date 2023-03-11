package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.CityDto;

public interface CityService {

    CityDto save(CityDto cityDto);

    void update(CityDto cityDto);

    void delete(CityDto cityDto);

    CityDto loadById(Long id);
}
