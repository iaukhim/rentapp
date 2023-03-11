package org.example.rentapp.servicestests;

import org.example.rentapp.dao.interfaces.OrderDao;
import org.example.rentapp.dtos.OrderDto;
import org.example.rentapp.entities.Order;
import org.example.rentapp.exceptions.NoEntityFoundException;
import org.example.rentapp.providers.DataProvider;
import org.example.rentapp.services.impl.OrderServiceImpl;
import org.example.rentapp.services.interfaces.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {

    private ModelMapper modelMapper = new ModelMapper();

    private OrderService orderService;

    private OrderDao orderDao = mock(OrderDao.class);

    private DataProvider dataProvider = new DataProvider();

    @BeforeEach
    public void before() {
        Order order = dataProvider.getOrder();
        order.setId(1L);
        reset(orderDao);
        when(orderDao.loadById(1L)).thenReturn(Optional.of(order));
        when(orderDao.loadById(0L)).thenReturn(Optional.empty());
        when(orderDao.save(any(Order.class))).thenReturn(order);
        when(orderDao.update(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderDao.loadByIdEager(1L)).thenReturn(Optional.of(order));
        when(orderDao.loadByIdEager(0L)).thenReturn(Optional.empty());
        this.orderService = new OrderServiceImpl(modelMapper, orderDao);
    }

    @Test
    public void loadById() {
        OrderDto orderDto = orderService.loadById(1L);
        Order order = dataProvider.getOrder();
        order.setId(1L);
        Assertions.assertEquals(orderDto, modelMapper.map(order, OrderDto.class));
        verify(orderDao).loadById(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> orderService.loadById(0L));
    }

    @Test
    public void save() {
        OrderDto orderDto = orderService.save(modelMapper.map(dataProvider.getOrder(), OrderDto.class));
        Order order = dataProvider.getOrder();
        order.setId(1L);
        Assertions.assertEquals(orderDto, modelMapper.map(order, OrderDto.class));
        verify(orderDao).save(any(Order.class));
    }

    @Test
    public void update() {
        Order order = dataProvider.getOrder();
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        orderService.update(orderDto);
        verify(orderDao).update(any(Order.class));
    }

    @Test
    public void delete() {
        OrderDto orderDto = modelMapper.map(dataProvider.getOrder(), OrderDto.class);
        orderService.delete(orderDto);
        verify(orderDao).delete(any(Order.class));
    }

    @Test
    public void deleteExpiredOrders() {
        orderService.deleteExpiredOrders();
        verify(orderDao).deleteExpiredOrders();
    }

    @Test
    public void loadByIdEager() {
        OrderDto orderDto = orderService.loadByIdEager(1L);
        Order order = dataProvider.getOrder();
        order.setId(1L);
        Assertions.assertEquals(orderDto, modelMapper.map(order, OrderDto.class));
        verify(orderDao).loadByIdEager(anyLong());
        Assertions.assertThrowsExactly(NoEntityFoundException.class, () -> orderService.loadById(0L));
    }
}
