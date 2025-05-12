package com.mamba.hotelmanagement.controller;

import com.mamba.hotelmanagement.model.Room;
import com.mamba.hotelmanagement.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/rooms")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/hotel/{id}")
    public ResponseEntity<Room> addRoom(@RequestBody Room room, @PathVariable("id") Long hotelId, @AuthenticationPrincipal UserDetails userDetails) {
        Room newRoom = roomService.addRoom(room, hotelId, userDetails);
        return ResponseEntity.status(201).body(newRoom);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room room, @AuthenticationPrincipal UserDetails userDetails) {
        Room updatedRoom = roomService.updateRoom(id, room, userDetails);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        roomService.deleteRoom(id, userDetails);
        return ResponseEntity.ok(Collections.singletonMap("message", "Room deleted successfully"));
    }

    @GetMapping
    public ResponseEntity<?> getRooms(
            @RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Room> rooms = roomService.getRooms(hotelId, roomType, isAvailable, page, size);
        if (hotelId != null) {
            if (rooms == null) {
                return ResponseEntity.status(404)
                        .body(Collections.singletonMap("message", "Hotel not found!"));
            }
            if (rooms.getContent().isEmpty()) {
                return ResponseEntity.ok()
                        .body(Collections.singletonMap("message", "Rooms not added yet!"));
            }
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return ResponseEntity.ok(room);
    }
}