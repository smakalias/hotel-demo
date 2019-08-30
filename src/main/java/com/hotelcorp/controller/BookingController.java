package com.hotelcorp.controller;

import com.hotelcorp.business.BookingService;
import com.hotelcorp.data.Booking;
import com.hotelcorp.data.BookingPriceStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public Iterable<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping(value = "/{id}")
    public Booking getBooking(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    @PutMapping(value = "/{id}")
    public Booking updateBooking(@RequestBody Booking newBooking, @PathVariable Long id) {
        return bookingService.updateBooking(newBooking, id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
    }

    @GetMapping(value = "/stats", params = "hotelId")
    public Iterable<BookingPriceStatistics> getBookingStatsForHotel(@RequestParam Long hotelId) {
        return bookingService.getBookingStatsForHotel(hotelId);
    }
}