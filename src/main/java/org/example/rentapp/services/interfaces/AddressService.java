package org.example.rentapp.services.interfaces;


import org.example.rentapp.dtos.AddressDto;

public interface AddressService {

    AddressDto save(AddressDto addressDto);

    void update(AddressDto addressDto);

    void delete(AddressDto addressDto);

    AddressDto loadById(Long id);

    void deleteAddressesStartsWith(String startsWith);

    AddressDto loadByIdEager(Long id);
}
