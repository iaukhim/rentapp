package org.example.rentapp.controllerstests;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.rentapp.controllers.CityController;
import org.example.rentapp.dao.interfaces.CityDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.CityDto;
import org.example.rentapp.entities.City;
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

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CityControllerTest {

    @Autowired
    private CityController cityController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDao userDao;

    private String renterJwtToken;

    private String adminJwtToken;

    private String landlordJwtToken;

    @BeforeEach
    public void before() {
        City city = dataProvider.getAddress().getCity();
        city = cityDao.save(city);
        city = cityDao.update(city);

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
                        .get("/api/cities/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/cities/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(dataProvider.getAddress().getCity().getName()));
    }

    @Test
    public void save() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/cities"))
                .andExpect(status().isUnauthorized());

        City city = dataProvider.getAddress().getCity();
        city.getCountry().setId(null);
        city.getCountry().setCode("RUS");
        city.getCountry().setName("RUSSIA");
        city.setId(null);
        String newCityName = "Minsk";
        city.setName(newCityName);
        CityDto cityDto = modelMapper.map(city, CityDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/cities")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newCityName));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/cities/1"))
                .andExpect(status().isUnauthorized());

        City city = cityDao.loadById(1L).get();
        Assertions.assertTrue(city.getName().equals(dataProvider.getAddress().getCity().getName()));
        String newCityName = "Minsk";
        city.setName(newCityName);
        CityDto cityDto = modelMapper.map(city, CityDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/cities/1")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityDto)))
                .andExpect(status().isNoContent());
        city = cityDao.loadById(1L).get();
        Assertions.assertTrue(city.getName().equals(newCityName));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/cities/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/cities/1")
                        .header("Authorization", renterJwtToken))
                .andExpect(status().isForbidden());

        Assertions.assertTrue(cityDao.loadById(1L).isPresent());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/cities/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(cityDao.loadById(1L).isEmpty());
    }
}
