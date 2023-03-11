package org.example.rentapp.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import org.example.rentapp.dao.interfaces.OrderDao;
import org.example.rentapp.entities.Order;
import org.example.rentapp.entities.Order_;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderDaoImpl extends AbstractDaoImpl<Order, Long> implements OrderDao {

    @Override
    public void deleteExpiredOrders() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<Order> query = cb.createCriteriaDelete(Order.class);
        Root<Order> root = query.from(Order.class);
        query.where(cb.lessThan(root.get(Order_.PLANNED_DATE), LocalDate.now()));
        entityManager.createQuery(query).executeUpdate();
    }

    @Override
    public Optional<Order> loadByIdEager(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        root.fetch(Order_.FACILITY, JoinType.INNER);
        root.fetch(Order_.RENTER, JoinType.INNER);
        query.select(root);

        List<Order> orderList = entityManager.createQuery(query).getResultList();
        return orderList.isEmpty() ? Optional.empty() : Optional.of(orderList.get(0));
    }
}
