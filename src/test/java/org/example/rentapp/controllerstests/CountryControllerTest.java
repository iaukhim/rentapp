package org.example.rentapp.controllerstests;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.rentapp.controllers.CountryController;
import org.example.rentapp.dao.interfaces.CountryDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.CountryDto;
import org.example.rentapp.entities.Country;
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
public class CountryControllerTest {

    @Autowired
    private CountryController countryController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDao userDao;

    private String renterJwtToken;

    private String adminJwtToken;

    private String landlordJwtToken;

    @BeforeEach
    public void before() {
        Country country = dataProvider.getAddress().getCity().getCountry();
        country = countryDao.save(country);
        country = countryDao.update(country);

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
                        .get("/api/countries/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/countries/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(dataProvider.getAddress().getCity().getCountry().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(dataProvider.getAddress().getCity().getCountry().getCode()));
    }

    @Test
    public void save() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/countries"))
                .andExpect(status().isUnauthorized());

        Country country = dataProvider.getAddress().getCity().getCountry();
        country.setId(null);
        String newCountryName = "Armenia";
        String newCountryCode = "ARM";
        country.setName(newCountryName);
        country.setCode(newCountryCode);
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/countries")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(countryDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newCountryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(newCountryCode));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/countries/1"))
                .andExpect(status().isUnauthorized());

        Country country = countryDao.loadById(1L).get();
        Assertions.assertTrue(country.getName().equals(dataProvider.getAddress().getCity().getCountry().getName()));
        String newCountryName = "Armenia";
        country.setName(newCountryName);
        CountryDto countryDto = modelMapper.map(country, CountryDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/countries/1")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(countryDto)))
                .andExpect(status().isNoContent());
        country = countryDao.loadById(1L).get();
        Assertions.assertTrue(country.getName().equals(newCountryName));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/countries/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/countries/1")
                        .header("Authorization", renterJwtToken))
                .andExpect(status().isForbidden());

        Assertions.assertTrue(countryDao.loadById(1L).isPresent());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/countries/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(countryDao.loadById(1L).isEmpty());
    }
}
