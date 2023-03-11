package org.example.rentapp.servicestests;

import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.UserCreationDto;
import org.example.rentapp.dtos.UserDto;
import org.example.rentapp.entities.User;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.providers.DataProvider;
import org.example.rentapp.services.impl.UserServiceImpl;
import org.example.rentapp.services.impl.UtilServiceImpl;
import org.example.rentapp.services.interfaces.UserService;
import org.example.rentapp.services.interfaces.UtilService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTests {

    private ModelMapper modelMapper = new ModelMapper();

    private UserService userService;

    private UtilService utilService;

    private UserDao userDao = mock(UserDao.class);

    private DataProvider dataProvider = new DataProvider();

    @BeforeEach
    public void before() {
        User user = dataProvider.getUser();
        user.setId(1L);
        reset(userDao);
        when(userDao.loadById(1L)).thenReturn(Optional.of(user));
        when(userDao.loadById(0L)).thenReturn(Optional.empty());
        when(userDao.save(any(User.class))).thenReturn(user);
        when(userDao.update(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(userDao.loadByIdEager(1L)).thenReturn(Optional.of(user));
        when(userDao.loadByIdEager(0L)).thenReturn(Optional.empty());
        this.utilService = new UtilServiceImpl(userDao);
        this.userService = new UserServiceImpl(modelMapper, userDao, utilService, new BCryptPasswordEncoder());
    }

    @Test
    public void loadById() {
        UserDto userDto = userService.loadById(1L);
        User user = dataProvider.getUser();
        user.setId(1L);
        Assertions.assertEquals(userDto, modelMapper.map(user, UserDto.class));
        verify(userDao).loadById(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> userService.loadById(0L));
    }

    @Test
    public void save() {
        UserDto userDto = userService.save(modelMapper.map(dataProvider.getUser(), UserCreationDto.class));
        User user = dataProvider.getUser();
        user.setId(1L);
        Assertions.assertEquals(userDto, modelMapper.map(user, UserDto.class));
        verify(userDao).save(any(User.class));
    }

    @Test
    public void update() {
        User user = dataProvider.getUser();
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userService.update(userDto);
        verify(userDao).update(any(User.class));
    }

    @Test
    public void delete() {
        UserDto userDto = modelMapper.map(dataProvider.getUser(), UserDto.class);
        userService.delete(userDto);
        verify(userDao).delete(any(User.class));
    }

    @Test
    public void deleteNonActiveUsers() {
        userService.deleteNonActiveUsers();
        verify(userDao).deleteNonActiveUsers();
    }

    @Test
    public void loadByIdEager() {
        UserDto userDto = userService.loadByIdEager(1L);
        User user = dataProvider.getUser();
        user.setId(1L);
        Assertions.assertEquals(userDto, modelMapper.map(user, UserDto.class));
        verify(userDao).loadByIdEager(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> userService.loadById(0L));
    }
}
