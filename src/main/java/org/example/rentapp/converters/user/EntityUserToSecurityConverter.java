package org.example.rentapp.converters.user;

import org.example.rentapp.entities.User;
import org.example.rentapp.security.SecurityUser;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

public class EntityUserToSecurityConverter implements Converter<User, SecurityUser> {
    @Override
    public SecurityUser convert(MappingContext<User, SecurityUser> context) {
        User entityUser = context.getSource();
        return SecurityUser.builder()
                .email(entityUser.getEmail())
                .password(entityUser.getPassword())
                .status(entityUser.getStatus())
                .roles(entityUser.getRoles()).build();
    }
}
