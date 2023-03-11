package org.example.rentapp.services.impl;

import jakarta.transaction.Transactional;
import org.example.rentapp.dao.interfaces.CountryDao;
import org.example.rentapp.dtos.CountryDto;
import org.example.rentapp.entities.Country;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.services.interfaces.CountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {
    private final ModelMapper mapper;

    private final CountryDao countryDao;

    @Autowired
    public CountryServiceImpl(ModelMapper mapper, CountryDao countryDao) {
        this.mapper = mapper;
        this.countryDao = countryDao;
    }

    @Override
    public CountryDto save(CountryDto countryDto) {
        Country country = mapper.map(countryDto, Country.class);
        Country savedCountry = countryDao.save(country);
        return mapper.map(savedCountry, CountryDto.class);
    }

    @Override
    public void update(CountryDto countryDto) {
        Country country = mapper.map(countryDto, Country.class);
        countryDao.update(country);
    }

    @Override
    public void delete(CountryDto countryDto) {
        Country country = mapper.map(countryDto, Country.class);
        countryDao.delete(country);
    }

    @Override
    public CountryDto loadById(Long id) {
        Optional<Country> country = countryDao.loadById(id);
        return mapper.map(country.orElseThrow(() -> new NoEntityFoundException(id, Country.class)), CountryDto.class);
    }
}
