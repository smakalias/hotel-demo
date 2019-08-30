package com.hotelcorp.data;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.emptyIterable;


@RunWith(SpringRunner.class)
@DataJpaTest
public class BookingRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookingRepository bookingRepository;

    private Hotel hotel;
    private Booking booking1, booking2;

    @Before
    public void setup() {
        hotel = new Hotel("Plaza", null, Byte.valueOf("4"));
        booking1 = new Booking("R2", "D2" , Short.valueOf("5"), BigDecimal.TEN, "EUR", hotel);
        booking2 = new Booking("R2", "D2", Short.valueOf("1"), BigDecimal.ONE, "USD", hotel);

        hotel = entityManager.persist(hotel);
        booking1 = entityManager.persist(booking1);
        booking2 = entityManager.persist(booking2);
        entityManager.flush();
    }

    @Test
    public void test_FindByHotelId_WithExistingHotelId_ShouldReturnBookingsForGivenHotelID() {
        final var bookings = bookingRepository.findByHotelId(hotel.getId());
        assertThat(bookings, containsInAnyOrder(booking1, booking2));
    }

    @Test
    public void test_FindByHotelName_WithExistingHotelName_ShouldReturnBookingsForGivenHotelName() {
        final var bookings = bookingRepository.findByHotelName(hotel.getName());
        assertThat(bookings, containsInAnyOrder(booking1, booking2));
    }

    @Test
    public void test_FindByHotelName_WithNonExistingHotelName_ShouldReturnEmptyList() {
        final var bookings = bookingRepository.findByHotelName("Does not exist");
        assertThat(bookings, emptyIterable());
    }

    @Test
    public void test_FindByCustomerLastName_WithExistingCustomer_ShouldReturnBookingsForGivenCustomerLastName() {
        final var bookings = bookingRepository.findByCustomerLastName(booking1.getCustomerLastName());
        assertThat(bookings, containsInAnyOrder(booking1, booking2));
    }

    @Test
    public void test_FindByCustomerLastName_WithNonExistingCustomer_ShouldReturnEmptyList() {
        final var bookings = bookingRepository.findByCustomerLastName("Does not exist");
        assertThat(bookings, emptyIterable());
    }

    @Test
    public void test_FindSumAmount_WithExistingHotelId_ShouldReturnSumPricesForGivenHotel() {
        final var expectedSumInEur = new BookingPriceStatistics("EUR", BigDecimal.TEN.setScale(3));
        final var expectedSumInUsd = new BookingPriceStatistics("USD", BigDecimal.ONE.setScale(3));

        final var bookingStats = bookingRepository.findSumAmountForHotel(hotel.getId());
        assertThat(bookingStats, containsInAnyOrder(expectedSumInEur, expectedSumInUsd));
    }

    @Test
    public void test_FindSumAmount_WithNonExistingHotelId_ShouldReturnEmptyList() {
        final var bookingStats = bookingRepository.findSumAmountForHotel(-1L);
        assertThat(bookingStats, emptyIterable());
    }
}
