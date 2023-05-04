package org.example.rentapp.controllerstests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.example.rentapp.controllers.OrderController;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dao.interfaces.FacilityDao;
import org.example.rentapp.dao.interfaces.OrderDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.OrderDto;
import org.example.rentapp.entities.Address;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.entities.Order;
import org.example.rentapp.entities.Role;
import org.example.rentapp.entities.User;
import org.example.rentapp.providers.DataProvider;
import org.example.rentapp.security.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private FacilityDao facilityDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private JwtService jwtService;

    private String renterJwtToken;

    private String adminJwtToken;

    private String landlordJwtToken;

    @BeforeEach
    public void before() {
        Address address = dataProvider.getAddress();
        address = addressDao.save(address);
        address = addressDao.update(address);
        User user = dataProvider.getUser();
        user = userDao.save(user);
        user.setAddress(address);
        user = userDao.update(user);

        Facility facility = dataProvider.getFacility();
        facility.setOwner(user);
        facility.setAddress(address);
        facility = facilityDao.update(facility);

        Order order = dataProvider.getOrder();
        order.setRenter(user);
        order.setFacility(facility);
        order = orderDao.update(order);

        objectMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        User renter = dataProvider.getUser();
        renter.setEmail("renter@gmail.com");
        renter = userDao.save(renter);
        userDao.update(renter);
        renterJwtToken = jwtService.createToken(renter.getEmail());

        User admin = dataProvider.getUser();
        admin.setId(null);
        admin.setRoles(List.of(new Role(1L, "ADMIN")));
        admin.setEmail("admin@gmail.com");
        admin = userDao.save(admin);
        userDao.update(admin);
        adminJwtToken = jwtService.createToken(admin.getEmail());

        User landLord = dataProvider.getUser();
        landLord.setId(null);
        landLord.setRoles(List.of(new Role(3L, "LANDLORD")));
        landLord.setEmail("landlord@gmail.com");
        landLord = userDao.save(landLord);
        userDao.update(landLord);
        landlordJwtToken = jwtService.createToken(landLord.getEmail());
    }

    @Test
    public void loadById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/orders/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/orders/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));
    }

    @Test
    public void save() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/orders"))
                .andExpect(status().isUnauthorized());


        Order order = dataProvider.getOrder();
        order.setFacility(facilityDao.loadById(1L).get());
        order.setRenter(userDao.loadById(1L).get());
        LocalDate newPlannedDate = LocalDate.now().plusYears(2);
        order.setPlannedDate(newPlannedDate);
        order.setId(null);
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/orders")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/orders/1"))
                .andExpect(status().isUnauthorized());

        Order order = orderDao.loadByIdEager(1L).get();
        Assertions.assertEquals(order.getPlannedDate(), dataProvider.getOrder().getPlannedDate());
        LocalDate newPlannedDate = LocalDate.now().plusYears(2);
        order.setPlannedDate(newPlannedDate);
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/orders/1")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isNoContent());
        order = orderDao.loadById(1L).get();
        Assertions.assertEquals(order.getPlannedDate(), newPlannedDate);
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/orders/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/orders/1")
                        .header("Authorization", renterJwtToken))
                .andExpect(status().isForbidden());

        Assertions.assertTrue(orderDao.loadById(1L).isPresent());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/orders/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(orderDao.loadById(1L).isEmpty());
    }

}
