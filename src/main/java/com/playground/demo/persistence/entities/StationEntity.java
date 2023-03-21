package com.playground.demo.persistence.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.playground.demo.persistence.entities.enums.Parish;
import com.playground.demo.persistence.entities.enums.StationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Data
@Builder
@Accessors(chain = true)
@Entity
@Table(name = "stations", uniqueConstraints = @UniqueConstraint(columnNames = {"coordinates"}))
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StationEntity {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false)
    Point coordinates;

    @Column(nullable = false)
    String address;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Parish parish;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    StationStatus status;

    @JsonManagedReference
    @OneToMany( mappedBy = "station", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<DockEntity> docks;
}
