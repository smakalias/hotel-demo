package com.hotelcorp.controller;

import com.hotelcorp.business.HotelService;
import com.hotelcorp.data.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;

    @Autowired
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public Iterable<Hotel> getAllHotels() {
        return hotelService.getAllHotels();
    }


    @GetMapping(value = "/{id}")
    public Hotel getHotel(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @PostMapping
    public Hotel createHotel(@RequestBody Hotel hotel) {
        return hotelService.createHotel(hotel);
    }

    @PutMapping(value = "/{id}")
    public Hotel updateHotel(@RequestBody Hotel newHotel, @PathVariable Long id) {
        return hotelService.updateHotel(newHotel, id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
    }
}
