package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.OrderDto;

public interface OrderService {

    OrderDto save(OrderDto orderDto);

    void update(OrderDto orderDto);

    void delete(OrderDto orderDto);

    OrderDto loadById(Long id);

    void deleteExpiredOrders();

    OrderDto loadByIdEager(Long id);
}
