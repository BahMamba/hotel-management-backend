// package com.mamba.hotelmanagement.controller;

// import com.mamba.hotelmanagement.model.Room;
// import com.mamba.hotelmanagement.service.RoomService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/hotels/{hotelId}/rooms")
// @RequiredArgsConstructor
// public class RoomController {
//     private final RoomService roomService;

//     @GetMapping
//     public ResponseEntity<Page<Room>> getRoomsByHotelId(
//             @PathVariable Long hotelId,
//             @RequestParam(defaultValue = "false") boolean availableOnly,
//             Pageable pageable) {
//         return ResponseEntity.ok(roomService.findRoomsByHotelId(hotelId, availableOnly, pageable));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Room> getRoom(@PathVariable Long id) {
//         return ResponseEntity.ok(roomService.findRoomById(id));
//     }

//     @PostMapping
//     public ResponseEntity<Room> createRoom(
//             @PathVariable Long hotelId,
//             @RequestBody Room room,
//             @RequestHeader("X-User-Id") Long adminId) {
//         return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(room, adminId));
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Room> updateRoom(
//             @PathVariable Long id,
//             @RequestBody Room room,
//             @RequestHeader("X-User-Id") Long adminId) {
//         return ResponseEntity.ok(roomService.updateRoom(id, room, adminId));
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Void> deleteRoom(
//             @PathVariable Long id,
//             @RequestHeader("X-User-Id") Long adminId) {
//         roomService.deleteRoom(id, adminId);
//         return ResponseEntity.noContent().build();
//     }
// }