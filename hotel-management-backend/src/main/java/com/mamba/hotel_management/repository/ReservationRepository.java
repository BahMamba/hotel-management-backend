package com.mamba.hotel_management.repository;

import com.mamba.hotel_management.model.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @NonNull
    Page<Reservation> findAll(Pageable pageable);

    List<Reservation> findByRoomId(Long roomId);

    List<Reservation> findByCustomerNameContainingIgnoreCaseAndCheckInDate(String customerName, LocalDate checkInDate);
}