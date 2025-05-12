package com.mamba.hotelmanagement.controller;

import java.util.Collections;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mamba.hotelmanagement.model.Room;
import com.mamba.hotelmanagement.service.RoomService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final PagedResourcesAssembler<Room> resourcesAssembler;

    @GetMapping
    public ResponseEntity<?> getRooms(@RequestParam(required = false) Long hotelId,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Boolean isAvailable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<Room> rooms = roomService.getRooms(hotelId, roomType, isAvailable, page, size);
        return ResponseEntity.ok(resourcesAssembler.toModel(rooms));
    }

    @PostMapping("/hotel/{id}")
    public ResponseEntity<?> addRoom(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Room room, @PathVariable Long id){
        try {
            Room savedRoom = roomService.addRoom(room, id, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "room creation failed!"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRoom(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long id, @RequestBody Room room){
        try {
            Room updatedRoom = roomService.updateRoom(id, room, userDetails);
            return ResponseEntity.ok(updatedRoom);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "room not updated"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        try {
            roomService.deleteRoom(id, userDetails);
            return ResponseEntity.ok("Room deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "room deletion failed!"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoom(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "room not found"));
        }
    }
}
