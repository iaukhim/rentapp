package org.example.rentapp.dao.impl;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.Query;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.entities.Address;
import org.example.rentapp.entities.Address_;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Optional;

@Repository
public class AddressDaoImpl extends AbstractDaoImpl<Address, Long> implements AddressDao {

    private final String DELETE_ADDRESSES_STARTS_WITH_QUERY = "DELETE FROM " + Address.class.getName() + " WHERE " + Address_.STREET_NAME + " LIKE CONCAT (:startsWith, '%')";

    @Override
    public void deleteAddressesStartsWith(String startsWith) {
        Query query = entityManager.createQuery(DELETE_ADDRESSES_STARTS_WITH_QUERY);
        query.setParameter("startsWith", startsWith);
        query.executeUpdate();
    }

    @Override
    public Optional<Address> loadByIdEager(Long id) {
        EntityGraph<?> entityGraph = entityManager.createEntityGraph(Address.class);
        entityGraph.addAttributeNodes(Address_.CITY);

        HashMap hints = new HashMap();
        hints.put("javax.persistence.loadgraph", entityGraph);
        Address address = entityManager.find(Address.class, id, hints);
        return Optional.ofNullable(address);
    }
}
