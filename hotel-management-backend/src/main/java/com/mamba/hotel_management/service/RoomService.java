package com.mamba.hotel_management.service;

import com.mamba.hotel_management.model.Hotel;
import com.mamba.hotel_management.model.Room;
import com.mamba.hotel_management.repository.HotelRepository;
import com.mamba.hotel_management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelRepository hotelRepository;


    public Page<Room> findAllRooms(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    public Page<Room> findRoomsByHotelId(Pageable pageable, Long hotelId) {
        return roomRepository.findByHotelId(hotelId, pageable);
    }

    public List<Room> findRoomsByTypeAndAvailability(String type, boolean isAvailable) {
        return roomRepository.findByTypeAndIsAvailable(type, isAvailable);
    }

    public Room findRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ROOM NOT FOUND!"));
    }

    public Room createRoom(Room room) {
        if (room.getHotel() == null || room.getHotel().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hotel ID must not be null");
        }
        Hotel hotel = hotelRepository.findById(room.getHotel().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));
        room.setHotel(hotel);
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room room) {
        Room existRoom = findRoomById(id);
        if (room.getHotel() == null || room.getHotel().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hotel ID must not be null");
        }
        Hotel hotel = hotelRepository.findById(room.getHotel().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));
        existRoom.setRoomNumber(room.getRoomNumber());
        existRoom.setType(room.getType());
        existRoom.setPrice(room.getPrice());
        existRoom.setAvailable(room.isAvailable());
        existRoom.setHotel(hotel);
        return roomRepository.save(existRoom);
    }

    public void deleteRoom(Long id) {
        Room existRoom = findRoomById(id);
        roomRepository.delete(existRoom);
    }
}