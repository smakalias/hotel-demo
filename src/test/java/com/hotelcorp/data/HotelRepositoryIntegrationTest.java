package com.hotelcorp.data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@RunWith(SpringRunner.class)
@DataJpaTest
public class HotelRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    public void test_FindByName_ShouldReturnHotelWithGivenName() {
        final String hotelName = "Plaza";
        var hotel = new Hotel(hotelName, null, Byte.valueOf("3"));
        entityManager.clear();
        entityManager.persistAndFlush(hotel);

        final var optionalHotel = hotelRepository.findByName(hotelName);

        assertThat(optionalHotel.isPresent(), is(true));
        assertThat(optionalHotel.get().getName(), is(hotelName));
    }
    @Test
    public void test_FindByName_WithNonExistentName_ShouldReturnNull() {
        final var optionalHotel = hotelRepository.findByName("Does not exist");
        assertThat(optionalHotel.isEmpty(), is(true));
    }
}
