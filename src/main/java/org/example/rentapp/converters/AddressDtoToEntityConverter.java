package org.example.rentapp.converters;

import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.entities.Address;
import org.example.rentapp.entities.Country;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

public class AddressDtoToEntityConverter implements Converter<AddressDto, Address> {
    @Override
    public Address convert(MappingContext<AddressDto, Address> context) {
        AddressDto addressDto = context.getSource();
        ModelMapper modelMapper = new ModelMapper();
        Address address = modelMapper.map(addressDto, Address.class);
        Country country = new Country();
        country.setId(addressDto.getCountryId());
        country.setCode(addressDto.getCountryCode());
        country.setName(addressDto.getCountryName());
        address.getCity().setCountry(country);
        return address;
    }
}
