package org.example.rentapp.converters;

import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.City;
import org.example.rentapp.entities.Facility;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class FacilityDtoToEntityConverter implements Converter<FacilityDto, Facility> {

    private final AddressDtoToEntityConverter addressDtoToEntityConverter;

    public FacilityDtoToEntityConverter(AddressDtoToEntityConverter addressDtoToEntityConverter) {
        this.addressDtoToEntityConverter = addressDtoToEntityConverter;
    }

    @Override
    public Facility convert(MappingContext<FacilityDto, Facility> context) {
        FacilityDto facilityDto = context.getSource();

        ModelMapper modelMapper = new ModelMapper();
        Facility facility = modelMapper.map(facilityDto, Facility.class);
        TypeMap<FacilityDto, City> typeMap = modelMapper.createTypeMap(FacilityDto.class, City.class);
        typeMap.addMapping(FacilityDto::getCityId, City::setId);
        typeMap.addMapping(FacilityDto::getCityName, City::setName);

        City city = modelMapper.map(facilityDto, City.class);
        facility.getAddress().setCity(city);
        return facility;
    }
}
