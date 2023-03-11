package org.example.rentapp.daotests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUtil;
import jakarta.transaction.Transactional;
import org.example.rentapp.config.TestConfig;
import org.example.rentapp.configuration.ConfigClass;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.entities.Address;
import org.example.rentapp.entities.User;
import org.example.rentapp.entities.User_;
import org.example.rentapp.providers.DataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigClass.class, TestConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
public class UserDaoTest {

    private PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AddressDao addressDao;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void before() {
        Address address = dataProvider.getAddress();
        address = addressDao.save(address);
        address = addressDao.update(address);
        User user = dataProvider.getUser();
        user = userDao.save(user);
        user.setAddress(address);
        user = userDao.update(user);

        em.joinTransaction();
        em.flush();
        em.clear();
    }

    @Test
    public void loadByIdUser() {
        User user = userDao.loadById(1L).get();
        Assertions.assertFalse(persistenceUtil.isLoaded(user, User_.ADDRESS));
        Assertions.assertFalse(persistenceUtil.isLoaded(user, User_.ROLES));
        Assertions.assertNotNull(user.getId());
    }

    @Test
    public void saveUser() {
        Assertions.assertTrue(userDao.loadAll().size() == 1);
        User user = dataProvider.getUser();
        user.setEmail("newemail@mail.com");
        user = userDao.save(user);
        Assertions.assertTrue(userDao.loadAll().size() == 2);
        Assertions.assertNotNull(user.getId());
    }


    @Test
    public void loadByIdEagerUser() {
        User loadedUser = userDao.loadByIdEager(1L).get();

        Assertions.assertTrue(persistenceUtil.isLoaded(loadedUser, User_.ADDRESS));
        Assertions.assertTrue(persistenceUtil.isLoaded(loadedUser, User_.ROLES));
    }


    @Test
    public void deleteNonActiveUsers() {
        User user = userDao.loadAll().get(0);
        user.setStatus(false);
        user = userDao.update(user);
        Assertions.assertTrue(userDao.loadAll().size() == 1);
        userDao.deleteNonActiveUsers();
        Assertions.assertTrue(userDao.loadAll().size() == 0);
    }

    @Test
    public void updateUser() {
        User user = userDao.loadAll().get(0);
        user.setEmail("new email");
        user = userDao.update(user);
        User loadedUser = userDao.loadById(user.getId()).get();
        Assertions.assertEquals(loadedUser, user);
    }

    @Test
    public void deleteUser() {
        User user = userDao.loadAll().get(0);
        Assertions.assertFalse(userDao.loadAll().isEmpty());
        userDao.delete(user);
        Assertions.assertTrue(userDao.loadAll().isEmpty());
    }
}
