package org.example.rentapp.services.impl;

import jakarta.transaction.Transactional;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.UserCreationDto;
import org.example.rentapp.dtos.UserDto;
import org.example.rentapp.entities.User;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.exceptions.UserAlreadyExists;
import org.example.rentapp.services.interfaces.UserService;
import org.example.rentapp.services.interfaces.UtilService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final ModelMapper mapper;

    private final UserDao userDao;

    private final UtilService utilService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(ModelMapper mapper, UserDao userDao, UtilService utilService, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.userDao = userDao;
        this.utilService = utilService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto loadByEmail(String email) {
        User user = userDao.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with " + email + " was not found"));
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto save(UserCreationDto userCreationDto) {
        if (utilService.checkAccountExistence(userCreationDto.getEmail())) {
            throw new UserAlreadyExists(userCreationDto.getEmail());
        }
        User user = mapper.map(userCreationDto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userDao.save(user);
        return mapper.map(savedUser, UserDto.class);
    }

    @Override
    public void update(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        userDao.update(user);
    }

    @Override
    public void delete(UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        userDao.delete(user);
    }

    @Override
    public UserDto loadById(Long id) {
        Optional<User> user = userDao.loadById(id);
        return mapper.map(user.orElseThrow(() -> new NoEntityFoundException(id, User.class)), UserDto.class);
    }

    @Override
    public void deleteNonActiveUsers() {
        userDao.deleteNonActiveUsers();
    }

    @Override
    public UserDto loadByIdEager(Long id) {
        Optional<User> user = userDao.loadByIdEager(id);
        return mapper.map(user.orElseThrow(() -> new NoEntityFoundException(id, User.class)), UserDto.class);
    }

    @Override
    public void diActivateAccount(String email) {
        userDao.diActivateAccount(email);
    }
}
