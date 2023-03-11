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
import org.example.rentapp.entities.Address_;
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
public class AddressDaoTest {

    private PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

    @Autowired
    private DataProvider dataProvider;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private FacilityDao facilityDao;

    @Autowired
    private OrderDao orderDao;

    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void before() {
        Address address = dataProvider.getAddress();
        address = addressDao.save(address);
        address = addressDao.update(address);

        em.flush();
        em.clear();
    }

    @Test
    public void loadByIdEagerAddress() {
        Address address = addressDao.loadByIdEager(1L).get();

        Assertions.assertNotNull(address);
        Assertions.assertTrue(persistenceUtil.isLoaded(addressDao, Address_.CITY));
    }

    @Test
    public void loadByIdAddress() {
        Address address = addressDao.loadById(1L).get();
        Assertions.assertFalse(persistenceUtil.isLoaded(address, Address_.CITY));
    }

    @Test
    public void saveAddress() {
        int size = addressDao.loadAll().size();
        Address loadedAddress = addressDao.loadAll().get(0);
        Address address = new Address();
        address.setCity(loadedAddress.getCity());
        address.setStreetName("Leninsky");
        address.setStreetNumber("12");
        address = addressDao.save(address);
        Assertions.assertNotNull(address.getId());
        Assertions.assertTrue(addressDao.loadAll().size() == size + 1);
    }

    @Test
    public void updateAddress() {
        Address address = addressDao.loadById(1L).get();
        address.setStreetName("BLK");
        address = addressDao.update(address);
        Assertions.assertEquals(address, addressDao.loadById(1L).get());
    }

    @Test
    public void deleteAddress() {
        Address address = addressDao.loadById(1L).get();
        int size = addressDao.loadAll().size();
        Assertions.assertTrue(size == 1);
        addressDao.delete(address);
        Assertions.assertTrue(addressDao.loadAll().isEmpty());
    }

    @Test
    public void deleteAddressesStartsWith() {
        Assertions.assertTrue(addressDao.loadAll().size() > 0);
        Address address = addressDao.loadAll().get(0);
        Address address1 = new Address();
        address1.setCity(address.getCity());
        address1.setStreetName("Leninsky");
        address1.setStreetNumber("12");
        addressDao.save(address1);
        int size = addressDao.loadAll().size();
        addressDao.deleteAddressesStartsWith("Len");
        Assertions.assertTrue(addressDao.loadAll().size() == size - 1);
    }

}
