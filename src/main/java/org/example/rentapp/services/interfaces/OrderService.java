package org.example.rentapp.services.interfaces;

import org.example.rentapp.dtos.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface OrderService {

    OrderDto save(OrderDto orderDto);

    void update(OrderDto orderDto);

    void delete(OrderDto orderDto);

    OrderDto loadById(Long id);

    void deleteExpiredOrders();

    OrderDto loadByIdEager(Long id);

    Page<OrderDto> loadRenterOrders(PageRequest pageRequest, String email);

    Page<OrderDto> loadLandlordOrders(PageRequest pageRequest, String email);
}
