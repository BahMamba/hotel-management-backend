package com.mamba.hotel_management.service;

import com.mamba.hotel_management.model.Hotel;
import com.mamba.hotel_management.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    public Page<Hotel> findAllHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable);
    }

    public List<Hotel> findHotelsByNameAndAddress(String name, String address) {
        return hotelRepository.findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(name, address);
    }

    public Hotel findHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "HOTEL NOT FOUND!"));
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel hotel) {
        Hotel existHotel = findHotelById(id);
        existHotel.setName(hotel.getName());
        existHotel.setCity(hotel.getCity());
        existHotel.setAddress(hotel.getAddress());
        return hotelRepository.save(existHotel);
    }

    public void delete(Long id) {
        Hotel existingHotel = findHotelById(id);
        hotelRepository.delete(existingHotel);
    }
}