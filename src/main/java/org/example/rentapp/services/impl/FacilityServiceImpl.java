package org.example.rentapp.services.impl;

import jakarta.transaction.Transactional;
import org.example.rentapp.dao.interfaces.FacilityDao;
import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.services.interfaces.FacilityService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class FacilityServiceImpl implements FacilityService {

    private final ModelMapper mapper;

    private final FacilityDao facilityDao;

    @Autowired
    public FacilityServiceImpl(ModelMapper mapper, FacilityDao facilityDao) {
        this.mapper = mapper;
        this.facilityDao = facilityDao;
    }

    @Override
    public FacilityDto save(FacilityDto facilityDto) {
        Facility facilityEntity = mapper.map(facilityDto, Facility.class);
        return mapper.map(facilityDao.save(facilityEntity), FacilityDto.class);
    }

    @Override
    public void update(FacilityDto facilityDto) {
        Facility facilityEntity = mapper.map(facilityDto, Facility.class);
        facilityDao.update(facilityEntity);
    }

    @Override
    public void delete(FacilityDto facilityDto) {
        Facility facilityEntity = mapper.map(facilityDto, Facility.class);
        facilityDao.delete(facilityEntity);
    }

    @Override
    public FacilityDto loadById(Long id) {
        Optional<Facility> facility = facilityDao.loadById(id);
        return mapper.map(facility.orElseThrow(() -> new NoEntityFoundException(id, Facility.class)), FacilityDto.class);
    }

    @Override
    public FacilityDto findLargestFacility() {
        Facility largestFacility = facilityDao.findLargestFacility();
        return mapper.map(largestFacility, FacilityDto.class);
    }

    @Override
    public FacilityDto loadByIdEager(Long id) {
        Optional<Facility> facility = facilityDao.loadByIdEager(id);
        return mapper.map(facility.orElseThrow(() -> new NoEntityFoundException(id, Facility.class)), FacilityDto.class);
    }

    @Override
    public Page<FacilityDto> loadAll(PageRequest pageRequest) {
        Page<Facility> facilities = facilityDao.loadAll(pageRequest);
        return facilities.map(n -> mapper.map(n, FacilityDto.class));
    }
}
