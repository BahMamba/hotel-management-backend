package com.mamba.hotel_management.repository;

import com.mamba.hotel_management.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @NonNull
    Page<Room> findAll(Pageable pageable);

    List<Room> findByHotelId(Long hotelId);

    List<Room> findByTypeAndIsAvailable(String type, boolean isAvailable);
}