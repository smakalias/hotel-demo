package com.hotelcorp.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HotelRepository extends CrudRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);
}
