package org.example.rentapp.controllerstests;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.example.rentapp.controllers.UserController;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.dtos.UserCreationDto;
import org.example.rentapp.dtos.UserDto;
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
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private UserDao userDao;

    private String renterJwtToken;

    private String landlordJwtToken;

    private String adminJwtToken;

    @BeforeEach
    public void before() {
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
                        .get("/api/users/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(dataProvider.getUser().getEmail()));
    }

    @Test
    public void save() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users"))
                .andExpect(status().isUnauthorized());

        User user = dataProvider.getUser();
        String newEmail = "new_email@gmail.com";
        user.setEmail(newEmail);
        user.setId(null);
        UserCreationDto userDto = modelMapper.map(user, UserCreationDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/users")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(newEmail));
    }

    @Test
    public void update() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/users/1"))
                .andExpect(status().isUnauthorized());

        User user = userDao.loadByIdEager(1L).get();
        Assertions.assertTrue(user.getEmail().equals(dataProvider.getUser().getEmail()));
        String newEmail = "new_email@gmail.com";
        user.setEmail(newEmail);
        user.setRoles(dataProvider.getUser().getRoles());
        UserDto userDto = modelMapper.map(user, UserDto.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/users/1")
                        .header("Authorization", adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNoContent());
        user = userDao.loadById(1L).get();
        Assertions.assertTrue(user.getEmail().equals(newEmail));
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/users/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/users/1")
                        .header("Authorization", renterJwtToken))
                .andExpect(status().isForbidden());

        Assertions.assertTrue(userDao.loadById(1L).isPresent());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/users/1")
                        .header("Authorization", adminJwtToken))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(userDao.loadById(1L).isEmpty());
    }
}
