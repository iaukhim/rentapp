package org.example.rentapp.servicestests;

import org.example.rentapp.dao.interfaces.CountryDao;
import org.example.rentapp.dtos.CountryDto;
import org.example.rentapp.entities.Country;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.providers.DataProvider;
import org.example.rentapp.services.impl.CountryServiceImpl;
import org.example.rentapp.services.interfaces.CountryService;
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

public class CountryServiceTest {

    private ModelMapper modelMapper = new ModelMapper();

    private CountryService countryService;

    private CountryDao countryDao = mock(CountryDao.class);

    private DataProvider dataProvider = new DataProvider();

    @BeforeEach
    public void before() {
        Country country = dataProvider.getAddress().getCity().getCountry();
        country.setId(1L);
        reset(countryDao);
        when(countryDao.loadById(1L)).thenReturn(Optional.of(country));
        when(countryDao.loadById(0L)).thenReturn(Optional.empty());
        when(countryDao.save(any(Country.class))).thenReturn(country);
        when(countryDao.update(any(Country.class))).thenAnswer(i -> i.getArguments()[0]);
        this.countryService = new CountryServiceImpl(modelMapper, countryDao);
    }

    @Test
    public void loadById() {
        CountryDto countryDto = countryService.loadById(1L);
        Country country = dataProvider.getAddress().getCity().getCountry();
        country.setId(1L);
        Assertions.assertEquals(countryDto, modelMapper.map(country, CountryDto.class));
        verify(countryDao).loadById(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> countryService.loadById(0L));
    }

    @Test
    public void save() {
        CountryDto countryDto = countryService.save(modelMapper.map(dataProvider.getAddress().getCity().getCountry(), CountryDto.class));
        Country country = dataProvider.getAddress().getCity().getCountry();
        country.setId(1L);
        Assertions.assertEquals(countryDto, modelMapper.map(country, CountryDto.class));
        verify(countryDao).save(any(Country.class));
    }

    @Test
    public void update() {
        Country country = dataProvider.getAddress().getCity().getCountry();
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);
        countryService.update(countryDto);
        verify(countryDao).update(any(Country.class));
    }

    @Test
    public void delete() {
        Country country = dataProvider.getAddress().getCity().getCountry();
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);
        countryService.delete(countryDto);
        verify(countryDao).delete(any(Country.class));
    }
}
