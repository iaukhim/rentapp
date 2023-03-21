package org.example.rentapp.converters.user;

import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceUtil;
import org.example.rentapp.dtos.UserDto;
import org.example.rentapp.entities.User;
import org.example.rentapp.entities.User_;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.Condition;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

public class UserEntityToDtoConverter implements Converter<User, UserDto> {
    private final PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Override
    public UserDto convert(MappingContext<User, UserDto> context) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(new Condition<>() {
            public boolean applies(MappingContext<Object, Object> context) {
                return !(context.getSource() instanceof HibernateProxy || context.getSource() instanceof PersistentCollection<?>);
            }
        });

        TypeMap<User, UserDto> typeMap = modelMapper.typeMap(User.class, UserDto.class);
        typeMap.addMappings(
                mapper -> {
                    if (persistenceUtil.isLoaded(context.getSource(), User_.ADDRESS)) {
                        mapper.map(source -> source.getAddress().getId(), (destination, value) -> destination.getAddressDto().setId((Long) value));
                        mapper.map(source -> source.getAddress().getDistrict(), (destination, value) -> destination.getAddressDto().setDistrict((String) value));
                        mapper.map(source -> source.getAddress().getStreetName(), (destination, value) -> destination.getAddressDto().setStreetName((String) value));
                        mapper.map(source -> source.getAddress().getStreetNumber(), (destination, value) -> destination.getAddressDto().setStreetNumber((String) value));
                        mapper.map(source -> source.getAddress().getCity().getId(), (destination, value) -> destination.getAddressDto().getCityDto().setId((Long) value));
                        mapper.map(source -> source.getAddress().getCity().getName(), (destination, value) -> destination.getAddressDto().getCityDto().setName((String) value));
                        mapper.map(source -> source.getAddress().getCity().getCountry().getId(), (destination, value) -> destination.getAddressDto().getCityDto().getCountryDto().setId((Long) value));
                        mapper.map(source -> source.getAddress().getCity().getCountry().getName(), (destination, value) -> destination.getAddressDto().getCityDto().getCountryDto().setName((String) value));
                        mapper.map(source -> source.getAddress().getCity().getCountry().getCode(), (destination, value) -> destination.getAddressDto().getCityDto().getCountryDto().setCode((String) value));
                    }
                });

        UserDto userDto = modelMapper.map(context.getSource(), UserDto.class);
        return userDto;
    }
}
