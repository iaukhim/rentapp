package org.example.rentapp.servicestests;

import org.example.rentapp.dao.interfaces.FacilityDao;
import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.providers.DataProvider;
import org.example.rentapp.services.impl.FacilityServiceImpl;
import org.example.rentapp.services.interfaces.FacilityService;
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

public class FacilityServiceTest {

    private ModelMapper modelMapper = new ModelMapper();

    private FacilityService facilityService;

    private FacilityDao facilityDao = mock(FacilityDao.class);

    private DataProvider dataProvider = new DataProvider();

    @BeforeEach
    public void before() {
        Facility facility = dataProvider.getFacility();
        facility.setId(1L);
        reset(facilityDao);
        when(facilityDao.loadById(1L)).thenReturn(Optional.of(facility));
        when(facilityDao.loadById(0L)).thenReturn(Optional.empty());
        when(facilityDao.save(any(Facility.class))).thenReturn(facility);
        when(facilityDao.update(any(Facility.class))).thenAnswer(i -> i.getArguments()[0]);
        when(facilityDao.loadByIdEager(1L)).thenReturn(Optional.of(facility));
        when(facilityDao.loadByIdEager(0L)).thenReturn(Optional.empty());
        when(facilityDao.findLargestFacility()).thenReturn(facility);
        this.facilityService = new FacilityServiceImpl(modelMapper, facilityDao);
    }

    @Test
    public void loadById() {
        FacilityDto facilityDto = facilityService.loadById(1L);
        Facility facility = dataProvider.getFacility();
        facility.setId(1L);
        Assertions.assertEquals(facilityDto, modelMapper.map(facility, FacilityDto.class));
        verify(facilityDao).loadById(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> facilityService.loadById(0L));
    }

    @Test
    public void save() {
        FacilityDto facilityDto = facilityService.save(modelMapper.map(dataProvider.getFacility(), FacilityDto.class));
        Facility facility = dataProvider.getFacility();
        facility.setId(1L);
        Assertions.assertEquals(facilityDto, modelMapper.map(facility, FacilityDto.class));
        verify(facilityDao).save(any(Facility.class));
    }

    @Test
    public void update() {
        Facility facility = dataProvider.getFacility();
        FacilityDto facilityDto = modelMapper.map(facility, FacilityDto.class);
        facilityService.update(facilityDto);
        verify(facilityDao).update(any(Facility.class));
    }

    @Test
    public void delete() {
        FacilityDto facilityDto = modelMapper.map(dataProvider.getFacility(), FacilityDto.class);
        facilityService.delete(facilityDto);
        verify(facilityDao).delete(any(Facility.class));
    }

    @Test
    public void findLargestFacility() {
        FacilityDto largestFacility = facilityService.findLargestFacility();
        verify(facilityDao).findLargestFacility();
    }

    @Test
    public void loadByIdEager() {
        FacilityDto facilityDto = facilityService.loadByIdEager(1L);
        Facility facility = dataProvider.getFacility();
        facility.setId(1L);
        Assertions.assertEquals(facilityDto, modelMapper.map(facility, FacilityDto.class));
        verify(facilityDao).loadByIdEager(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> facilityService.loadById(0L));
    }
}
