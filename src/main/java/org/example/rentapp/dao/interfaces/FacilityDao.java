package org.example.rentapp.dao.interfaces;

import org.example.rentapp.entities.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface FacilityDao extends AbstractDao<Facility, Long> {

    Facility findLargestFacility();

    Optional<Facility> loadByIdEager(Long id);

    Page<Facility> loadAll(PageRequest pageRequest);
}

