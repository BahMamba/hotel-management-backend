package com.mamba.hotel_management.repository;

import com.mamba.hotel_management.model.Hotel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @NonNull
    Page<Hotel> findAll(Pageable pageable);

    List<Hotel> findByNameContainingIgnoreCaseAndAddressContainingIgnoreCase(String name, String address);
    
}