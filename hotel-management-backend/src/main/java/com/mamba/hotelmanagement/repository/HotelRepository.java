package com.mamba.hotelmanagement.repository;

import com.mamba.hotelmanagement.model.Hotel;
import com.mamba.hotelmanagement.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
   Page<Hotel> findByAdminUser(User admin, Pageable pageable);
   Page<Hotel> findByCityContainingIgnoreCase(String city, Pageable pageable);
   
}