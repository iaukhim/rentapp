package org.example.rentapp.converters;

import lombok.RequiredArgsConstructor;
import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.entities.Address;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

@RequiredArgsConstructor
public class AddressDtoToEntityConverter implements Converter<AddressDto, Address> {
    private final CityDtoToEntityConverter cityDtoToEntityConverter;

    @Override
    public Address convert(MappingContext<AddressDto, Address> context) {
        AddressDto addressDto = context.getSource();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(cityDtoToEntityConverter);
        Address address = modelMapper.map(addressDto, Address.class);
        return address;
    }
}
