package org.example.rentapp.converters.city;

import org.example.rentapp.dtos.CityDto;
import org.example.rentapp.entities.City;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class CityEntityToDtoConverter implements Converter<City, CityDto> {
    @Override
    public CityDto convert(MappingContext<City, CityDto> context) {

        ModelMapper modelMapper = new ModelMapper();
        TypeMap<City, CityDto> typeMap = modelMapper.createTypeMap(City.class, CityDto.class);

        typeMap.addMappings(mapper -> {
            mapper.map(source -> source.getCountry().getId(), (destination, value) -> destination.getCountryDto().setId((Long) value));
            mapper.map(source -> source.getCountry().getName(), (destination, value) -> destination.getCountryDto().setName((String) value));
            mapper.map(source -> source.getCountry().getCode(), ((destination, value) -> destination.getCountryDto().setCode((String) value)));
        });
        return modelMapper.map(context.getSource(), context.getDestinationType());
    }
}
