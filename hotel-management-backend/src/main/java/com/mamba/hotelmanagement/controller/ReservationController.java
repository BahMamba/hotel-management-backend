package com.mamba.hotelmanagement.controller;

import com.mamba.hotelmanagement.model.Reservation;
import com.mamba.hotelmanagement.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<Reservation>> getAllReservations(Pageable pageable) {
        return ResponseEntity.ok(reservationService.findAllReservations(pageable));
    }

    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<List<Reservation>> getReservationsByHotelId(
            @PathVariable Long hotelId,
            @RequestHeader("X-User-Id") Long adminId) {
        return ResponseEntity.ok(reservationService.findReservationsByHotelId(hotelId, adminId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.findReservationById(id));
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation,
            @RequestHeader("X-User-Id") Long clientId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(reservation, clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation reservation,
            @RequestHeader("X-User-Id") Long clientId) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservation, clientId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long clientId) {
        reservationService.deleteReservation(id, clientId);
        return ResponseEntity.noContent().build();
    }
}