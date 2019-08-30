package com.hotelcorp.business;

import com.hotelcorp.data.Booking;
import com.hotelcorp.data.BookingPriceStatistics;
import com.hotelcorp.data.BookingRepository;
import com.hotelcorp.data.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class BookingService {

    private HotelService hotelService;
    private BookingRepository bookingRepository;

    @Autowired
    BookingService(HotelService hotelService, BookingRepository bookingRepository) {
        this.hotelService = hotelService;
        this.bookingRepository = bookingRepository;
    }

    public Iterable<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Iterable<Booking> getBookingsForCustomer(String lastName) {
        return bookingRepository.findByCustomerLastName(lastName);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> bookingNotFoundException(id));
    }

    public Booking createBooking(Booking booking) {
        Hotel bookingHotel = booking.getHotel();
        Optional<Hotel> optionalHotel = hotelService.getHotelByIdOrName(bookingHotel);

        Hotel hotel = optionalHotel.orElseGet(() -> hotelService.createHotel(bookingHotel)); // create hotel if new
        booking.setHotel(hotel);
        return bookingRepository.save(booking);
    }

    public Booking updateBooking(Booking newBooking, Long id) {
        return bookingRepository.findById(id)
                .map(b -> {
                    // update
                    if (newBooking.getHotel() != null) {
                        // check for valid hotel update info
                        Optional<Hotel> optionalHotel = hotelService.getHotelByIdOrName(newBooking.getHotel());
                        newBooking.setHotel(optionalHotel.orElse(null));
                    }
                    b.updateNonNullValues(newBooking);
                    return bookingRepository.save(b);
                })
                .orElseThrow(() -> bookingNotFoundException(id));
    }

    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw bookingNotFoundException(id);
        }
        bookingRepository.deleteById(id);
    }

    public Iterable<Booking> getBookingsForHotel(Long hotelId) {
        return bookingRepository.findByHotelId(hotelId);
    }

    public Iterable<Booking> getBookingsForHotel(String hotelName) {
        return bookingRepository.findByHotelName(hotelName);
    }

    public Iterable<BookingPriceStatistics> getBookingStatsForHotel(Long hotelId) {
        return bookingRepository.findSumAmountForHotel(hotelId);
    }

    private RuntimeException bookingNotFoundException(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking with id: " + id + " does not exist");
    }
}
