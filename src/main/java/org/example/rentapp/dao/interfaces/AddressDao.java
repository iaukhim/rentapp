package org.example.rentapp.dao.interfaces;

import org.example.rentapp.entities.Address;

import java.util.Optional;

public interface AddressDao extends AbstractDao<Address, Long> {

    void deleteAddressesStartsWith(String startsWith);

    Optional<Address> loadByIdEager(Long id);
}
