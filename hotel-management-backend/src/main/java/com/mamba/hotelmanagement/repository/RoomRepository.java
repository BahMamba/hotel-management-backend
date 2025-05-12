package com.mamba.hotelmanagement.repository;

import com.mamba.hotelmanagement.model.Hotel;
import com.mamba.hotelmanagement.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByHotel(Hotel hotel, Pageable pageable);
    Page<Room> findByTypeContainingIgnoreCase(String type, Pageable pageable);
    Page<Room> findByIsAvailable(boolean isAvailable, Pageable pageable);
    boolean existsByHotel(Hotel hotel);
}