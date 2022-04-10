package com.epam.spsa.dao;

import com.epam.spsa.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
public class AddressDaoTest {

    @Autowired
    private AddressDao addressDao;

    @Test
    public void saveAddressTest() {
        Address address = getAddress();
        Address saved = addressDao.save(address);
        assertEquals(saved, address);
    }

    @Test
    public void getAddressByID() {
        int id = 1;
        Address returned = addressDao.getById(id);
        assertNotNull(returned);
    }

    @Test
    public void getAddressByUserIdTest() {
        int id = 1;
        Address address = addressDao.getByUserId(id);
        assertNotNull(address);
    }

    @Test
    public void getAddressByCoordinatesTest() {
        Address address = addressDao.getAll().get(0);
        List<Address> addresses = addressDao.getByCoordinates(address.getLatitude(), address.getLongitude());

        assertTrue(addresses.size() > 0);
        assertEquals(addresses.get(0), address);
    }

    private Address getAddress() {
        return Address.builder()
                .latitude(150)
                .longitude(78)
                .build();
    }

}