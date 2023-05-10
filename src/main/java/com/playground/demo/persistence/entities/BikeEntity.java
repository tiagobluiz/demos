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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * Generates a human-readable bike identifier
     *
     * @return a String in the format [E/C][X], where the first letter is either E or C if the type is electric or
     * classic, respectively, and the X is the bike identifier. e.g.: E123, E1, C321
     */
    public String getBikeDesignation() {
        return type.name().charAt(0) + String.valueOf(id);
    }

// TODO: Review if required
//    @OneToMany(mappedBy = "bike")
//    List<UserReviewEntity> reviews;
//
//    @OneToMany(mappedBy = "bike")
//    List<TripEntity> trips;
}
