package com.mamba.hotelmanagement.controller;

import com.mamba.hotelmanagement.model.Hotel;
import com.mamba.hotelmanagement.service.HotelService;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final PagedResourcesAssembler<Hotel> pagedResourcesAssembler;

    @PostMapping
    public ResponseEntity<?> createHotel(@RequestBody Hotel hotel, @AuthenticationPrincipal UserDetails userDetails){
        try {
            hotelService.createHotel(hotel, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("message", "Hotel Created!"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel, @AuthenticationPrincipal UserDetails userDetails){
        try {
            hotelService.updateHotel(id, hotel, userDetails);
            return ResponseEntity.ok(Collections.singletonMap("message", "Hotel updated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            hotelService.deleteHotel(id, userDetails);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonMap("message", "Hotel deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<PagedModel<EntityModel<Hotel>>> getHotelsByAdmin(@RequestParam(defaultValue = "0") int page,
                                                                            @RequestParam(defaultValue = "10") int size,
                                                                            @AuthenticationPrincipal UserDetails userDetails
                                                                            ){
        try {
            Page<Hotel> hotels = hotelService.getHotelByAdmin(userDetails, page, size);
            return ResponseEntity.ok(pagedResourcesAssembler.toModel(hotels));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(pagedResourcesAssembler.toModel(Page.empty()));
        }
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<Hotel>>> getAllHotels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String city) {
        Page<Hotel> hotels = hotelService.getAllHotels(city, page, size);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(hotels));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Hotel hotel = hotelService.getHotelById(id, userDetails);
            return ResponseEntity.ok(hotel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    } 
}