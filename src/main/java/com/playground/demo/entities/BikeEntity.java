package com.playground.demo.entities;

import com.playground.demo.entities.enums.BikeStatus;
import com.playground.demo.entities.enums.BikeType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bikes")
public class BikeEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    BikeType type;

    @Column
    @Enumerated(EnumType.STRING)
    BikeStatus status;

    @Column(nullable = false)
    int km;

// TODO: Review if required
//    @OneToMany(mappedBy = "bike")
//    List<UserReviewEntity> reviews;
//
//    @OneToMany(mappedBy = "bike")
//    List<TripEntity> trips;
}
