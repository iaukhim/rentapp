package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.CountryDto;

public interface CountryService {


    CountryDto save(CountryDto countryDto);

    void update(CountryDto countryDto);

    void delete(CountryDto countryDto);

    CountryDto loadById(Long id);
}
