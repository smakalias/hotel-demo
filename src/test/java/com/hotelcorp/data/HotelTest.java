package com.hotelcorp.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class HotelTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void test_HotelInstantiation_WithInvalidRating_ShouldRaisesException() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid rating value: 10. Use values [1-5]");

        var hotel = new Hotel("Hilton", null, Byte.valueOf("10"));
    }

    @Test
    public void test_HotelInstantiation_WithNullRating_ShouldBeAccepted() {
        var hotel = new Hotel("Hilton", null, null);
        assertThat(hotel.getRating(), nullValue());
    }

    @Test
    public void test_HotelUpdate_WithInvalidRating_ShouldRaiseException() {
        var hotel = new Hotel("Hilton", null, Byte.valueOf("5"));

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid rating value: 0. Use values [1-5]");
        hotel.setRating(Byte.valueOf("0"));
    }

    @Test
    public void test_HotelUpdate_WithValidRating_ShouldBeAccepted() {
        var hotel = new Hotel("Hilton", null, Byte.valueOf("5"));
        final Byte aRating = Byte.valueOf("1");

        hotel.setRating(aRating);
        assertThat(hotel.getRating(), is(aRating));
    }

    @Test
    public void test_HotelUpdate_WithNullRating_ShouldBeAccepted() {
        var hotel = new Hotel("Hilton", null, Byte.valueOf("5"));
        hotel.setRating(null);
        assertThat(hotel.getRating(), nullValue());
    }

    @Test
    public void test_HotelUpdate_WithSourceContainingNullValues_ShouldNotOverrideNonNullValues() {
        var hotel = new Hotel("Hilton", "Syntagma Sq", Byte.valueOf("5"));
        final var upd = new Hotel("Hilton upd", null, null);

        hotel.updateNonNullValues(upd);
        assertThat(hotel.getAddress(), notNullValue());
        assertThat(hotel.getRating(), notNullValue());
    }
}