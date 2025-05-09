package com.mamba.hotelmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mamba.hotelmanagement.model.Hotel;
import com.mamba.hotelmanagement.model.Role;
import com.mamba.hotelmanagement.model.User;
import com.mamba.hotelmanagement.repository.HotelRepository;
import com.mamba.hotelmanagement.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HotelService {

    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    public Hotel createHotel(Hotel hotel, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if (user.getRole() == null || !Role.ADMIN_HOTEL.equals(user.getRole())) {
            throw new RuntimeException("Only Admin can create Hotel!");
        }
        hotel.setAdminUser(user);
        return hotelRepository.save(hotel);
    }

    public Hotel updateHotel(Long id, Hotel updateHotel, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if (user.getRole() == null || !Role.ADMIN_HOTEL.equals(user.getRole())) {
            throw new RuntimeException("Only Admin can update this Hotel!");
        }
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found!"));
        if (!hotel.getAdminUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only Admin can update this Hotel!");
        }
        hotel.setName(updateHotel.getName());
        hotel.setAddress(updateHotel.getAddress());
        hotel.setCity(updateHotel.getCity());
        hotel.setDescription(updateHotel.getDescription());
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id, UserDetails userDetails) {
        User admin = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found!"));
        if (admin.getRole() == null || !Role.ADMIN_HOTEL.equals(admin.getRole())) {
            throw new RuntimeException("Only Admin can delete this Hotel!");
        }
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found!"));
        if (!hotel.getAdminUser().getId().equals(admin.getId())) {
            throw new RuntimeException("Only Admin can delete this Hotel!");
        }
        hotelRepository.delete(hotel);
    }

    public Page<Hotel> getHotelByAdmin(UserDetails userDetails, int page, int size) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Admin not found!"));
        if (user.getRole() == null || !Role.ADMIN_HOTEL.equals(user.getRole())) {
            throw new RuntimeException("Only Admin can view this Hotel!");
        }
        Pageable pageable = PageRequest.of(page, size);
        return hotelRepository.findByAdminUser(user, pageable);
    }

    public Page<Hotel> getAllHotels(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (city != null && !city.isEmpty()) {
            return hotelRepository.findByCityContainingIgnoreCase(city, pageable);
        }
        return hotelRepository.findAll(pageable);
    }

    public Hotel getHotelById(Long id, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        if (user.getRole() == null || !Role.ADMIN_HOTEL.equals(user.getRole())) {
            throw new RuntimeException("Only Admin can view this Hotel!");
        }
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found!"));
        if (!hotel.getAdminUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only Admin can view this Hotel!");
        }
        return hotel;
    }
}