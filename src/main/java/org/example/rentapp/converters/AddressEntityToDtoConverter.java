package org.example.rentapp.converters;

import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.entities.Address;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class AddressEntityToDtoConverter implements Converter<Address, AddressDto> {
    @Override
    public AddressDto convert(MappingContext<Address, AddressDto> context) {
        ModelMapper modelMapper = new ModelMapper();
        Address address = context.getSource();

        AddressDto addressDto = modelMapper.map(address, AddressDto.class);

        addressDto.setCountryId(address.getCity().getCountry().getId());
        addressDto.setCountryName(address.getCity().getCountry().getName());
        addressDto.setCountryCode(address.getCity().getCountry().getCode());
        return addressDto;
    }
}
