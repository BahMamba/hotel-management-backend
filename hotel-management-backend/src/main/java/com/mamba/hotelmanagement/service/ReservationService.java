package com.mamba.hotelmanagement.service;

import com.mamba.hotelmanagement.model.Reservation;
import com.mamba.hotelmanagement.model.Room;
import com.mamba.hotelmanagement.model.User;
import com.mamba.hotelmanagement.model.Role;
import com.mamba.hotelmanagement.repository.ReservationRepository;
import com.mamba.hotelmanagement.repository.RoomRepository;
import com.mamba.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public Page<Reservation> findAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }

    public List<Reservation> findReservationsByHotelId(Long hotelId, Long adminId) {
        return reservationRepository.findByRoomHotelId(hotelId);
    }

    public List<Reservation> findReservationsByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public Reservation findReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    public Reservation createReservation(Reservation reservation, Long clientId) {
        if (reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID must not be null");
        }
        if (reservation.getClient() == null || reservation.getClient().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client ID must not be null");
        }
        Room room = roomRepository.findById(reservation.getRoom().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        User client = userRepository.findById(reservation.getClient().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        if (!client.getRole().equals(Role.CLIENT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must be a client");
        }
        if (!room.isAvailable()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room is not available");
        }
        if (room.getRoomNumber() == null || room.getType() == null || room.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Room data is incomplete");
        }

        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();
        if (checkIn == null || checkOut == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-in and check-out dates must not be null");
        }
        if (checkOut.isBefore(checkIn)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out date must be after check-in date");
        }

        room.setAvailable(false);
        roomRepository.save(room);
        reservation.setRoom(room);
        reservation.setClient(client);
        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(Long id, Reservation reservation, Long clientId) {
        Reservation existReservation = findReservationById(id);
        if (reservation.getRoom() == null || reservation.getRoom().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room ID must not be null");
        }
        if (reservation.getClient() == null || reservation.getClient().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client ID must not be null");
        }
        Room room = roomRepository.findById(reservation.getRoom().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        User client = userRepository.findById(reservation.getClient().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        if (!client.getRole().equals(Role.CLIENT)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must be a client");
        }

        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();
        if (checkIn != null && checkOut != null && checkOut.isBefore(checkIn)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out date must be after check-in date");
        }

        existReservation.setCheckInDate(reservation.getCheckInDate());
        existReservation.setCheckOutDate(reservation.getCheckOutDate());
        existReservation.setRoom(room);
        existReservation.setClient(client);
        return reservationRepository.save(existReservation);
    }

    public void deleteReservation(Long id, Long clientId) {
        Reservation reservation = findReservationById(id);
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        if (!reservation.getClient().getId().equals(clientId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the client can cancel their reservation");
        }
        Room room = reservation.getRoom();
        room.setAvailable(true);
        roomRepository.save(room);
        reservationRepository.delete(reservation);
    }
}