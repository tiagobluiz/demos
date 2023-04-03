package com.playground.demo.persistence.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.playground.demo.persistence.entities.enums.AssetStatus;
import jakarta.persistence.*;
import lombok.*;

import static com.playground.demo.persistence.entities.enums.AssetStatus.ACTIVE;
import static java.util.Objects.isNull;

@Data
@Builder
@Entity
@Table(name = "docks")
@EqualsAndHashCode(exclude = {"station", "bike"})
@ToString(exclude = {"station", "bike"})
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DockEntity {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", referencedColumnName = "id", nullable = false)
    private StationEntity station;

    @Column(name = "dock_number", nullable = false)
    private int number;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bike_id", referencedColumnName = "id")
    private BikeEntity bike;

    public boolean isFree() {
        return isNull(bike) && ACTIVE.equals(status);
    }
}
