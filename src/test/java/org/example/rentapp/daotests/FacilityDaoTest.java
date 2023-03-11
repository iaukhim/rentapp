package org.example.rentapp.daotests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUtil;
import jakarta.transaction.Transactional;
import org.example.rentapp.config.TestConfig;
import org.example.rentapp.configuration.ConfigClass;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dao.interfaces.FacilityDao;
import org.example.rentapp.dao.interfaces.OrderDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.entities.Address;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.entities.Facility_;
import org.example.rentapp.entities.Order;
import org.example.rentapp.entities.User;
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

import java.util.ArrayList;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ConfigClass.class, TestConfig.class})
@ActiveProfiles("test")
@WebAppConfiguration
public class FacilityDaoTest {

    private PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Autowired
    DataProvider dataProvider;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private FacilityDao facilityDao;

    @Autowired
    private OrderDao orderDao;

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

        Facility facility = dataProvider.getFacility();
        facility.setOwner(user);
        facility.setAddress(address);
        facility = facilityDao.update(facility);

        Order order = dataProvider.getOrder();
        order.setRenter(user);
        order.setFacility(facility);
        order = orderDao.update(order);

        em.joinTransaction();
        em.flush();
        em.clear();
    }

    @Test
    public void findLargestFacility() {
        Facility facility = new Facility();
        facility.setName("zal2");
        facility.setSpace(8999999D);
        Address address = addressDao.loadById(1L).get();
        facility.setAddress(address);
        User user = userDao.loadById(1L).get();
        facility.setOwner(user);
        Assertions.assertTrue(facilityDao.loadAll().size() > 0);
        facility = facilityDao.save(facility);
        facility = facilityDao.update(facility);
        Facility largestFacility = facilityDao.findLargestFacility();
        Assertions.assertEquals(facility, largestFacility);
    }

    @Test
    public void loadByIdEagerFacility() {
        Facility facility = facilityDao.loadByIdEager(1L).get();
        Assertions.assertTrue(persistenceUtil.isLoaded(facility, Facility_.ADDRESS));
        Assertions.assertTrue(persistenceUtil.isLoaded(facility, Facility_.OWNER));
        Assertions.assertTrue(persistenceUtil.isLoaded(facility, Facility_.ORDERS));
    }

    @Test
    public void loadByIdFacility() {
        Facility facility = facilityDao.loadById(1L).get();

        Assertions.assertNotNull(facility);
        Assertions.assertFalse(persistenceUtil.isLoaded(facility, Facility_.ORDERS));
        Assertions.assertFalse(persistenceUtil.isLoaded(facility, Facility_.OWNER));
        Assertions.assertFalse(persistenceUtil.isLoaded(facility, Facility_.ADDRESS));
    }

    @Test
    public void saveFacility() {
        int size = facilityDao.loadAll().size();
        Facility facility = dataProvider.getFacility();
        facility.setName("zal2");
        facility.setOwner(userDao.loadById(1L).get());
        facility.setAddress(addressDao.loadById(1L).get());
        facility = facilityDao.save(facility);
        Order order = orderDao.loadById(1L).get();
        em.detach(order);
        order.setId(null);
        order.setFacility(facility);
        order = orderDao.save(order);
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(order);
        facility.setOrders(orders);

        facility = facilityDao.update(facility);

        Assertions.assertNotNull(facility.getId());
        Assertions.assertTrue(facilityDao.loadAll().size() == size + 1);
    }

    @Test
    public void updateFacility() {
        Facility facility = facilityDao.loadById(1L).get();
        em.detach(facility);
        facility.setName("new zal");
        Facility updatedFacility = facilityDao.update(facility);
        Assertions.assertEquals(facility, updatedFacility);
    }

    @Test
    public void deleteFacility() {
        int size = facilityDao.loadAll().size();
        Facility facility = facilityDao.loadById(1L).get();
        facilityDao.delete(facility);
        Assertions.assertTrue(facilityDao.loadAll().size() == size - 1);
    }

}
