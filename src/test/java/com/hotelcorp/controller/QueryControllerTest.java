package com.hotelcorp.controller;

import com.hotelcorp.business.BookingService;
import com.hotelcorp.business.HotelService;
import com.hotelcorp.data.Booking;
import com.hotelcorp.data.Hotel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(QueryController.class)
public class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private HotelService hotelService;

    private Hotel hotel = new Hotel("Plaza", "address", null);

    @Test
    public void test_GetBookingsForHotel_WithHotelId_ShouldReturnBookingsForHotel() throws Exception {
        when(bookingService.getBookingsForHotel(any(Long.class)))
                .thenReturn(List.of(
                        new Booking("R2", "D2", (short) 5, BigDecimal.TEN, "EUR", hotel),
                        new Booking("C", "3Pio", (short) 3, BigDecimal.ONE, "USD", hotel))
                );

        mockMvc.perform(get("/queries/bookings").param("hotelId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", hasEntry("customerLastName", "D2")))
                .andExpect(jsonPath("$[1]", hasEntry("customerLastName", "3Pio")))
                .andReturn();
    }

    @Test
    public void test_GetBookingsForHotel_WithHotelName_ShouldReturnBookingsForHotel() throws Exception {
        when(bookingService.getBookingsForHotel(anyString()))
                .thenReturn(List.of(
                        new Booking("R2", "D2", (short) 5, BigDecimal.TEN, "EUR", hotel),
                        new Booking("C", "3Pio", (short) 3, BigDecimal.ONE, "USD", hotel))
                );

        mockMvc.perform(get("/queries/bookings").param("hotelName", "Plaza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", hasEntry("customerLastName", "D2")))
                .andExpect(jsonPath("$[1]", hasEntry("customerLastName", "3Pio")))
                .andReturn();
    }

    @Test
    public void test_GetBookingsForHotel_WithNonExistingHotel_ShouldReturnEmptyList() throws Exception {

        when(bookingService.getBookingsForHotel(anyString()))
                .thenReturn(List.of());

        mockMvc.perform(get("/queries/bookings").param("hotelName", "Plaza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andReturn();
    }


    @Test
    public void test_GetHotelsForCustomer_WithCustomerLastName_ShouldReturnHotelsForCustomer() throws Exception {
        var hotel1 = new Hotel("Hotel1", "address1", null);
        hotel1.setId(1L);
        var hotel2 = new Hotel("Hotel2", "address2", null);
        hotel2.setId(2L);

        when(bookingService.getBookingsForCustomer(anyString()))
                .thenReturn(List.of(
                        new Booking("R2", "D2", (short) 5, BigDecimal.TEN, "EUR", hotel1),
                        new Booking("R2", "D2", (short) 3, BigDecimal.ONE, "USD", hotel2),
                        new Booking("R2", "D2", (short) 1, BigDecimal.ONE, "USD", hotel1)));

        when(hotelService.getHotels(Set.of(1L, 2L)))
                .thenReturn(List.of(hotel1, hotel2));

        mockMvc.perform(get("/queries/hotels").param("customerLastName", "D2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", hasEntry("name", "Hotel1")))
                .andExpect(jsonPath("$[1]", hasEntry("name", "Hotel2")))
                .andReturn();
    }
}