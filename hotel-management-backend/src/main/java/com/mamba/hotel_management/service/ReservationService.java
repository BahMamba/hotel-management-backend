package com.mamba.hotel_management.service;

import com.mamba.hotel_management.model.Reservation;
import com.mamba.hotel_management.model.Room;
import com.mamba.hotel_management.repository.ReservationRepository;
import com.mamba.hotel_management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

        public Page<Reservation> findAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public List<Reservation> findReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public List<Reservation> findReservationsByCustomerNameAndCheckInDate(String customerName, LocalDate checkInDate) {
        return reservationRepository.findByCustomerNameContainingIgnoreCaseAndCheckInDate(customerName, checkInDate);
    }

    public Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RESERVATION NOT FOUND!"));
    }

    public Reservation createReservation(Reservation reservation) {
        if (reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID must not be null");
        }
        Room room = roomRepository.findById(reservation.getRoom().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ROOM NOT FOUND!"));
        if (!room.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ROOM IS NOT AVAILABLE");
        }

        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();
        if (checkIn != null && checkOut != null && checkOut.isBefore(checkIn)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out date must be after check-in date");
        }

        room.setAvailable(false);
        roomRepository.save(room);
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existReservation = findReservationById(id);
        if (reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID must not be null");
        }
        Room room = roomRepository.findById(reservation.getRoom().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();
        if (checkIn != null && checkOut != null && checkOut.isBefore(checkIn)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out date must be after check-in date");
        }

        existReservation.setCustomerName(reservation.getCustomerName());
        existReservation.setCustomerEmail(reservation.getCustomerEmail());
        existReservation.setCheckInDate(reservation.getCheckInDate());
        existReservation.setCheckOutDate(reservation.getCheckOutDate());
        existReservation.setRoom(room);
        return reservationRepository.save(existReservation);
    }

    public void deleteReservation(Long id) {
        Reservation reservation = findReservationById(id);
        Room room = reservation.getRoom();
        room.setAvailable(true);
        roomRepository.save(room);
        reservationRepository.delete(reservation);
    }
}