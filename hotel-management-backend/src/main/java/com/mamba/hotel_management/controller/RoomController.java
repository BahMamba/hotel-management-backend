package com.mamba.hotel_management.controller;

import com.mamba.hotel_management.model.Room;
import com.mamba.hotel_management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public ResponseEntity<Page<Room>> getAllRooms(Pageable pageable) {
        Page<Room> rooms = roomService.findAllRooms(pageable);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/by-hotel/{hotelId}")
    public ResponseEntity<Page<Room>> getRoomsByHotelId(@PathVariable Long hotelId, Pageable pageable) {
        Page<Room> rooms = roomService.findRoomsByHotelId(pageable, hotelId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Room>> searchRooms(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "true") boolean isAvailable) {
        List<Room> rooms = roomService.findRoomsByTypeAndAvailability(type, isAvailable);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        Room room = roomService.findRoomById(id);
        return ResponseEntity.ok(room);
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room createdRoom = roomService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room) {
        Room updatedRoom = roomService.updateRoom(id, room);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}