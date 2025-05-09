package com.mamba.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hotels")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User adminUser;
}