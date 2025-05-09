package com.mamba.hotelmanagement.repository;

import com.mamba.hotelmanagement.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @NonNull
    Page<Room> findAll(Pageable pageable);

    Page<Room> findByHotelId(Long hotelId, Pageable pageable);

    Page<Room> findByHotelIdAndIsAvailableTrue(Long hotelId, Pageable pageable);

    List<Room> findByTypeAndIsAvailable(String type, boolean isAvailable);
}