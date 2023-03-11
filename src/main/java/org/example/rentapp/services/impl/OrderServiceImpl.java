package org.example.rentapp.services.impl;

import jakarta.transaction.Transactional;
import org.example.rentapp.dao.interfaces.OrderDao;
import org.example.rentapp.dtos.OrderDto;
import org.example.rentapp.entities.Order;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.services.interfaces.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final ModelMapper mapper;

    private final OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(ModelMapper mapper, OrderDao orderDao) {
        this.mapper = mapper;
        this.orderDao = orderDao;
    }

    @Override
    public OrderDto save(OrderDto orderDto) {
        Order orderEntity = mapper.map(orderDto, Order.class);
        return mapper.map(orderDao.save(orderEntity), OrderDto.class);
    }

    @Override
    public void update(OrderDto orderDto) {
        Order orderEntity = mapper.map(orderDto, Order.class);
        orderDao.update(orderEntity);
    }

    @Override
    public void delete(OrderDto orderDto) {
        Order orderEntity = mapper.map(orderDto, Order.class);
        orderDao.delete(orderEntity);
    }

    @Override
    public OrderDto loadById(Long id) {
        Optional<Order> order = orderDao.loadById(id);
        return mapper.map(order.orElseThrow(() -> new NoEntityFoundException(id, Order.class)), OrderDto.class);
    }

    @Override
    public void deleteExpiredOrders() {
        orderDao.deleteExpiredOrders();
    }

    @Override
    public OrderDto loadByIdEager(Long id) {
        Optional<Order> order = orderDao.loadByIdEager(id);
        return mapper.map(order.orElseThrow(() -> new NoEntityFoundException(id, Order.class)), OrderDto.class);
    }
}
