package org.example.rentapp.servicestests;

import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.entities.Address;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.providers.DataProvider;
import org.example.rentapp.services.impl.AddressServiceImpl;
import org.example.rentapp.services.interfaces.AddressService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddressServiceTest {

    private ModelMapper modelMapper = new ModelMapper();

    private AddressService addressService;

    private AddressDao addressDao = mock(AddressDao.class);

    private DataProvider dataProvider = new DataProvider();

    @BeforeEach
    public void before() {
        Address address = dataProvider.getAddress();
        address.setId(1L);
        reset(addressDao);
        when(addressDao.loadById(1L)).thenReturn(Optional.of(address));
        when(addressDao.loadById(0L)).thenReturn(Optional.empty());
        when(addressDao.save(any(Address.class))).thenReturn(address);
        when(addressDao.update(any(Address.class))).thenAnswer(i -> i.getArguments()[0]);
        when(addressDao.loadByIdEager(1L)).thenReturn(Optional.of(address));
        when(addressDao.loadByIdEager(0L)).thenReturn(Optional.empty());
        this.addressService = new AddressServiceImpl(modelMapper, addressDao);
    }

    @Test
    public void loadById() {
        AddressDto addressDto = addressService.loadById(1L);
        Address address = dataProvider.getAddress();
        address.setId(1L);
        Assertions.assertEquals(addressDto, modelMapper.map(address, AddressDto.class));
        verify(addressDao).loadById(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> addressService.loadById(0L));
    }

    @Test
    public void save() {
        AddressDto addressDto = addressService.save(modelMapper.map(dataProvider.getAddress(), AddressDto.class));
        Address address = dataProvider.getAddress();
        address.setId(1L);
        Assertions.assertEquals(addressDto, modelMapper.map(address, AddressDto.class));
        verify(addressDao).save(any(Address.class));
    }

    @Test
    public void update() {
        Address address = dataProvider.getAddress();
        AddressDto addressDto = modelMapper.map(address, AddressDto.class);
        addressService.update(addressDto);
        verify(addressDao).update(any(Address.class));
    }

    @Test
    public void delete() {
        Address address = dataProvider.getAddress();
        AddressDto addressDto = modelMapper.map(address, AddressDto.class);
        addressService.delete(addressDto);
        verify(addressDao).delete(any(Address.class));
    }

    @Test
    public void deleteAddressesStartsWith() {
        addressService.deleteAddressesStartsWith("aa");
        verify(addressDao).deleteAddressesStartsWith(anyString());
    }

    @Test
    public void loadByIdEager() {
        AddressDto addressDto = addressService.loadByIdEager(1L);
        Address address = dataProvider.getAddress();
        address.setId(1L);
        Assertions.assertEquals(addressDto, modelMapper.map(address, AddressDto.class));
        verify(addressDao).loadByIdEager(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> addressService.loadById(0L));
    }
}
