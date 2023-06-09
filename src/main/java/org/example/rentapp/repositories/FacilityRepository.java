package org.example.rentapp.repositories;

import org.example.rentapp.entities.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends PagingAndSortingRepository<Facility, Long>, JpaSpecificationExecutor<Facility> {

    Page<Facility> findAllByOwnerEmail(PageRequest pageRequest, String renterEmail);

}
