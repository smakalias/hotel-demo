package com.hotelcorp.business;

import com.hotelcorp.data.Hotel;
import com.hotelcorp.data.HotelRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    @Autowired
    HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Iterable<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Iterable<Hotel> getHotels(Iterable<Long> ids) {
        return hotelRepository.findAllById(ids);
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> hotelNotFoundException(id));
    }

    public Hotel createHotel(Hotel hotel) {
        throwIfExists(hotel.getName());
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Hotel newHotel, Long id) {
        return hotelRepository.findById(id)
                .map(h -> {
                    // update but first check for name dupe
                    final String updName = newHotel.getName();
                    if (updName != null && !updName.equals(h.getName())) {
                        throwIfExists(updName);
                    }
                    h.updateNonNullValues(newHotel);
                    return hotelRepository.save(h);
                })
                .orElseThrow(() -> hotelNotFoundException(id));
    }

    public void deleteHotel(Long id) {
        if (!hotelRepository.existsById(id)) {
            throw hotelNotFoundException(id);
        }
        hotelRepository.deleteById(id);
    }

    Optional<Hotel> getHotelByIdOrName(@NonNull Hotel hotel) {
        if (hotel.getId() == null && hotel.getName() == null) {
            return Optional.empty();
        }
        return hotel.getId() != null
                ? hotelRepository.findById(hotel.getId())
                : hotelRepository.findByName(hotel.getName());
    }

    private void throwIfExists(@Nullable String hotelName) {
        if (hotelName != null &&
                hotelRepository.findByName(hotelName).isPresent()) {
            throw hotelAlreadyExistsException(hotelName);
        }
    }

    private RuntimeException hotelNotFoundException(Long id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel with id: " + id + " does not exist");
    }

    private RuntimeException hotelAlreadyExistsException(String name) {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Hotel : " + name + " already exists");
    }
}
