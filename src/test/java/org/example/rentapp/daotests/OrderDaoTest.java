package org.example.rentapp.daotests;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUtil;
import jakarta.transaction.Transactional;
import org.example.rentapp.dao.interfaces.AddressDao;
import org.example.rentapp.dao.interfaces.FacilityDao;
import org.example.rentapp.dao.interfaces.OrderDao;
import org.example.rentapp.dao.interfaces.UserDao;
import org.example.rentapp.entities.Address;
import org.example.rentapp.entities.Facility;
import org.example.rentapp.entities.Order;
import org.example.rentapp.entities.Order_;
import org.example.rentapp.entities.User;
import org.example.rentapp.providers.DataProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
public class OrderDaoTest {

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
    public void deleteExpiredOrders() {
        Order order = dataProvider.getOrder();
        order.setFacility(facilityDao.loadById(1L).get());
        order.setRenter(userDao.loadById(1L).get());
        order.setPlannedDate(LocalDate.now().minusWeeks(3));

        int size = orderDao.loadAll().size();
        Assertions.assertTrue(size > 0);

        order = orderDao.save(order);

        Assertions.assertTrue(orderDao.loadAll().size() == size + 1);
        orderDao.deleteExpiredOrders();
        Order loadedOrder = orderDao.loadAll().get(0);
        Assertions.assertTrue(loadedOrder.getPlannedDate().compareTo(LocalDate.now()) > 0);
        Assertions.assertTrue(orderDao.loadAll().size() == size);
    }

    @Test
    public void loadByIdEagerOrder() {
        Order order = orderDao.loadByIdEager(1L).get();

        Assertions.assertTrue(persistenceUtil.isLoaded(order, Order_.FACILITY));
        Assertions.assertTrue(persistenceUtil.isLoaded(order, Order_.RENTER));
    }

    @Test
    public void loadByIdOrder() {
        Order order = orderDao.loadById(1L).get();

        Assertions.assertNotNull(order);
        Assertions.assertFalse(persistenceUtil.isLoaded(order, Order_.RENTER));
        Assertions.assertFalse(persistenceUtil.isLoaded(order, Order_.FACILITY));
    }

    @Test
    public void updateOrder() {
        Order order = orderDao.loadById(1L).get();
        em.detach(order);
        order.setPlannedDate(LocalDate.now().minusYears(35));
        Order updatedOrder = orderDao.update(order);
        Assertions.assertEquals(order, updatedOrder);
    }

    @Test
    public void deleteOrder() {
        int size = orderDao.loadAll().size();
        Order order = orderDao.loadById(1L).get();
        orderDao.delete(order);
        Assertions.assertTrue(orderDao.loadAll().size() == size - 1);
    }

    @Test
    public void saveOrder() {
        int size = orderDao.loadAll().size();
        Order order = dataProvider.getOrder();
        order.setRenter(userDao.loadById(1L).get());
        order.setFacility(facilityDao.loadById(1L).get());
        order.setCreationDate(LocalDate.now());
        order.setCreationDate(LocalDate.now().plusMonths(1));
        order = orderDao.save(order);
        Assertions.assertNotNull(order.getId());
        Assertions.assertTrue(orderDao.loadAll().size() == size + 1);
    }
}
