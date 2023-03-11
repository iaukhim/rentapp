package org.example.rentapp.controllers;

import org.example.rentapp.dtos.OrderDto;
import org.example.rentapp.services.interfaces.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final String ROLE_LANDLORD = "ROLE_LANDLORD";

    private final String ROLE_RENTER = "ROLE_RENTER";

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public OrderDto save(@RequestBody OrderDto orderDto) {
        return orderService.save(orderDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured({ROLE_ADMIN})
    public void delete(@PathVariable(name = "id") Long id) {
        OrderDto orderDto = orderService.loadById(id);
        orderService.delete(orderDto);
    }

    @PutMapping("/{id}")
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody OrderDto orderDto) {
        orderService.update(orderDto);
    }

    @GetMapping("/{id}")
    @Secured({ROLE_LANDLORD, ROLE_RENTER, ROLE_ADMIN})
    public OrderDto loadById(@PathVariable(name = "id") Long id) {
        return orderService.loadById(id);
    }
}
