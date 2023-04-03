package com.playground.demo.persistence.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.playground.demo.persistence.entities.enums.BikeType;
import com.playground.demo.persistence.entities.enums.Parish;
import com.playground.demo.persistence.entities.enums.StationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Objects;

import static com.playground.demo.persistence.entities.enums.AssetStatus.ACTIVE;

@Data
@Builder
@Accessors(chain = true)
@Entity
@Table(name = "stations")
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StationEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Point coordinates;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Parish parish;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StationStatus status;

    @JsonManagedReference
    @OneToMany(mappedBy = "station", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DockEntity> docks;

    public int getAvailableBikesPerType(BikeType type) {
        return (int) docks.stream()
                .map(DockEntity::getBike)
                .filter(Objects::nonNull)
                .filter(bike -> ACTIVE.equals(bike.getStatus()))
                .map(BikeEntity::getType)
                .filter(type::equals)
                .count();
    }

    public int getNumberOfFreeDocks() {
        return (int) docks.stream().filter(DockEntity::isFree).count();
    }
}
