package com.mamba.hotel_management.controller;

import com.mamba.hotel_management.model.Hotel;
import com.mamba.hotel_management.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public ResponseEntity<Page<Hotel>> getAllHotels(Pageable pageable) {
        Page<Hotel> hotels = hotelService.findAllHotels(pageable);
        return ResponseEntity.ok(hotels);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Hotel>> searchHotels(
            @RequestParam(required = false, defaultValue = "") String keyword,
            Pageable pageable) {
        Page<Hotel> hotels = hotelService.searchHotelsByKeyword(keyword, pageable);
        return ResponseEntity.ok(hotels); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable Long id) {
        Hotel hotel = hotelService.findHotelById(id);
        return ResponseEntity.ok(hotel);
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@RequestBody Hotel hotel) {
        Hotel createdHotel = hotelService.createHotel(hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
        Hotel updatedHotel = hotelService.updateHotel(id, hotel);
        return ResponseEntity.ok(updatedHotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
