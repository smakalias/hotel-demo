package com.hotelcorp.data;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class BookingTest {

    @Test
    public void test_BookingUpdate_WithSourceContainingNullValues_ShouldNotOverrideNonNullValues() {
        var booking = new Booking("R2",
                "D2",
                Short.valueOf("5"),
                BigDecimal.TEN,
                "EUR",
                new Hotel("Plaza", null, null));


        final var upd = new Booking(null, null, null, null, "USD", null);

        booking.updateNonNullValues(upd);
        assertThat(booking.getCustomerLastName(), notNullValue());
        assertThat(booking.getHotel(), notNullValue());
    }
}