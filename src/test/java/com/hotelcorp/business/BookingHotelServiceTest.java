package com.hotelcorp.business;

import com.hotelcorp.data.Booking;
import com.hotelcorp.data.Hotel;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyIterable;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = NONE)
public class BookingHotelServiceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private BookingService mockBookingService;

    @Mock
    private HotelService mockHotelService;

    // service under test
    private BookingHotelService bookingHotelService;

    @Before
    public void setUp() throws Exception {
        bookingHotelService = new BookingHotelService(mockHotelService, mockBookingService);
    }

    @Test
    public void test_GetHotelsForBookingByCustomerName_WithMatchingBookings_ShouldReturnHotelsForMatchingBookings() {
        var hotel1 = new Hotel("Hotel1", "address1", null);
        hotel1.setId(1L);
        var hotel2 = new Hotel("Hotel2", "address2", null);
        hotel2.setId(2L);

        when(mockBookingService.getBookingsForCustomer(anyString()))
                .thenReturn(List.of(
                        new Booking("R2", "D2", (short) 5, BigDecimal.TEN, "EUR", hotel1),
                        new Booking("R2", "D2", (short) 3, BigDecimal.ONE, "USD", hotel2),
                        new Booking("R2", "D2", (short) 1, BigDecimal.ONE, "USD", hotel1)));

        when(mockHotelService.getHotels(Set.of(1L, 2L)))
                .thenReturn(List.of(hotel1, hotel2));

        Iterable<Hotel> hotels = bookingHotelService.getHotelsForBookingsByCustomerLastName("D2");

        assertThat(hotels, contains(hotel1, hotel2));
    }

    @Test
    public void test_GetHotelsForBookingByCustomerName_WithNonMatchingBookings_ShouldReturnEmptyList() {
        when(mockBookingService.getBookingsForCustomer(anyString()))
                .thenReturn(List.of());

        when(mockHotelService.getHotels(Set.of()))
                .thenReturn(List.of());

        Iterable<Hotel> hotels = bookingHotelService.getHotelsForBookingsByCustomerLastName("D2");

        assertThat(hotels, emptyIterable());
    }
}