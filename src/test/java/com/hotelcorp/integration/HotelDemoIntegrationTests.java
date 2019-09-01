package com.hotelcorp.integration;

import com.hotelcorp.data.Booking;
import com.hotelcorp.data.Hotel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HotelDemoIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url;

    @Before
    public void setup() {
        url = String.format("http://localhost:%d", port);
    }

    @Test
    public void test_RetrieveCreateUpdateDelete_Hotel() {
        Long id = 5L;
        var hotel = new Hotel ("Plaza", "Plaza Address", Byte.valueOf("4"));
        hotel.setId(id);

        url += "/hotels";

        // post
        final var created = restTemplate.postForObject(url, hotel, Hotel.class);
        assertThat(created, is(hotel));

        url += "/" + id;

        // get
        final var retrieved = restTemplate.getForObject(url, Hotel.class);
        assertThat(retrieved, is(hotel));

        // put
        hotel.setAddress("Plaza Address upd");
        restTemplate.put(url, hotel);
        final var updated = restTemplate.getForObject(url, Hotel.class);
        assertThat(updated, is(hotel));

        // delete
        restTemplate.delete(url);
        final var deleted = restTemplate.getForObject(url, Hotel.class);
        assertThat(deleted.getId(), nullValue());
    }

    @Test
    public void test_RetrieveCreateUpdateDelete_Booking() {
        var hotel = restTemplate.getForObject(url + "/hotels/1", Hotel.class);

        Long id = 8L;
        var booking = new Booking("R2", "D2", (short) 3, BigDecimal.TEN.setScale(3), "EUR", hotel);
        booking.setId(id);

        url += "/bookings";

        // post
        final var created = restTemplate.postForObject(url, booking, Booking.class);
        assertThat(created, is(booking));

        url += "/" + id;

        // get
        final var retrieved = restTemplate.getForObject(url, Booking.class);
        assertThat(retrieved, is(booking));

        // put
        booking.setNumberOfPax((short) 5);
        restTemplate.put(url, booking);
        final var updated = restTemplate.getForObject(url, Booking.class);
        assertThat(updated, is(booking));

        // delete
        restTemplate.delete(url);
        final var deleted = restTemplate.getForObject(url, Booking.class);
        assertThat(deleted.getId(), nullValue());
    }

    @Test
    public void test_Retrieve_BookingsForHotel() {
        final var retrieved = restTemplate.getForObject(url + "queries/bookings?hotelId=1", Booking[].class);
        assertThat(Arrays.asList(retrieved), hasSize(4));
    }

    @Test
    public void test_Retrieve_HotelsForBookingsByCustomer() {
        final var retrieved = restTemplate.getForObject(url + "queries/hotels?customerLastName=Skywalker", Hotel[].class);
        assertThat(Arrays.asList(retrieved), hasSize(2));
    }

}
