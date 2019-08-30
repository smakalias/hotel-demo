package com.hotelcorp.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends CrudRepository<Booking, Long> {
    Iterable<Booking> findByHotelId(Long hotelId);
    Iterable<Booking> findByHotelName(String hotelName);
    Iterable<Booking> findByCustomerLastName(String lastName);

    @Query("SELECT new com.hotelcorp.data.BookingPriceStatistics(currency, SUM(price)) " +
            "FROM Booking " +
            "WHERE hotel_id = :hotelId " +
            "GROUP BY currency")
    Iterable<BookingPriceStatistics> findSumAmountForHotel(@Param("hotelId") Long hotelId);
}
