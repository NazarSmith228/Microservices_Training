package com.epam.slsa.dao;

import com.epam.slsa.builders.locationType.LocationTypeBuilder;
import com.epam.slsa.dao.impl.AddressDaoImpl;
import com.epam.slsa.dao.impl.LocationDaoImpl;
import com.epam.slsa.dao.impl.LocationTypeDaoImpl;
import com.epam.slsa.model.Address;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.LocationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.slsa.builders.address.AddressBuilder.getAddress;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationWithoutId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@Rollback
public class AddressDaoTest {

    @Autowired
    private AddressDaoImpl addressDao;

    @Autowired
    private LocationDaoImpl locationDao;

    @Autowired
    private LocationTypeDaoImpl locationTypeDao;

    @Test
    public void saveTest() {
        Address address = getAddress();
        Address newAddress = addressDao.save(address);

        assertEquals(address, newAddress);
    }

    @Test
    public void updateTest() {
        Address address = getAddress();
        Address newAddress = addressDao.save(address);
        newAddress.setLatitude(132.4);
        Address updateAddress = addressDao.update(address);

        assertEquals(updateAddress, newAddress);
    }

    @Test
    public void deleteTest() {
        Address address = getAddress();
        Address newAddress = addressDao.save(address);
        int id = newAddress.getId();
        addressDao.delete(newAddress);

        assertNull(addressDao.getById(id));
    }

    @Test
    public void getByIdTest() {
        Address address = getAddress();
        Address newAddress = addressDao.save(address);
        int id = newAddress.getId();
        Address getByIdAddress = addressDao.getById(id);

        assertEquals(newAddress, getByIdAddress);
    }

    @Test
    public void getAllTest() {
        Address address = getAddress();
        int sizeBefore = addressDao.getAll().size();
        addressDao.save(address);
        int sizeAfter = addressDao.getAll().size();

        assertEquals(1, sizeAfter - sizeBefore);
    }

    @Test
    public void getByCoordinatesTest() {
        Address address = getAddress();
        addressDao.save(address);
        Address getByCoordinatesAddress = addressDao.getByCoordinates(address.getLatitude(), address.getLongitude());

        assertEquals(address, getByCoordinatesAddress);
    }

    @Test
    public void getByLocationIdTest() {
        LocationType locationType = LocationTypeBuilder.getLocationTypeWithoutId();
        LocationType saveLocationType = locationTypeDao.save(locationType);
        Address address = Address.builder()
                .location(null)
                .longitude(120)
                .latitude(140)
                .build();
        Location location = getLocationWithoutId();
        location.setLocationType(saveLocationType);
        location.setAddress(address);
        Location saveLocation = locationDao.save(location);
        int locationId = saveLocation.getId();
        address.setLocation(saveLocation);

        Address saveAddress = addressDao.save(address);
        Address addressByLocationId = addressDao.getByLocationId(locationId);

        assertEquals(saveAddress, addressByLocationId);
    }

}
