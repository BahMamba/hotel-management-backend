package com.mamba.hotelmanagement.repository;

import com.mamba.hotelmanagement.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @NonNull
    Page<Reservation> findAll(Pageable pageable);

    List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByRoomHotelId(Long hotelId);
}