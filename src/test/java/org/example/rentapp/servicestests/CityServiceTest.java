package org.example.rentapp.servicestests;

import org.example.rentapp.dao.interfaces.CityDao;
import org.example.rentapp.dtos.CityDto;
import org.example.rentapp.entities.City;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.providers.DataProvider;
import org.example.rentapp.services.impl.CityServiceImpl;
import org.example.rentapp.services.interfaces.CityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CityServiceTest {

    private ModelMapper modelMapper = new ModelMapper();

    private CityService cityService;

    private CityDao cityDao = mock(CityDao.class);

    private DataProvider dataProvider = new DataProvider();

    @BeforeEach
    public void before() {
        City city = dataProvider.getAddress().getCity();
        city.setId(1L);
        reset(cityDao);
        when(cityDao.loadById(1L)).thenReturn(Optional.of(city));
        when(cityDao.loadById(0L)).thenReturn(Optional.empty());
        when(cityDao.save(any(City.class))).thenReturn(city);
        when(cityDao.update(any(City.class))).thenAnswer(i -> i.getArguments()[0]);
        this.cityService = new CityServiceImpl(modelMapper, cityDao);
    }

    @Test
    public void loadById() {
        CityDto cityDto = cityService.loadById(1L);
        City city = dataProvider.getAddress().getCity();
        city.setId(1L);
        Assertions.assertEquals(cityDto, modelMapper.map(city, CityDto.class));
        verify(cityDao).loadById(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> cityService.loadById(0L));
    }

    @Test
    public void save() {
        CityDto cityDto = cityService.save(modelMapper.map(dataProvider.getAddress().getCity(), CityDto.class));
        City city = dataProvider.getAddress().getCity();
        city.setId(1L);
        Assertions.assertEquals(cityDto, modelMapper.map(city, CityDto.class));
        verify(cityDao).save(any(City.class));
    }

    @Test
    public void update() {
        City city = dataProvider.getAddress().getCity();
        CityDto cityDto = modelMapper.map(city, CityDto.class);
        cityService.update(cityDto);
        verify(cityDao).update(any(City.class));
    }

    @Test
    public void delete() {
        City city = dataProvider.getAddress().getCity();
        CityDto cityDto = modelMapper.map(city, CityDto.class);
        cityService.delete(cityDto);
        verify(cityDao).delete(any(City.class));
    }
}
