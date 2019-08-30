package com.hotelcorp.controller;

import com.hotelcorp.business.BookingService;
import com.hotelcorp.data.Booking;
import com.hotelcorp.data.BookingPriceStatistics;
import com.hotelcorp.data.Hotel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static com.hotelcorp.utils.Utils.asJsonString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private Hotel hotel = new Hotel("Plaza", "address", null);

    @Test
    public void test_GetAllBookings_WithExistingBookings_ShouldReturnAllBookings() throws Exception {
        when(bookingService.getAllBookings())
                .thenReturn(List.of(
                        new Booking("R2", "D2", (short) 5, BigDecimal.TEN, "EUR", hotel),
                        new Booking("C", "3Pio", (short) 3, BigDecimal.ONE, "USD", hotel))
                );

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", hasEntry("customerLastName", "D2")))
                .andExpect(jsonPath("$[1]", hasEntry("customerLastName", "3Pio")))
                .andReturn();
    }


    @Test
    public void test_GetAllBookings_WithNoBookings_ShouldReturnEmptyList() throws Exception {

        when(bookingService.getAllBookings())
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }


    @Test
    public void test_GetSingleBooking_WithBookingExisting_ShouldReturnBooking() throws Exception {
        var booking = new Booking("R2", "D2", null, BigDecimal.ONE, "EUR", hotel);

        when(bookingService.getBookingById(any(Long.class)))
                .thenReturn(booking);

        mockMvc.perform(get("/bookings/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasEntry("customerLastName", "D2")))
                .andReturn();
    }


    @Test
    public void test_GetSingleBooking_WithNonExistingBooking_ShouldReturnStatusNotFound() throws Exception {

        when(bookingService.getBookingById(any(Long.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/bookings/{id}", 1))
                .andExpect(status().isNotFound())
                .andReturn();
    }


    @Test
    public void test_Post_WithNewBooking_ShouldReturnBooking() throws Exception {
        var booking = new Booking("R2", "D2", null, BigDecimal.ONE, "EUR", hotel);

        when(bookingService.createBooking(any(Booking.class)))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                .content(asJsonString(booking))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasEntry("customerLastName", "D2")))
                .andReturn();
    }

    @Test
    public void test_Post_WithExistingBooking_ShouldReturnStatusConflict() throws Exception {
        var booking = new Booking("R2", "D2", null, BigDecimal.ONE, "EUR", hotel);

        when(bookingService.createBooking(any(Booking.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        mockMvc.perform(post("/bookings")
                .content(asJsonString(booking))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void test_Put_WithExistingBooking_ShouldReturnUpdatedBooking() throws Exception {
        var booking = new Booking("R2", "D2", null, BigDecimal.ONE, "EUR", hotel);

        when(bookingService.updateBooking(any(Booking.class), any(Long.class)))
                .thenReturn(booking);

        mockMvc.perform(put("/bookings/{id}", 1)
                .content(asJsonString(booking))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasEntry("customerLastName", "D2")))
                .andReturn();
    }

    @Test
    public void test_Put_WithNonExistingBooking_ShouldReturnStatusNotFound() throws Exception {
        var booking = new Booking("R2", "D2", null, BigDecimal.ONE, "EUR", hotel);

        when(bookingService.updateBooking(any(Booking.class), any(Long.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(put("/bookings/{id}", 1)
                .content(asJsonString(booking))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void test_Delete_WithExistingBooking_ShouldDeleteBooking() throws Exception {
        doNothing()
                .when(bookingService).deleteBooking(any(Long.class));

        mockMvc.perform(delete("/bookings/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test_Delete_WithNonExistingBooking_ShouldReturnStatusNotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(bookingService).deleteBooking(any(Long.class));

        mockMvc.perform(delete("/bookings/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void test_GetBookingStatsForHotel_WithExistingHotel_ShouldReturnStats() throws Exception {
        when(bookingService.getBookingStatsForHotel(any(Long.class)))
                .thenReturn(List.of(
                        new BookingPriceStatistics("EUR", BigDecimal.TEN),
                        new BookingPriceStatistics("USD", BigDecimal.ONE))
                );

        mockMvc.perform(get("/bookings/stats").param("hotelId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", hasEntry("currency", "EUR")))
                .andExpect(jsonPath("$[0]", hasEntry("sumAmount", 10)))
                .andExpect(jsonPath("$[1]", hasEntry("currency", "USD")))
                .andExpect(jsonPath("$[1]", hasEntry("sumAmount", 1)))
                .andReturn();
    }

    @Test
    public void test_GetBookingStatsForHotel_WithNonExistingHotel_ShouldReturnEmptyList() throws Exception {
        when(bookingService.getBookingStatsForHotel(any(Long.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/bookings/stats").param("hotelId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }
}