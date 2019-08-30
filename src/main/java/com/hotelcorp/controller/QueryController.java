package com.hotelcorp.controller;

import com.hotelcorp.business.BookingService;
import com.hotelcorp.business.HotelService;
import com.hotelcorp.data.Booking;
import com.hotelcorp.data.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/queries")
public class QueryController {

    private final BookingService bookingService;
    private final HotelService hotelService;

    @Autowired
    public QueryController(BookingService bookingService, HotelService hotelService) {
        this.bookingService = bookingService;
        this.hotelService = hotelService;
    }

    @GetMapping(value = "/bookings", params = "hotelId")
    public Iterable<Booking> getBookingsOfHotel(@RequestParam Long hotelId) {
        return bookingService.getBookingsForHotel(hotelId);
    }

    @GetMapping(value = "/bookings", params = "hotelName")
    public Iterable<Booking> getBookingsByHotel(@RequestParam String hotelName) {
        return bookingService.getBookingsForHotel(hotelName);
    }

    @GetMapping(value = "/hotels", params = "customerLastName")
    public Iterable<Hotel> getBookingsByCustomerLastName(@RequestParam("customerLastName") String lastName) {
        final var hotelIds = StreamSupport
                .stream(bookingService.getBookingsForCustomer(lastName).spliterator(), false)
                .map(Booking::getHotel)
                .map(Hotel::getId)
                .collect(Collectors.toSet());
        return hotelService.getHotels(hotelIds);
    }
}