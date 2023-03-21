package org.example.rentapp.converters;

import org.example.rentapp.dtos.CityDto;
import org.example.rentapp.entities.City;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class CityDtoToEntityConverter implements Converter<CityDto, City> {

    @Override
    public City convert(MappingContext<CityDto, City> context) {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<CityDto, City> typeMap = modelMapper.createTypeMap(CityDto.class, City.class);

        typeMap.addMappings(mapper -> {
                    mapper.map(source -> source.getCountryDto().getId(), (destination, value) -> destination.getCountry().setId((Long) value));
                    mapper.map(source -> source.getCountryDto().getName(), (destination, value) -> destination.getCountry().setName((String) value));
                    mapper.map(source -> source.getCountryDto().getCode(), (destination, value) -> destination.getCountry().setCode((String) value));
                }
        );
        City city = modelMapper.map(context.getSource(), context.getDestinationType());
        return city;
    }
}
