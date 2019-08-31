package com.hotelcorp.business;

import com.hotelcorp.data.Booking;
import com.hotelcorp.data.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookingHotelService {

    private HotelService hotelService;

    private BookingService bookingService;

    @Autowired
    public BookingHotelService(HotelService hotelService, BookingService bookingService) {
        this.hotelService = hotelService;
        this.bookingService = bookingService;
    }

    public Iterable<Hotel> getHotelsForBookingsByCustomerLastName(String customerLastName) {
        final var hotelIds = StreamSupport
                .stream(bookingService.getBookingsForCustomer(customerLastName).spliterator(), false)
                .map(Booking::getHotel)
                .map(Hotel::getId)
                .collect(Collectors.toSet());
        return hotelService.getHotels(hotelIds);
    }
}
