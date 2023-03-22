package com.playground.demo.persistence.entities;

import com.playground.demo.persistence.entities.enums.AssetStatus;
import com.playground.demo.persistence.entities.enums.BikeType;
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
    AssetStatus status;

    @Column(nullable = false)
    int km;

    @OneToOne(mappedBy = "bike", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    DockEntity dock;

// TODO: Review if required
//    @OneToMany(mappedBy = "bike")
//    List<UserReviewEntity> reviews;
//
//    @OneToMany(mappedBy = "bike")
//    List<TripEntity> trips;
}
