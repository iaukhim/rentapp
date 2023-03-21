package org.example.rentapp.converters.user;

import org.example.rentapp.dtos.UserDto;
import org.example.rentapp.entities.User;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class UserDtoToEntityConverter implements Converter<UserDto, User> {
    @Override
    public User convert(MappingContext<UserDto, User> context) {

        ModelMapper modelMapper = new ModelMapper();

        TypeMap<UserDto, User> typeMap = modelMapper.createTypeMap(UserDto.class, User.class);
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
