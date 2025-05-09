package com.mamba.hotelmanagement.service;

import com.mamba.hotelmanagement.model.Hotel;
import com.mamba.hotelmanagement.model.Room;
import com.mamba.hotelmanagement.model.User;
import com.mamba.hotelmanagement.model.Role;
import com.mamba.hotelmanagement.repository.HotelRepository;
import com.mamba.hotelmanagement.repository.RoomRepository;
import com.mamba.hotelmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    public Page<Room> findRoomsByHotelId(Long hotelId, boolean availableOnly, Pageable pageable) {
        if (availableOnly) {
            return roomRepository.findByHotelIdAndIsAvailableTrue(hotelId, pageable);
        }
        return roomRepository.findByHotelId(hotelId, pageable);
    }

    public Room findRoomById(Long hotelId, Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));
        if (!room.getHotel().getId().equals(hotelId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room does not belong to this hotel");
        }
        return room;
    }

    public Room createRoom(Long hotelId, Room room, Long adminId) {
        if (room.getRoomNumber() == null || room.getType() == null || room.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room number, type, and price must not be null");
        }
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
        if (!admin.getRole().equals(Role.ADMIN_HOTEL)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must be an admin");
        }

        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    public Room updateRoom(Long hotelId, Long id, Room room, Long adminId) {
        Room existRoom = findRoomById(hotelId, id);
        if (room.getRoomNumber() == null || room.getType() == null || room.getPrice() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room number, type, and price must not be null");
        }
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
        if (!admin.getRole().equals(Role.ADMIN_HOTEL)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must be an admin");
        }

        existRoom.setRoomNumber(room.getRoomNumber());
        existRoom.setType(room.getType());
        existRoom.setPrice(room.getPrice());
        existRoom.setAvailable(room.isAvailable());
        return roomRepository.save(existRoom);
    }

    public void deleteRoom(Long hotelId, Long id, Long adminId) {
        Room room = findRoomById(hotelId, id);
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
        if (!admin.getRole().equals(Role.ADMIN_HOTEL)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User must be an admin");
        }
        roomRepository.delete(room);
    }
}