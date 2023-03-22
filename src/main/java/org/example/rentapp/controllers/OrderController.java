package org.example.rentapp.controllers;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.example.rentapp.additional.sorts.OrderSort;
import org.example.rentapp.dtos.OrderDto;
import org.example.rentapp.services.interfaces.OrderService;
import org.example.rentapp.services.interfaces.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final String ROLE_LANDLORD = "ROLE_LANDLORD";

    private final String ROLE_RENTER = "ROLE_RENTER";

    private final String ROLE_ADMIN = "ROLE_ADMIN";

    private final OrderService orderService;

    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    @Secured({ROLE_RENTER, ROLE_ADMIN})
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
    @Secured({ROLE_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody OrderDto orderDto) {
        orderService.update(orderDto);
    }

    @GetMapping("/{id}")
    @Secured({ROLE_ADMIN})
    public OrderDto loadById(@PathVariable(name = "id") Long id) {
        return orderService.loadById(id);
    }

    @PutMapping("/my-orders/")
    @Secured({ROLE_RENTER, ROLE_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUserOrder(@RequestBody OrderDto orderDto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        String renterEmail = orderService.loadByIdEager(orderDto.getId()).getRenter().getEmail();
        if (!email.equals(renterEmail)) {
            throw new AccessDeniedException("You cannot update another user`s order");
        }
        orderService.update(orderDto);
    }

    @GetMapping("/my-orders/renter")
    @Secured({ROLE_RENTER, ROLE_ADMIN})
    public Page<OrderDto> loadCurrentRenterUserOrders(
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(50) Integer limit,
            @RequestParam(name = "sort", defaultValue = "ID_ASC") OrderSort sort
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.loadRenterOrders(PageRequest.of(offset, limit, sort.getSortValue()), email);
    }

    @GetMapping("/my-orders/landlord")
    @Secured({ROLE_LANDLORD, ROLE_ADMIN})
    public Page<OrderDto> loadCurrentLandlordUserOrders(
            @RequestParam(name = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(name = "limit", defaultValue = "10") @Min(1) @Max(50) Integer limit,
            @RequestParam(name = "sort", defaultValue = "ID_ASC") OrderSort sort
    ) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderService.loadLandlordOrders(PageRequest.of(offset, limit, sort.getSortValue()), email);
    }

    @DeleteMapping("/my-orders/{id}")
    @Secured({ROLE_RENTER, ROLE_ADMIN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUserOrder(@PathVariable(name = "id") Long id) {
        OrderDto orderDto = orderService.loadByIdEager(id);
        String renterEmail = orderDto.getRenter().getEmail();
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!renterEmail.equals(currentUserEmail)) {
            throw new AccessDeniedException("You cannot delete another user`s order");
        }
        orderService.delete(orderDto);
    }

    @GetMapping("/my-orders/{id}")
    @Secured({ROLE_ADMIN})
    public OrderDto loadByIdCurrentUserOrder(@PathVariable(name = "id") Long id) {
        OrderDto orderDto = orderService.loadByIdEager(id);
        String renterEmail = orderDto.getRenter().getEmail();
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!renterEmail.equals(currentUserEmail)) {
            throw new AccessDeniedException("You cannot load another user`s order");
        }
        return orderDto;
    }
}
