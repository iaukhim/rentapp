package org.example.rentapp.dao.interfaces;

import org.example.rentapp.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface OrderDao extends AbstractDao<Order, Long> {

    void deleteExpiredOrders();

    Optional<Order> loadByIdEager(Long id);

    Page<Order> loadRenterOrders(PageRequest pageRequest, String email);

    Page<Order> loadLandlordOrders(PageRequest pageRequest, String email);
}
