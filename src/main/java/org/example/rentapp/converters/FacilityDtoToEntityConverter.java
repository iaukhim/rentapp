package org.example.rentapp.converters;

import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Facility;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class FacilityDtoToEntityConverter implements Converter<FacilityDto, Facility> {

    @Override
    public Facility convert(MappingContext<FacilityDto, Facility> context) {
        FacilityDto facilityDto = context.getSource();

        ModelMapper modelMapper = new ModelMapper();

        TypeMap<FacilityDto, Facility> typeMap = modelMapper.createTypeMap(FacilityDto.class, Facility.class);
        typeMap.addMappings(
                mapper -> {
                    mapper.map(source -> source.getAddressDto().getCityDto().getId(), (destination, value) -> destination.getAddress().getCity().setId((Long) value));
                    mapper.map(source -> source.getAddressDto().getCityDto().getName(), (destination, value) -> destination.getAddress().getCity().setName((String) value));
                    mapper.map(source -> source.getAddressDto().getCityDto().getCountryDto().getId(), (destination, value) -> destination.getAddress().getCity().getCountry().setId((Long) value));
                    mapper.map(source -> source.getAddressDto().getCityDto().getCountryDto().getName(), (destination, value) -> destination.getAddress().getCity().getCountry().setName((String) value));
                    mapper.map(source -> source.getAddressDto().getCityDto().getCountryDto().getCode(), (destination, value) -> destination.getAddress().getCity().getCountry().setCode((String) value));
                });

        return modelMapper.map(context.getSource(), context.getDestinationType());
    }
}
