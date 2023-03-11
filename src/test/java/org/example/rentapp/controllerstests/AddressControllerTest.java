package org.example.rentapp.controllerstests;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.rentapp.controllers.AddressController;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.AddressDto;
import org.example.rentapp.entities.Address;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressControllerTest {

    @Autowired
    private AddressController addressController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDao userDao;

    private String renterJwtToken;

    private String adminJwtToken;

    private String landlordJwtToken;

    @BeforeEach
    public void before() {
        Address address = dataProvider.getAddress();
        address = addressDao.save(address);
        address = addressDao.update(address);

        User renter = dataProvider.getUser();
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
                        .get("/api/users/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/addresses/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.district").value(dataProvider.getAddress().getDistrict()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.streetName").value(dataProvider.getAddress().getStreetName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.streetNumber").value(dataProvider.getAddress().getStreetNumber()));
    }

    @Test
    public void save() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/addresses"))
                .andExpect(status().isUnauthorized());

        Address address = dataProvider.getAddress();
        String newStreetNumber = "999/99";
        address.setStreetNumber(newStreetNumber);
        address.setId(null);
        address.setCity(addressDao.loadById(1L).get().getCity());
        AddressDto addressDto = modelMapper.map(address, AddressDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/addresses")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.district").value(dataProvider.getAddress().getDistrict()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.streetName").value(dataProvider.getAddress().getStreetName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.streetNumber").value(newStreetNumber));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/addresses/1"))
                .andExpect(status().isUnauthorized());

        Address address = addressDao.loadByIdEager(1L).get();
        Assertions.assertTrue(address.getStreetNumber().equals(dataProvider.getAddress().getStreetNumber()));
        String newStreetNumber = "999/99";
        address.setStreetNumber(newStreetNumber);
        AddressDto addressDto = modelMapper.map(address, AddressDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/addresses/1")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDto)))
                .andExpect(status().isNoContent());
        address = addressDao.loadById(1L).get();
        Assertions.assertTrue(address.getStreetNumber().equals(newStreetNumber));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/addresses/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/addresses/1")
                        .header("Authorization", renterJwtToken))
                .andExpect(status().isForbidden());

        Assertions.assertTrue(addressDao.loadById(1L).isPresent());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/addresses/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(addressDao.loadById(1L).isEmpty());
    }
}
