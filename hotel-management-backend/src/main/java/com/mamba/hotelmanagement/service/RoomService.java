
package com.mamba.hotelmanagement.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mamba.hotelmanagement.model.Hotel;
import com.mamba.hotelmanagement.model.Role;
import com.mamba.hotelmanagement.model.Room;
import com.mamba.hotelmanagement.model.User;
import com.mamba.hotelmanagement.repository.HotelRepository;
import com.mamba.hotelmanagement.repository.RoomRepository;
import com.mamba.hotelmanagement.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    public Room addRoom(Room room, Long hotelId, UserDetails userDetails){
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() == null || !Role.ADMIN_HOTEL.equals(user.getRole())) {
            throw new RuntimeException("Only admins can create rooms");
        }
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));
        
        if (!hotel.getAdminUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only admins can manage this hotel");
        }
        room.setHotel(hotel);
        return roomRepository.save(room);   
    }

    public Room updateRoom(Long id, Room updateRoom, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() == null || !Role.ADMIN_HOTEL.equals(user.getRole())) {
            throw new RuntimeException("Only admins can update rooms");
        }
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        if (!room.getHotel().getAdminUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only admins can manage this hotel");
        }
        room.setRoomNumber(updateRoom.getRoomNumber());
        room.setType(updateRoom.getType());
        room.setPrice(updateRoom.getPrice());
        room.setAvailable(updateRoom.isAvailable());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole() == null || !Role.ADMIN_HOTEL.equals(user.getRole())) {
            throw new RuntimeException("Only admins can delete rooms");
        }
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        if (!room.getHotel().getAdminUser().getId().equals(user.getId())) {
            throw new RuntimeException("Only admins can manage this hotel");
        }
        roomRepository.delete(room);
    }

    public Page<Room> getRooms(Long hotelId, String roomType, Boolean isAvailable, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Hotel hotel = null;
        if (hotelId != null) {
            hotel = hotelRepository.findById(hotelId)
                    .orElseThrow(() -> new RuntimeException("Hotel not found!"));
        }

        if (hotel != null) {
            return roomRepository.findByHotel(hotel, pageable);
        }
        if (isAvailable != null) {
            return roomRepository.findByIsAvailable(isAvailable, pageable);
        }
        return roomRepository.findByTypeContainingIgnoreCase(roomType != null ? roomType : "", pageable);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }
}
