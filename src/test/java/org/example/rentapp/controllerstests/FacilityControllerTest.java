package org.example.rentapp.controllerstests;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.rentapp.controllers.FacilityController;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dao.interfaces.FacilityDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.FacilityDto;
import org.example.rentapp.entities.Facility;
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
public class FacilityControllerTest {

    @Autowired
    private FacilityController facilityController;

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
    private JwtService jwtService;

    private String renterJwtToken;

    private String adminJwtToken;

    private String landlordJwtToken;

    @BeforeEach
    public void before() {
        Facility facility = dataProvider.getFacility();
        facility.setAddress(addressDao.save(addressDao.save(dataProvider.getAddress())));
        facility.setOwner(userDao.save(dataProvider.getUser()));
        facility = facilityDao.save(facility);
        facility = facilityDao.update(facility);

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
                        .get("/api/facilities/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/facilities/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(dataProvider.getFacility().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.space").value(dataProvider.getFacility().getSpace()));
    }

    @Test
    public void save() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/facilities/"))
                .andExpect(status().isUnauthorized());

        Facility facility = dataProvider.getFacility();
        facility.setAddress(addressDao.loadById(1L).get());
        facility.setOwner(userDao.loadById(1L).get());
        String newName = "new_facility_name";
        facility.setName(newName);
        facility.setId(null);
        FacilityDto facilityDto = modelMapper.map(facility, FacilityDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/facilities")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facilityDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(newName));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/facilities/1"))
                .andExpect(status().isUnauthorized());

        Facility facility = facilityDao.loadByIdEager(1L).get();
        Assertions.assertEquals(facility.getName(), dataProvider.getFacility().getName());
        String newName = "new_facility_name";
        facility.setName(newName);
        FacilityDto facilityDto = modelMapper.map(facility, FacilityDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/facilities/1")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facilityDto)))
                .andExpect(status().isNoContent());
        facility = facilityDao.loadById(1L).get();
        Assertions.assertEquals(facility.getName(), newName);
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/facilities/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/facilities/1")
                        .header("Authorization", renterJwtToken))
                .andExpect(status().isForbidden());

        Assertions.assertTrue(facilityDao.loadById(1L).isPresent());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/facilities/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(facilityDao.loadById(1L).isEmpty());
    }
}
