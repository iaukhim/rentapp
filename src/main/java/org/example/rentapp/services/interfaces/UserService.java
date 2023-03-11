package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.UserCreationDto;
import org.example.rentapp.dtos.UserDto;

public interface UserService {

    UserDto save(UserCreationDto userCreationDto);

    void update(UserDto userDto);

    void delete(UserDto userDto);

    UserDto loadById(Long id);

    void deleteNonActiveUsers();

    UserDto loadByIdEager(Long id);

    UserDto loadByEmail(String email);
}
