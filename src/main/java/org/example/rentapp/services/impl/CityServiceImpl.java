package org.example.rentapp.services.impl;

import jakarta.transaction.Transactional;
import org.example.rentapp.dao.interfaces.CityDao;
import org.example.rentapp.dtos.CityDto;
import org.example.rentapp.entities.City;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.services.interfaces.CityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CityServiceImpl implements CityService {

    private final ModelMapper mapper;

    private final CityDao cityDao;

    @Autowired
    public CityServiceImpl(ModelMapper mapper, CityDao cityDao) {
        this.mapper = mapper;
        this.cityDao = cityDao;
    }

    @Override
    public CityDto save(CityDto cityDto) {
        City cityEntity = mapper.map(cityDto, City.class);
        return mapper.map(cityDao.save(cityEntity), CityDto.class);
    }

    @Override
    public void update(CityDto cityDto) {
        City cityEntity = mapper.map(cityDto, City.class);
        cityDao.update(cityEntity);
    }

    @Override
    public void delete(CityDto cityDto) {
        City cityEntity = mapper.map(cityDto, City.class);
        cityDao.delete(cityEntity);
    }

    @Override
    public CityDto loadById(Long id) {
        Optional<City> city = cityDao.loadById(id);
        return mapper.map(city.orElseThrow(() -> new NoEntityFoundException(id, City.class)), CityDto.class);
    }
}
