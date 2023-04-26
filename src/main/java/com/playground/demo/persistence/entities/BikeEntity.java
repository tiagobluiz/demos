package com.playground.demo.persistence.entities;

import com.playground.demo.persistence.entities.enums.AssetStatus;
import com.playground.demo.persistence.entities.enums.BikeType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Builder
@Accessors(chain = true)
@Entity
@Table(name = "bikes")
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BikeEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BikeType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    @Column(nullable = false)
    private int kms;

    @Column(nullable = false)
    private LocalDate lastMaintenanceDate;

    @OneToOne(mappedBy = "bike", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DockEntity dock;

// TODO: Review if required
//    @OneToMany(mappedBy = "bike")
//    List<UserReviewEntity> reviews;
//
//    @OneToMany(mappedBy = "bike")
//    List<TripEntity> trips;
}
