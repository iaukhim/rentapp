package org.example.rentapp.converters;

import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.entities.Address;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class AddressEntityToDtoConverter implements Converter<Address, AddressDto> {

    @Override
    public AddressDto convert(MappingContext<Address, AddressDto> context) {
        ModelMapper modelMapper = new ModelMapper();
        TypeMap<Address, AddressDto> typeMap = modelMapper.typeMap(Address.class, AddressDto.class);
        Address address = context.getSource();

        typeMap.addMappings(
                mapper -> {
                    mapper.map(source -> source.getCity().getId(), (destination, value) -> destination.getCityDto().setId((Long) value));
                    mapper.map(source -> source.getCity().getName(), (destination, value) -> destination.getCityDto().setName((String) value));
                    mapper.map(source -> source.getCity().getCountry().getId(), (destination, value) -> destination.getCityDto().getCountryDto().setId((Long) value));
                    mapper.map(source -> source.getCity().getCountry().getName(), (destination, value) -> destination.getCityDto().getCountryDto().setName((String) value));
                    mapper.map(source -> source.getCity().getCountry().getCode(), (destination, value) -> destination.getCityDto().getCountryDto().setCode((String)value));
                });

        AddressDto addressDto = modelMapper.map(address, AddressDto.class);


        return addressDto;
    }
}