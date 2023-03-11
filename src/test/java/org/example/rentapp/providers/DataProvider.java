package org.example.rentapp.providers;

import lombok.NoArgsConstructor;
import org.example.rentapp.entities.Address;
import org.example.rentapp.entities.City;
import org.example.rentapp.entities.Country;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.entities.Order;
import org.example.rentapp.entities.Role;
import org.example.rentapp.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@NoArgsConstructor
public class DataProvider {

    public User getUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("password");
        user.setJoindate(LocalDate.now());
        user.setStatus(true);

        Role role = new Role();
        role.setId(2L);
        role.setName("renter");
        ArrayList<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);
        return user;
    }

    public Facility getFacility() {
        Facility facility = new Facility();
        facility.setName("zal1");
        facility.setSpace(300D);
        return facility;
    }

    public Address getAddress() {
        Country country = new Country();
        country.setCode("BLR");
        country.setName("Belarus");

        City city = new City();
        city.setCountry(country);
        city.setName("Grodno");

        Address address = new Address();
        address.setCity(city);
        address.setDistrict("leninsky");
        address.setStreetName("Dubko");
        address.setStreetNumber("17");

        return address;
    }

    public Order getOrder() {
        Order order = new Order();
        order.setCreationDate(LocalDate.now());
        order.setPlannedDate(LocalDate.now().plusMonths(2));
        return order;
    }

}
