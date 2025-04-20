package com.mamba.hotel_management.controller;

import com.mamba.hotel_management.model.Reservation;
import com.mamba.hotel_management.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<Reservation>> getAllReservations(Pageable pageable) {
        Page<Reservation> reservations = reservationService.findAllReservations(pageable);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<Reservation>> getReservationsByRoomId(@PathVariable Long roomId) {
        List<Reservation> reservations = reservationService.findReservationsByRoomId(roomId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Reservation>> searchReservations(
            @RequestParam(required = false, defaultValue = "") String customerName,
            @RequestParam(required = false) LocalDate checkInDate) {
        List<Reservation> reservations = reservationService.findReservationsByCustomerNameAndCheckInDate(customerName, checkInDate);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.findReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        Reservation reserv = reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok(reserv);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}