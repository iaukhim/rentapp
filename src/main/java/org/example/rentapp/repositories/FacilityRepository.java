package org.example.rentapp.repositories;

import org.example.rentapp.entities.Facility;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends PagingAndSortingRepository<Facility, Long> {
}
