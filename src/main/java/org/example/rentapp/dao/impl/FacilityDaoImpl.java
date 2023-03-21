package org.example.rentapp.dao.impl;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.example.rentapp.dao.interfaces.FacilityDao;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.entities.Facility_;
import org.example.rentapp.repositories.FacilityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FacilityDaoImpl extends AbstractDaoImpl<Facility, Long> implements FacilityDao {
    private final FacilityRepository facilityRepository;
    @Override
    public Facility findLargestFacility() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Facility> query = cb.createQuery(Facility.class);
        Root<Facility> root = query.from(Facility.class);
        query.select(root);

        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.desc(root.get(Facility_.SPACE)));
        query.orderBy(orderList);
        TypedQuery<Facility> facilityTypedQuery = entityManager.createQuery(query).setMaxResults(1);
        return facilityTypedQuery.getResultList().get(0);
    }

    @Override
    public Optional<Facility> loadByIdEager(Long id) {
        EntityGraph<?> graph = entityManager.getEntityGraph("facility-entity-graph-full-eager");
        HashMap hints = new HashMap();
        hints.put("javax.persistence.fetchgraph", graph);
        Facility facility = entityManager.find(Facility.class, id, hints);
        return Optional.ofNullable(facility);
    }

    @Override
    public Page<Facility> loadAll(PageRequest pageRequest) {
        return facilityRepository.findAll(pageRequest);
    }

}
