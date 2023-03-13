package com.playground.demo.entities;


import com.playground.demo.entities.enums.Parish;
import com.playground.demo.entities.enums.StationStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "stations")
public class StationEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    int id;

    @Column(nullable = false)
    String coordinates;

    @Column(nullable = false)
    String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Parish parish;

    @Column(nullable = false)
    int capacity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    StationStatus status;

    @Column(nullable = false)
    int electricBikes;

    @Column(nullable = false)
    int classicBikes;
}
