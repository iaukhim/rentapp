package org.example.rentapp.dao.interfaces;

import org.example.rentapp.entities.Facility;

import java.util.Optional;

public interface FacilityDao extends AbstractDao<Facility, Long> {

    Facility findLargestFacility();

    Optional<Facility> loadByIdEager(Long id);
}
