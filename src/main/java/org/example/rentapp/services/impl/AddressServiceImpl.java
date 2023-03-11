package org.example.rentapp.services.impl;

import jakarta.transaction.Transactional;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.entities.Address;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.services.interfaces.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final ModelMapper mapper;

    private final AddressDao addressDao;

    @Autowired
    public AddressServiceImpl(ModelMapper mapper, AddressDao addressDao) {
        this.mapper = mapper;
        this.addressDao = addressDao;
    }

    @Override
    public AddressDto save(AddressDto addressDto) {
        Address addressEntity = mapper.map(addressDto, Address.class);
        Address address = addressDao.save(addressEntity);
        AddressDto savedAddressDto = mapper.map(address, AddressDto.class);
        return savedAddressDto;
    }

    @Override
    public void update(AddressDto addressDto) {
        Address addressEntity = mapper.map(addressDto, Address.class);
        addressDao.update(addressEntity);
    }

    @Override
    public void delete(AddressDto addressDto) {
        Address addressEntity = mapper.map(addressDto, Address.class);
        addressDao.delete(addressEntity);
    }

    @Override
    public AddressDto loadById(Long id) {
        Optional<Address> address = addressDao.loadById(id);
        return mapper.map(address.orElseThrow(() -> new NoEntityFoundException(id, Address.class)), AddressDto.class);
    }

    @Override
    public void deleteAddressesStartsWith(String startsWith) {
        addressDao.deleteAddressesStartsWith(startsWith);
    }

    @Override
    public AddressDto loadByIdEager(Long id) {
        Optional<Address> address = addressDao.loadByIdEager(id);
        return mapper.map(address.orElseThrow(() -> new NoEntityFoundException(id, Address.class)), AddressDto.class);
    }
}
