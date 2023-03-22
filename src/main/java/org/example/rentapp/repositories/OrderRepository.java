package org.example.rentapp.repositories;

import org.example.rentapp.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    Page<Order> findAllByFacilityOwnerEmail(PageRequest pageRequest, String email);

    Page<Order> findAllByRenterEmail(String email, PageRequest pageRequest);
}
