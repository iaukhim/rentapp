package org.example.rentapp.converters;

import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Facility;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class FacilityEntityToDtoConverter implements Converter<Facility, FacilityDto> {
    @Override
    public FacilityDto convert(MappingContext<Facility, FacilityDto> context) {
        Facility facility = context.getSource();

        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Facility, FacilityDto> typeMap = modelMapper.createTypeMap(Facility.class, FacilityDto.class);

        typeMap.addMappings(
                mapper -> {
                    mapper.map(src -> src.getAddress().getCity().getId(), FacilityDto::setCityId);
                    mapper.map(src -> src.getAddress().getCity().getName(), FacilityDto::setCityName);
                    mapper.map(src -> src.getAddress().getCity().getCountry().getId(), FacilityDto::setCountryId);
                    mapper.map(src -> src.getAddress().getCity().getCountry().getCode(), FacilityDto::setCountryCode);
                    mapper.map(src -> src.getAddress().getCity().getCountry().getName(), FacilityDto::setCountryName);
                });

        FacilityDto facilityDto = modelMapper.map(facility, FacilityDto.class);
        return facilityDto;
    }
}
